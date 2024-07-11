package nutshell.server.domain;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Entity
@Getter
@NoArgsConstructor(access= AccessLevel.PROTECTED)
public class InProgressDay {
    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Long id;

    @Column(name = "inprogress_date", nullable = false)
    private LocalDate inprogressDate;

    @Column(name = "created_at", nullable = false)
    private LocalDate createdAt;

    @ManyToOne(targetEntity = Task.class, fetch = FetchType.LAZY)
    @JoinColumn(name = "task_id", nullable = false)
    private Task task;

    public InProgressDay(LocalDate inprogressDate, Task task) {
        this.inprogressDate = inprogressDate;
        this.task = task;
        this.createdAt = LocalDate.now();
    }

    public void update(){
        this.createdAt = LocalDate.now();
    }
}
