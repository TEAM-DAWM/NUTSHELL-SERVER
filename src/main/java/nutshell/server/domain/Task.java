package nutshell.server.domain;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import nutshell.server.dto.type.Status;
import org.springframework.cglib.core.Local;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor(access= AccessLevel.PROTECTED)
public class Task {
    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "description")
    private String description;

    @Column(name = "priority")
    private String priority;

    // deadLine 을 date와 time 두개로 쪼갬
    @Column(name = "dead_line_date")
    private LocalDate deadLineDate;

    @Column(name = "dead_line_time")
    private LocalTime deadLineTime;

    @Column(name = "assigned_date")
    private LocalDate assignedDate;

    @Column(name = "reminder")
    private String reminder;

    @Column(name = "repetition")
    private String repetition;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private Status status;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;

    @ManyToOne(targetEntity = User.class, fetch = FetchType.LAZY)
    @JoinColumn(name="user_id", nullable = false)
    private User user;

    @OneToMany(mappedBy = "task", fetch = FetchType.LAZY, cascade = CascadeType.REMOVE)
    private List<TaskStatus> taskStatuses;

    @Builder
    public Task(User user, String name, String description, LocalDate deadLineDate, LocalTime deadLineTime) {
        this.user = user;
        this.name = name;
        this.description = description;
        this.deadLineDate = deadLineDate;
        this.deadLineTime = deadLineTime;
        this.status = Status.TODO;
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }


    public void updateTask(String name, String description, LocalDate deadLineDate, LocalTime deadLineTime) {
        if (name != null)
            this.name = name;
        if (description != null)
            this.description = description;
        this.deadLineDate = deadLineDate;
        this.deadLineTime = deadLineTime;
        this.updatedAt = LocalDateTime.now();
    }

    public void updateAssignedDate(LocalDate assignedDate) {
        this.assignedDate = assignedDate;
        this.updatedAt = LocalDateTime.now();
    }

    public void updateStatus(Status status) {
        this.status = status;
        this.updatedAt = LocalDateTime.now();
    }
}
