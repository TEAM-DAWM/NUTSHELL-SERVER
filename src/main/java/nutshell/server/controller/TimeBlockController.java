package nutshell.server.controller;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import nutshell.server.annotation.UserId;
import nutshell.server.controller.swagger.TimeBlockControllerSwagger;
import nutshell.server.dto.googleCalender.request.CategoriesDto;
import nutshell.server.dto.timeBlock.request.TimeBlockRequestDto;
import nutshell.server.dto.timeBlock.response.TimeBlocksWithGooglesDto;
import nutshell.server.service.timeBlock.TimeBlockService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.time.LocalDate;

@RestController
@RequestMapping("/api/tasks")
@RequiredArgsConstructor
public class TimeBlockController implements TimeBlockControllerSwagger {
    private final TimeBlockService timeBlockService;

    @Override
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
    public ResponseEntity<TimeBlocksWithGooglesDto> getTimeBlocks(
            @UserId final Long userId,
            @RequestParam @JsonFormat(pattern = "yyyy-MM-dd", timezone = "Asia/Seoul") final LocalDate startDate,
            @RequestParam final Integer range,
            @RequestBody(required = false) final CategoriesDto categoriesDto
    ) {
        return ResponseEntity.ok(timeBlockService.getTimeBlocksWithGoogle(userId, startDate, range, categoriesDto));
    }
}
