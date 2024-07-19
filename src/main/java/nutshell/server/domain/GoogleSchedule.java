package nutshell.server.domain;

import jakarta.persistence.Id;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import nutshell.server.dto.googleCalender.response.GoogleSchedulesDto;
import org.springframework.data.redis.core.RedisHash;
import org.springframework.data.redis.core.index.Indexed;

import java.util.List;

@Getter
@RedisHash(value="google_schedule")
@NoArgsConstructor(access= AccessLevel.PROTECTED)
public class GoogleSchedule {
    @Id
    private String id;
    @Indexed
    private Long googleCalendarId;
    @Indexed
    private String googleCategoryId;
    private String name;
    private String color;
    private List<GoogleSchedulesDto.GoogleScheduleDto> schedules;

    @Builder
    public GoogleSchedule(String id, Long googleCalendarId, String googleCategoryId, String name, String color, List<GoogleSchedulesDto.GoogleScheduleDto> schedules) {
        this.id = id;
        this.googleCalendarId = googleCalendarId;
        this.googleCategoryId = googleCategoryId;
        this.name = name;
        this.color = color;
        this.schedules = schedules;
    }
}
