package nutshell.server.dto.googleCalender.response;

import lombok.Builder;

import java.util.List;

@Builder
public record GoogleCategoriesDto(
        List<GoogleCategoryDto> categories
) {
    @Builder
    public record GoogleCategoryDto(
            String id,
            String name,
            String color
    ) {
    }
}
