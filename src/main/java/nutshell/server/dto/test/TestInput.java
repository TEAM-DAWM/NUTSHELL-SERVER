package nutshell.server.dto.test;

import jakarta.validation.constraints.NotBlank;

public record TestInput(
        @NotBlank
        String name,
        String email
) {
}
