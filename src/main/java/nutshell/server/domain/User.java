package nutshell.server.domain;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Getter
@Table(name="users")
@NoArgsConstructor(access= AccessLevel.PROTECTED)
public class User {
    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Long id;

    private String name;

    private String email;

    private String serialId;

    private String googleToken;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

    @Builder
    public User(String name, String email, String serialId) {
        this.name = name;
        this.email = email;
        this.serialId = serialId;
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }

    public void updateGoogleToken(String googleToken) {
        this.googleToken = googleToken;
        this.updatedAt = LocalDateTime.now();
    }
}
