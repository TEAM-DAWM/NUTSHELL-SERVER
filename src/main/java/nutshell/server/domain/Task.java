package nutshell.server.domain;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import nutshell.server.dto.type.Status;

import java.time.LocalDate;
import java.time.LocalDateTime;
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

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private Status status;

    @Column(name = "priority")
    private String priority;

    @Column(name = "dead_line")
    private LocalDateTime deadLine;

    @Column(name = "assigned_date")
    private LocalDate assignedDate;

    @Column(name = "completion_date")
    private LocalDate completionDate;

    @Column(name = "reminder")
    private String reminder;

    @Column(name = "repetition")
    private String repetition;

    @Column(name = "inprogress_date")
    private LocalDate inprogressDate;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;

    @ManyToOne(targetEntity= User.class, fetch=FetchType.LAZY)
    @JoinColumn(name="user_id", nullable = false)
    private User user;

    @OneToMany(mappedBy="task", fetch=FetchType.LAZY, cascade=CascadeType.ALL)
    private List<TimeBlock> timeBlocks;

    @OneToMany(mappedBy="task", fetch=FetchType.LAZY, cascade=CascadeType.ALL)
    private List<Defer> defers;

    @OneToMany(mappedBy="task", fetch=FetchType.LAZY, cascade=CascadeType.ALL)
    private List<InProgressDay> inProgressDays;


    @Builder
    public Task(User user, String name, String description, LocalDateTime deadLine) {
        this.user = user;
        this.name = name;
        this.description = description;
        this.status = Status.TODO;
        this.deadLine = deadLine;
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }

    public void updateTask(String name, String description, LocalDateTime deadLine) {
        if (name != null)
            this.name = name;
        if (description != null)
            this.description = description;
        if (deadLine != null)
            this.deadLine = deadLine;
        this.updatedAt = LocalDateTime.now();
    }

    public void updateAssignedDate(LocalDate assignedDate) {
        this.assignedDate = assignedDate;
        this.updatedAt = LocalDateTime.now();
    }

    public void updateStatus(Status status) {
        this.status = status;
        if(status == Status.IN_PROGRESS) {
            this.inprogressDate = LocalDate.now();
            this.completionDate = null;
        }
        else if (status == Status.DONE) {
            this.completionDate = LocalDate.now();
            this.inprogressDate = null;
        }
        else if(status == Status.DEFERRED)
            this.assignedDate = null;
        else {
            this.inprogressDate = null;
            this.completionDate = null;
        }
        this.updatedAt = LocalDateTime.now();
    }
}
