package nutshell.server.discord.external;

import nutshell.server.discord.exception.ErrorLogAppenderException;
import nutshell.server.discord.model.*;
import nutshell.server.discord.model.Image;
import nutshell.server.discord.util.ApiCallUtil;

import java.awt.*;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class DiscordWebHook {
    private final String urlString;
    private String username;
    private String avataUrl;
    private boolean tts;

    private final List<EmbedObject> embedObjects = new ArrayList<>();

    public DiscordWebHook(String urlString, String username, String avataUrl, boolean tts) {
        this.urlString = urlString;
        this.username = username;
        this.avataUrl = avataUrl;
        this.tts = tts;
    }

    public void addEmbed(EmbedObject embed) {
        this.embedObjects.add(embed);
    }

    public void execute() throws IOException {
        if (this.embedObjects.isEmpty()) {
            throw new RuntimeException("컨텐츠를 설정하거나 하나 이상의 Embed Object를 추가해야 합니다.");
        }

        try {
            ApiCallUtil.callDiscordAppenderPostAPI(
                    this.urlString, createDiscordEmbedObject(
                            this.embedObjects, initializerDiscordSendForJsonObject(new JsonObject())
                    )
            );
        } catch (IOException ioException) {
            throw ioException;
        }
    }

    private JsonObject initializerDiscordSendForJsonObject(JsonObject jsonObject) {
        jsonObject.put("username", this.username);
        jsonObject.put("avatar_url", this.avataUrl);
        jsonObject.put("tts", this.tts);
        return jsonObject;
    }

    private JsonObject createDiscordEmbedObject(List<EmbedObject> embeds, JsonObject jsonObject) {
        if (embeds.isEmpty()) {
            throw new ErrorLogAppenderException();
        }

        List<JsonObject> embedObjects = new ArrayList<>();

        for (EmbedObject embed : embeds) {
            JsonObject jsonEmbed = new JsonObject();

            jsonEmbed.put("title", embed.getTitle());
            jsonEmbed.put("description", embed.getDescription());
            jsonEmbed.put("url", embed.getUrl());

            processDiscordEmbedColor(embed, jsonEmbed);
            processDiscordEmbedFooter(embed.getFooter(), jsonEmbed);
            processDiscordEmbedImage(embed.getImage(), jsonEmbed);
            processDiscordEmbedThumbnail(embed.getThumbnail(), jsonEmbed);
            processDiscordEmbedAuthor(embed.getAuthor(), jsonEmbed);
            processDiscordEmbedMessageFields(embed.getFields(), jsonEmbed);

            embedObjects.add(jsonEmbed);

        }
        jsonObject.put("embeds", embedObjects.toArray());
        return jsonObject;
    }

    private void processDiscordEmbedColor(EmbedObject embed, JsonObject jsonEmbed) {
        if (embed.getColor() != null) {
            Color color = embed.getColor();
            int rgb = color.getRed();
            rgb = (rgb << 8) + color.getGreen();
            rgb = (rgb << 8) + color.getBlue();

            jsonEmbed.put("color", rgb);
        }
    }

    private void processDiscordEmbedFooter(Footer footer, JsonObject jsonEmbed) {
        if(footer != null){
            JsonObject jsonFooter = new JsonObject();
            jsonFooter.put("text", footer.text());
            jsonFooter.put("icon_url", footer.iconUrl());
            jsonEmbed.put("footer", jsonFooter);
        }
    }

    private void processDiscordEmbedImage(Image image, JsonObject jsonEmbed){
        if (image != null) {
            JsonObject jsonImage = new JsonObject();
            jsonImage.put("url", image.url());
            jsonEmbed.put("image", jsonImage);
        }
    }

    private void processDiscordEmbedThumbnail(Thumbnail thumbnail, JsonObject jsonEmbed){
        if (thumbnail != null) {
            JsonObject jsonThumbnail = new JsonObject();
            jsonThumbnail.put("url", thumbnail.url());
            jsonEmbed.put("thumbnail", jsonThumbnail);
        }
    }

    private void processDiscordEmbedAuthor(Author author, JsonObject jsonEmbed){
        if (author != null) {
            JsonObject jsonAuthor = new JsonObject();
            jsonAuthor.put("name", author.name());
            jsonAuthor.put("url", author.url());
            jsonAuthor.put("icon_url", author.iconUrl());
            jsonEmbed.put("author", jsonAuthor);
        }
    }

    private void processDiscordEmbedMessageFields(List<Field> fields, JsonObject jsonEmbed) {
        List<JsonObject> jsonFields = new ArrayList<>();

        for (Field field : fields) {
            JsonObject jsonField = new JsonObject();

            jsonField.put("name", field.name());
            jsonField.put("value", field.value());
            jsonField.put("inline", field.inline());

            jsonFields.add(jsonField);
        }
        jsonEmbed.put("fields", jsonFields.toArray());
    }
}