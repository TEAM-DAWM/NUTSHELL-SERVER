package nutshell.server.service.timeBlock;

import lombok.RequiredArgsConstructor;
import nutshell.server.domain.Task;
import nutshell.server.domain.TimeBlock;
import nutshell.server.domain.User;
import nutshell.server.dto.timeBlock.request.TimeBlockCreateDto;
import nutshell.server.dto.type.Status;
import nutshell.server.exception.BusinessException;
import nutshell.server.exception.code.BusinessErrorCode;
import nutshell.server.service.task.TaskUpdater;
import nutshell.server.service.task.TaskRetriever;
import nutshell.server.service.user.UserRetriever;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class TimeBlockService {
    private final TimeBlockUpdater timeBlockEditor;
    private final TimeBlockRemover timeBlockRemover;
    private final TimeBlockRetriever timeBlockRetriever;
    private final TimeBlockSaver timeBlockSaver;
    private final TaskRetriever taskRetriever;
    private final TaskUpdater taskUpdater;
    private final UserRetriever userRetriever;
    @Transactional
    public TimeBlock create(
            final Long userId,
            final Long taskId,
            final TimeBlockCreateDto timeBlockCreateDto
    ) {
        if (timeBlockCreateDto.startTime().isAfter(timeBlockCreateDto.endTime())) {
            throw new BusinessException(BusinessErrorCode.DATE_CONFLICT);
        }
        User user = userRetriever.findById(userId);
        Task task = taskRetriever.findByUserAndId(user, taskId);
        if (timeBlockRetriever.existsByTaskAndStartTimeBetweenAndEndTimeBetween(
                task.getId(),
                timeBlockCreateDto.startTime().toLocalDate()
        )) {
            throw new BusinessException(BusinessErrorCode.MULTI_CONFLICT);
        }
        if (task.getStatus() == Status.DONE){
            //완료된 날짜보다 endTime이 더 뒤라면
            if (task.getCompletionDate().isBefore(timeBlockCreateDto.endTime())) {
                throw new BusinessException(BusinessErrorCode.DONE_CONFLICT);
            }
        } else if (task.getStatus() == Status.IN_PROGRESS){
            //타겟 당일이 아니라면
            if (!timeBlockCreateDto.targetDate().isEqual(timeBlockCreateDto.startTime().toLocalDate())) {
                throw new BusinessException(BusinessErrorCode.IN_PROGRESS_CONFLICT);
            }
        } else if (task.getStatus() == Status.TODO){
            //오늘보다 이전이라면
            if (LocalDateTime.now().isAfter(timeBlockCreateDto.startTime())) {
                throw new BusinessException(BusinessErrorCode.TODO_CONFLICT);
            }
            taskUpdater.updateAssignedDate(task, timeBlockCreateDto.startTime().toLocalDate());
        }
        return timeBlockSaver.save(TimeBlock.builder()
                .task(task)
                .startTime(timeBlockCreateDto.startTime())
                .endTime(timeBlockCreateDto.endTime())
                .build()
        );
    }

    @Transactional
    public void update(
            final Long userId,
            final Long taskId,
            final Long timeBlockId,
            final TimeBlockCreateDto timeBlockCreateDto
    ){
        User user = userRetriever.findById(userId);
        Task task = taskRetriever.findByUserAndId(user, taskId);
        TimeBlock timeBlock = timeBlockRetriever.findByTaskAndId(task, timeBlockId);
        timeBlockEditor.updateTime(timeBlock,timeBlockCreateDto.startTime(), timeBlockCreateDto.endTime());
    }

    @Transactional
    public void delete(
            final Long userId,
            final Long taskId,
            final Long timeBlockId
    ){
        User user = userRetriever.findById(userId);
        Task task = taskRetriever.findByUserAndId(user, taskId);
        TimeBlock timeBlock = timeBlockRetriever.findByTaskAndId(task, timeBlockId);
        timeBlockRemover.remove(timeBlock);
    }
}
