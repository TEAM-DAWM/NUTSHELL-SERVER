package nutshell.server.controller.swagger;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import nutshell.server.annotation.UserId;
import nutshell.server.dto.timeBlock.request.TimeBlockRequestDto;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;

@Tag(name = "TimeBlock", description = "[TimeBlockController] TimeBlock 관련 API 입니다.")
public interface TimeBlockControllerSwagger {

    @Operation(summary = "Task 타임블로킹 생성", description = "드래그를 통해서 Task의 Time Blocking을 생성하는 POST API 입니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "204 No Content", description = "Success", content = @Content),
            @ApiResponse(responseCode = "200 Ok", description = "Failure",
                    content = @Content(mediaType = "application/json",
                            examples = {
                                    @ExampleObject(name = "startTime이 endTime보다 늦을 경우", value =
                                            "{\"code\": \"conflict\", \"data\": null, \"message\": \"시작 시간은 종료 시간 이전이어야 합니다.\"}"),
                                    @ExampleObject(name = "다른 timeBlock과 겹칠 경우", value =
                                            "{\"code\": \"conflict\", \"data\": null, \"message\": \"지정된 시간 범위 내에 이미 TimeBlock이 있습니다.\"}"),
                                    @ExampleObject(name = "같은 날에 해당 task가 이미 timeBlock을 가지고 있을 경우", value =
                                            "{\"code\": \"conflict\", \"data\": null, \"message\": \"지정된 날짜의 작업에 대한 TimeBlock이 이미 있습니다.\"}"),
                                    @ExampleObject(name = "timeBlock을 할당할 수 없는 날일 경우", value =
                                            "{\"code\": \"conflict\", \"data\": null, \"message\": \"지정된 날짜에 대한 TimeBlock을 생성할 수 없습니다.\"}")
                            }
                    )
            ),
            @ApiResponse(responseCode = "400", description = "Bad Request",
                    content = @Content(mediaType = "application/json",
                            examples = {
                                    @ExampleObject(name = "필요한 값이 들어오지 않았거나 올바르지 않은 값이 들어왔을 경우", value =
                                            "{\"code\": \"error\", \"data\": null, \"message\": \"인자의 형식이 올바르지 않습니다.\"}"),
                                    @ExampleObject(name = "올바른 날짜 형식이 아닐 경우", value =
                                            "{\"code\": \"error\", \"data\": null, \"message\": \"날짜 형식이 올바르지 않습니다.\"}"),
                            }
                    )
            ),
            @ApiResponse(responseCode = "401", description = "Unauthorized",
                    content = @Content(mediaType = "application/json", schema = @Schema(example =
                            "{\"code\": \"error\", \"data\": null, \"message\": \"인증되지 않은 사용자입니다.\"}"))
            ),
            @ApiResponse(responseCode = "404", description = "Not Found",
                    content = @Content(mediaType = "application/json",
                            examples = {
                                    @ExampleObject(name = "존재하지 않는 사용자일 경우", value =
                                            "{\"code\": \"error\", \"data\": null, \"message\": \"존재하지 않는 사용자 입니다.\"}"),
                                    @ExampleObject(name = "존재하지 않는 task의 task id일 경우", value =
                                            "{\"code\": \"error\", \"data\": null, \"message\": \"존재하지 않는 Task 입니다.\"}"),
                                    @ExampleObject(name = "timeBlock을 만들고자 하는 날에 task가 없을 경우", value =
                                            "{\"code\": \"error\", \"data\": null, \"message\": \"해당기간의 Task를 찾을 수 없습니다.\"}"),
                            }
                    )
            ),
            @ApiResponse(responseCode = "500", description = "Internal Server Error",
                    content = @Content(mediaType = "application/json", schema = @Schema(example =
                            "{\"code\": \"error\", \"data\": null, \"message\": \"서버 내부 오류입니다.\"}"))
            )
    })
    ResponseEntity<Void> createTimeBlock(
            @UserId final Long userId,
            @PathVariable final Long taskId,
            @RequestBody @Valid final TimeBlockRequestDto timeBlockRequestDto
    );
}