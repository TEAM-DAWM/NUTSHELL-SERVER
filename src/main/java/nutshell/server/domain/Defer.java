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
public class Defer {

    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Long id;

    @Column(name="created_at", nullable = false)
    private LocalDateTime createdAt;

    @ManyToOne(targetEntity = Task.class, fetch = FetchType.LAZY)
    @JoinColumn(name="task_id", nullable = false)
    private Task task;

    @Builder
    public Defer(Task task) {
        this.task = task;
        this.createdAt = LocalDateTime.now();
    }
}
