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
public class Task {
    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Long id;

    private String name;

    private String description;

    @Enumerated(EnumType.STRING)
    private Status status;

    private String priority;

    private LocalDateTime deadLine;

    private LocalDate assignedDate;

    private LocalDate completionDate;

    private String reminder;

    private String repitition;

    private LocalDate inprogressDate;

    private LocalDate createdAt;

    private LocalDate updatedAt;

    @ManyToOne(targetEntity= User.class, fetch=FetchType.LAZY)
    @JoinColumn(name="user_id")
    private User user;

    @Builder
    public Task(User user, String name, String description, LocalDateTime deadLine) {
        this.user = user;
        this.name = name;
        this.description = description;
        this.status = Status.TODO;
        this.deadLine = deadLine;
        this.createdAt = LocalDate.now();
        this.updatedAt = LocalDate.now();
    }

    @Builder
    public void updateTask(String name, String description, LocalDateTime deadLine) {
        if (name != null)
            this.name = name;
        if (description != null)
            this.description = description;
        if (deadLine != null)
            this.deadLine = deadLine;
        this.updatedAt = LocalDate.now();
    }

    public void updateAssignedDate(LocalDate assignedDate) {
        this.assignedDate = assignedDate;
        this.updatedAt = LocalDate.now();
    }

    public void updateStatus(Status status) {
        this.status = status;
        if(status == Status.IN_PROGRESS)
            this.inprogressDate = LocalDate.now();
        else if(status == Status.DONE)
            this.completionDate = LocalDate.now();
        else if(status == Status.DEFERRED)
            this.assignedDate = null;
        this.updatedAt = LocalDate.now();
    }
}
