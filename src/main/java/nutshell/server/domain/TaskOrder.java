package nutshell.server.domain;

import jakarta.persistence.Id;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.redis.core.RedisHash;

import java.time.LocalDate;
import java.util.List;

@Getter
@RedisHash(value="task_order")
@NoArgsConstructor(access= AccessLevel.PROTECTED)
public class TaskOrder {
    @Id
    private String id;
    private List<Long> taskList;

    @Builder
    public TaskOrder(Long userId, Boolean type, LocalDate targetDate, List<Long> taskList) {
        this.id = userId + "-" + type;
        if (targetDate != null) this.id += "-" + targetDate;
        this.taskList = taskList;
    }
}
