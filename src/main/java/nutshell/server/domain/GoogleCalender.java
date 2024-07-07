package nutshell.server.domain;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor(access= AccessLevel.PROTECTED)
public class GoogleCalender {
    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Long id;

    @Column(name = "refresh_token", nullable = false, unique = true)
    private String refreshToken;

    @Column(name = "access_token", nullable = false, unique = true)
    private String accessToken;

    @Column(name = "email", nullable = false, unique = true)
    private String email;

    @Column(name = "serial_id", nullable = false, unique = true)
    private String serialId;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;

    @ManyToOne(targetEntity= User.class, fetch=FetchType.LAZY)
    @JoinColumn(name="user_id", nullable = false)
    private User user;

    @Builder
    public GoogleCalender(String accessToken, String refreshToken, String email, String serialId, User user) {
        this.accessToken = accessToken;
        this.refreshToken = refreshToken;
        this.serialId = serialId;
        this.email = email;
        this.user = user;
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }

    public void updateTokens(String accessToken){
        this.accessToken = accessToken;
        this.updatedAt = LocalDateTime.now();
    }
}
