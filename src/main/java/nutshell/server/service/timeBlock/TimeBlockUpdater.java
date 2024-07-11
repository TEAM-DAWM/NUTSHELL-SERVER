package nutshell.server.service.timeBlock;

import nutshell.server.domain.TimeBlock;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
public class TimeBlockUpdater {

    public void updateTime(
            final TimeBlock timeBlock,
            final LocalDateTime startTime,
            final LocalDateTime endTime
    ){
        timeBlock.updateTimeBlock(startTime, endTime);
    }
}
