package nutshell.server.domain;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import nutshell.server.domain.enums.SocialLoginPlatform;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Getter
@Table(name="users")
@NoArgsConstructor(access= AccessLevel.PROTECTED)
public class User {
    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Long id;

    @Column(name="name", nullable = false)
    private String name;

    @Column(name="email", nullable = false, unique = true)
    private String email;

    @Column(name="serial_id", nullable = false, unique = true)
    private String serialId;

    @Column(name="created_at", nullable = false)
    private LocalDateTime createdAt;

    @Column(name="updated_at", nullable = false)
    private LocalDateTime updatedAt;

    @OneToMany(mappedBy="user", fetch=FetchType.LAZY, cascade=CascadeType.ALL)
    private List<Task> tasks;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private SocialLoginPlatform socialLoginPlatform;

    @OneToMany(mappedBy="user", fetch=FetchType.LAZY, cascade=CascadeType.ALL)
    private List<GoogleCalender> googleCalenders;

    @Builder
    public User(String serialId, SocialLoginPlatform socialLoginPlatform, String name, String email) {
        this.serialId = serialId;
        this.socialLoginPlatform = socialLoginPlatform;
        this.name = name;
        this.email = email;
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }
}
