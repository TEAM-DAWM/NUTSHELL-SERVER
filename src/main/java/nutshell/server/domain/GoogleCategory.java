package nutshell.server.domain;


import jakarta.persistence.Id;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.redis.core.RedisHash;
import org.springframework.data.redis.core.index.Indexed;

@Getter
@RedisHash(value="google_category")
@NoArgsConstructor(access= AccessLevel.PROTECTED)
public class GoogleCategory {
    @Id
    private String id;
    @Indexed
    private Long googleCalendarId;
    private String name;
    private String color;


    @Builder
    public GoogleCategory(String id, Long googleCalendarId, String name, String color) {
        this.id = id;
        this.googleCalendarId = googleCalendarId;
        this.name = name;
        this.color = color;
    }
}
