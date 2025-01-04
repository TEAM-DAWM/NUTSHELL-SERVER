package nutshell.server.controller;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import nutshell.server.annotation.UserId;
import nutshell.server.dto.timeBlock.request.TimeBlockRequestDto;
import nutshell.server.dto.timeBlock.response.TimeBlockTasksDto;
import nutshell.server.service.timeBlock.TimeBlockService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/tasks")
@RequiredArgsConstructor
public class TimeBlockController {
    private final TimeBlockService timeBlockService;

    @PostMapping("/{taskId}/time-blocks")
    public ResponseEntity<Void> createTimeBlock(
            @UserId final Long userId,
            @PathVariable final Long taskId,
            @RequestBody @Valid final TimeBlockRequestDto timeBlockRequestDto
    ) {
        URI uri = URI.create(timeBlockService.create(userId, taskId, timeBlockRequestDto).getId().toString());
        return ResponseEntity.created(uri).build();
    }

    @PatchMapping("/{taskId}/time-blocks/{timeBlockId}")
    public ResponseEntity<Void> updateTimeBlock(
            @UserId final Long userId,
            @PathVariable final Long taskId,
            @PathVariable final Long timeBlockId,
            @RequestBody @Valid final TimeBlockRequestDto timeBlockRequestDto
    ) {
        timeBlockService.update(userId, taskId, timeBlockId, timeBlockRequestDto);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{taskId}/time-blocks/{timeBlockId}")
    public ResponseEntity<Void> deleteTimeBlock(
            @UserId final Long userId,
            @PathVariable final Long taskId,
            @PathVariable final Long timeBlockId
    ) {
        timeBlockService.delete(userId, taskId, timeBlockId);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/time-blocks")
    public ResponseEntity<TimeBlockTasksDto> getTimeBlocks(
            @UserId final Long userId,
            @RequestParam @JsonFormat(pattern = "yyyy-MM-dd", timezone = "Asia/Seoul") final LocalDate startDate,
            @RequestParam final Integer range
    ) {
        return ResponseEntity.ok(timeBlockService.getTimeBlocks(userId, startDate, range));
    }
}
