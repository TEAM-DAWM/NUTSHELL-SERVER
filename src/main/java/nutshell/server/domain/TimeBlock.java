package nutshell.server.domain;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor(access= AccessLevel.PROTECTED)
public class TimeBlock {
    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Long id;

    private LocalDate date;

    private String startTime;

    private String endTime;

    @ManyToOne(targetEntity= Task.class, fetch=FetchType.LAZY)
    @JoinColumn(name="task_id")
    private Task task;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

    @Builder
    public TimeBlock(LocalDate date, String startTime, String endTime, Task task) {
        this.date = date;
        this.startTime = startTime;
        this.endTime = endTime;
        this.task = task;
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }

    @Builder
    public void updateTimeBlock(String startTime, String endTime) {
        this.startTime = startTime;
        this.endTime = endTime;
        this.updatedAt = LocalDateTime.now();
    }
}
