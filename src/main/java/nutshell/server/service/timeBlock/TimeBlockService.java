package nutshell.server.service.timeBlock;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import nutshell.server.domain.Task;
import nutshell.server.domain.TaskStatus;
import nutshell.server.domain.TimeBlock;
import nutshell.server.domain.User;
import nutshell.server.dto.googleCalender.request.CategoriesDto;
import nutshell.server.dto.googleCalender.response.GoogleSchedulesDto;
import nutshell.server.dto.timeBlock.request.TimeBlockCreateDto;
import nutshell.server.dto.timeBlock.request.TimeBlockUpdateDto;
import nutshell.server.dto.timeBlock.response.TimeBlocksDto;
import nutshell.server.dto.timeBlock.response.TimeBlocksWithGooglesDto;
import nutshell.server.dto.type.Status;
import nutshell.server.exception.BusinessException;
import nutshell.server.exception.IllegalArgumentException;
import nutshell.server.exception.code.BusinessErrorCode;
import nutshell.server.exception.code.IllegalArgumentErrorCode;
import nutshell.server.service.googleCalendar.GoogleCalendarService;
import nutshell.server.service.task.TaskRetriever;
import nutshell.server.service.taskStatus.TaskStatusRemover;
import nutshell.server.service.taskStatus.TaskStatusRetriever;
import nutshell.server.service.taskStatus.TaskStatusSaver;
import nutshell.server.service.user.UserRetriever;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class TimeBlockService {
    private final TimeBlockUpdater timeBlockEditor;
    private final TimeBlockRemover timeBlockRemover;
    private final TimeBlockRetriever timeBlockRetriever;
    private final TimeBlockSaver timeBlockSaver;
    private final TaskRetriever taskRetriever;
    private final UserRetriever userRetriever;
    private final TaskStatusRetriever taskStatusRetriever;
    private final GoogleCalendarService googleCalendarService;
    private final TaskStatusSaver taskStatusSaver;
    private final TaskStatusRemover taskStatusRemover;
    @Transactional
    public TimeBlock create(
            final Long userId,
            final Long taskId,
            final TimeBlockCreateDto timeBlockCreateDto
    ) {
        //시작시간이 끝나는 시간보다 늦다면
        if (timeBlockCreateDto.startTime().isAfter(timeBlockCreateDto.endTime())) {
            throw new BusinessException(BusinessErrorCode.TIME_CONFLICT);
        }
        User user = userRetriever.findByUserId(userId);
        Task task = taskRetriever.findByUserAndId(user, taskId);
        //시작시간과 끝나는 시간 사이에 다른 타임블록이 있다면
        if (timeBlockRetriever.existsByTaskUserAndStartTimeBetweenAndEndTimeBetween(
                user,
                timeBlockCreateDto.startTime(),
                timeBlockCreateDto.endTime()
        )) {
            throw new BusinessException(BusinessErrorCode.DUP_TIMEBLOCK_CONFLICT);
        }
        //timeBlock생성일에 이미 같은 task의 timeBlock이 있다면
        if (timeBlockRetriever.existsByTaskAndStartTimeBetweenAndEndTimeBetween(
                task,
                timeBlockCreateDto.startTime().toLocalDate().atStartOfDay(),
                timeBlockCreateDto.startTime().toLocalDate().atTime(23,59,59)
        )) {
            throw new BusinessException(BusinessErrorCode.DUP_DAY_TIMEBLOCK_CONFLICT);
        }
        if (!taskStatusRetriever.existsByTaskAndTargetDate(task, timeBlockCreateDto.startTime().toLocalDate())) {
            if (timeBlockCreateDto.startTime().toLocalDate().isAfter(LocalDate.now().minusDays(1)) && task.getStatus() == Status.TODO) {
                taskStatusRemover.removeAll(taskStatusRetriever.findAllByTask(task, timeBlockCreateDto.startTime().toLocalDate()));
                taskStatusSaver.save(
                        TaskStatus.builder()
                                .task(task)
                                .status(task.getStatus())
                                .targetDate(timeBlockCreateDto.startTime().toLocalDate())
                                .build()
                );
            } else
                throw new BusinessException(BusinessErrorCode.DENY_DAY_TIMEBLOCK_CONFLICT);
        }
        TaskStatus taskStatus = taskStatusRetriever.findByTaskAndTargetDate(task, timeBlockCreateDto.startTime().toLocalDate());
        return timeBlockSaver.save(TimeBlock.builder()
                .taskStatus(taskStatus)
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
            final TimeBlockUpdateDto timeBlockUpdateDto
    ){
        //시작시간이 끝나는 시간보다 늦다면
        if (timeBlockUpdateDto.startTime().isAfter(timeBlockUpdateDto.endTime())) {
            throw new BusinessException(BusinessErrorCode.TIME_CONFLICT);
        }
        User user = userRetriever.findByUserId(userId);
        Task task = taskRetriever.findByUserAndId(user, taskId);
        TimeBlock timeBlock = timeBlockRetriever.findByTaskAndId(task, timeBlockId);
        //자기자신을 제외한 다른 타임블록 중 시작시간과 끝나는 시간 사이에 다른 타임블록이 있다면
        if (timeBlockRetriever.existsByTaskUserAndStartTimeBetweenAndEndTimeBetweenAndIdNot(
                user,
                timeBlockId,
                timeBlockUpdateDto.startTime(),
                timeBlockUpdateDto.endTime()
        )) {
            throw new BusinessException(BusinessErrorCode.DUP_TIMEBLOCK_CONFLICT);
        }
        timeBlockEditor.updateTime(timeBlock,timeBlockUpdateDto.startTime(), timeBlockUpdateDto.endTime());
    }

    @Transactional
    public void delete(
            final Long userId,
            final Long taskId,
            final Long timeBlockId
    ){
        User user = userRetriever.findByUserId(userId);
        Task task = taskRetriever.findByUserAndId(user, taskId);
        TimeBlock timeBlock = timeBlockRetriever.findByTaskAndId(task, timeBlockId);
        timeBlockRemover.remove(timeBlock);
    }

    public TimeBlocksWithGooglesDto getTimeBlocksWithGoogle(
            final Long userId,
            final LocalDate startDate,
            final Integer range,
            final CategoriesDto categoriesDto
    ){
        if (range < 1) {
            throw new IllegalArgumentException(IllegalArgumentErrorCode.INVALID_RANGE);
        }

        LocalDateTime startTime = startDate.atStartOfDay();
        LocalDateTime endTime = startDate.plusDays(range-1).atTime(23,59,59);
        User user = userRetriever.findByUserId(userId);
        List<TimeBlocksDto> tasks = taskRetriever.findAllByUserAndTimeBlocks(user, startTime, endTime)
                .stream().map(
                        task -> TimeBlocksDto.builder()
                                .id(task.getId())
                                .name(task.getName())
                                .timeBlocks(timeBlockRetriever.findAllByTaskIdAndTimeRange(task, startTime, endTime))
                                .build()
                ).toList();
        List<GoogleSchedulesDto> googles = googleCalendarService.getGoogleCalenders(userId, startDate, range, categoriesDto);
        return TimeBlocksWithGooglesDto.builder()
                .tasks(tasks)
                .googles(googles)
                .build();
    }
}
