package nutshell.server.domain;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import nutshell.server.dto.type.Status;

import java.time.LocalDate;
import java.time.LocalDateTime;

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

    @Column(name = "target_date", nullable = false)
    private LocalDate targetDate;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;

    @ManyToOne(targetEntity= Task.class, fetch=FetchType.LAZY)
    @JoinColumn(name="task_id", nullable = false)
    private Task task;

    @Builder
    public TaskStatus(Status status, Task task, LocalDate targetDate) {
        this.status = status;
        this.task = task;
        this.targetDate = targetDate;
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }

    public void updateStatus(Status status) {
        this.status = status;
        this.updatedAt = LocalDateTime.now();
    }
}
