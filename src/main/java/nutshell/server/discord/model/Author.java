package nutshell.server.discord.model;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

public record Author (
        String name,
        String url,
        String iconUrl
){
}