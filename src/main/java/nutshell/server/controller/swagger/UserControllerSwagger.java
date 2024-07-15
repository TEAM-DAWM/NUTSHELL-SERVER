package nutshell.server.controller.swagger;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import nutshell.server.annotation.UserId;
import nutshell.server.dto.user.response.UserDto;
import org.springframework.http.ResponseEntity;

@Tag(name = "User", description = "[UserController] 유저 관련 API 입니다.")
public interface UserControllerSwagger {

    @Operation(summary = "유저 정보 조회", description = "Google 사용자 정보를 조회하는 GET API 입니다.")
    @ApiResponses(
            {
                    @ApiResponse(responseCode = "200",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = UserDto.class))),

                    @ApiResponse(responseCode = "401", description = "인증되지 않은 사용자입니다.", content = @Content(mediaType = "application/json")),
                    @ApiResponse(responseCode = "404", description = "존재하지 않는 사용자 입니다.", content = @Content(mediaType = "application/json")),
                    @ApiResponse(responseCode = "500", description = "서버 내부 오류입니다.", content = @Content(mediaType = "application/json"))
            }
    )
    ResponseEntity<UserDto> getUser(@UserId final Long userId);
}
