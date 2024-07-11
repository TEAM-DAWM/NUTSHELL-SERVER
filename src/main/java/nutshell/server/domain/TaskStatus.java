package nutshell.server.domain;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import nutshell.server.dto.type.Status;

import java.time.LocalDate;

@Entity
@Getter
@NoArgsConstructor(access= AccessLevel.PROTECTED)
public class TaskStatus {
    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private Status status;

    @Column(name = "created_at", nullable = false)
    private LocalDate createdAt;

    @Column(name = "updated_at", nullable = false)
    private LocalDate updatedAt;

    @ManyToOne(targetEntity= Task.class, fetch=FetchType.LAZY)
    @JoinColumn(name="task_id", nullable = false)
    private Task task;

    @Builder
    public TaskStatus(Status status, Task task) {
        this.status = status;
        this.task = task;
        this.createdAt = LocalDate.now();
        this.updatedAt = LocalDate.now();
    }

    public void updateStatus(Status status) {
        this.status = status;
        this.updatedAt = LocalDate.now();
    }
}
