package nutshell.server.dto.googleCalender.response;

import lombok.Builder;

import java.util.List;

@Builder
public record GoogleEmailsDto(
        List<GoogleEmailDto> emails
) {
    @Builder
    public record GoogleEmailDto(
            String email,
            List<GoogleCategoryDto> categories
    ){
        @Builder
        public record GoogleCategoryDto(
                String id,
                String name,
                String color
        ) {
        }
    }
}
