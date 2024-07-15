package nutshell.server.domain;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

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

    @Column(name="given_name")
    private String givenName;

    @Column(name="family_name")
    private String familyName;

    @Column(name="image", nullable = false)
    private String image;

    @Column(name="email", nullable = false, unique = true)
    private String email;

    @Column(name="serial_id", nullable = false, unique = true)
    private String serialId;

    @Column(name="created_at", nullable = false)
    private LocalDateTime createdAt;

    @Column(name="updated_at", nullable = false)
    private LocalDateTime updatedAt;

    @OneToMany(mappedBy="user", fetch = FetchType.LAZY, cascade=CascadeType.REMOVE)
    private List<Task> tasks;

    @OneToMany(mappedBy="user", fetch = FetchType.LAZY, cascade=CascadeType.REMOVE)
    private List<GoogleCalendar> googleCalendars;

    @Builder

    public User(String givenName, String familyName, String image, String email, String serialId) {
        this.givenName = givenName;
        this.familyName = familyName;
        this.image = image;
        this.email = email;
        this.serialId = serialId;
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }
}
