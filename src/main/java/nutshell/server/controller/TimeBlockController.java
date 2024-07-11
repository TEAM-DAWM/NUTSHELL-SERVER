package nutshell.server.controller;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.RequiredArgsConstructor;
import nutshell.server.annotation.UserId;
import nutshell.server.dto.googleCalender.request.CategoriesDto;
import nutshell.server.dto.timeBlock.request.TimeBlockCreateDto;
import nutshell.server.dto.timeBlock.request.TimeBlockUpdateDto;
import nutshell.server.dto.timeBlock.response.TimeBlocksWithGooglesDto;
import nutshell.server.service.timeBlock.TimeBlockService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.time.LocalDate;

@RestController
@RequestMapping("/api/tasks")
@RequiredArgsConstructor
public class TimeBlockController {
    private final TimeBlockService timeBlockService;

    @PostMapping("/{taskId}/time-blocks")
    public ResponseEntity<Void> createTimeBlock(
            @UserId final Long userId,
            @PathVariable final Long taskId,
            @RequestBody final TimeBlockCreateDto timeBlockCreateDto
    ) {
        URI uri = URI.create(timeBlockService.create(userId, taskId, timeBlockCreateDto).getId().toString());
        return ResponseEntity.created(uri).build();
    }

    @PatchMapping("/{taskId}/time-blocks/{timeBlockId}")
    public ResponseEntity<Void> updateTimeBlock(
            @UserId final Long userId,
            @PathVariable final Long taskId,
            @PathVariable final Long timeBlockId,
            @RequestBody final TimeBlockUpdateDto timeBlockUpdateDto
    ) {
        timeBlockService.update(userId, taskId, timeBlockId, timeBlockUpdateDto);
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
    public ResponseEntity<TimeBlocksWithGooglesDto> getTimeBlocks(
            @UserId final Long userId,
            @RequestParam @JsonFormat(pattern = "yyyy-MM-dd", timezone = "Asia/Seoul") final LocalDate startDate,
            @RequestParam final Integer range,
            @RequestBody(required = false) final CategoriesDto categoriesDto
    ) {
        return ResponseEntity.ok(timeBlockService.getTimeBlocksWithGoogle(userId, startDate, range, categoriesDto));
    }
}
