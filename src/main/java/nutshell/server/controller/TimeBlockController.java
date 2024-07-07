package nutshell.server.controller;

import lombok.RequiredArgsConstructor;
import nutshell.server.annotation.UserId;
import nutshell.server.dto.timeBlock.request.TimeBlockCreateDto;
import nutshell.server.service.timeBlock.TimeBlockService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;

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
            @RequestBody final TimeBlockCreateDto timeBlockCreateDto
    ) {
        timeBlockService.update(userId, taskId, timeBlockId, timeBlockCreateDto);
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
}
