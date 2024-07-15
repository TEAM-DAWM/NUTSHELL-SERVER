package nutshell.server.controller;

import lombok.RequiredArgsConstructor;
import nutshell.server.annotation.UserId;
import nutshell.server.controller.swagger.UserControllerSwagger;
import nutshell.server.dto.user.response.UserDto;
import nutshell.server.service.user.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/users")
public class UserController implements UserControllerSwagger {
    private final UserService userService;

    @Override
    @GetMapping
    public ResponseEntity<UserDto> getUser(@UserId final Long userId){
        return ResponseEntity.ok(userService.getUser(userId));
    }

}
