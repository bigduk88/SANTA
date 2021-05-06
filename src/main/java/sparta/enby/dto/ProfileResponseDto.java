package sparta.enby.dto;

import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
public class ProfileResponseDto {

    private Long id;
    private String title;
    private String board_imgUrl;
    private String location;
    private LocalDateTime meetTime;
    private LocalDateTime createdAt;
    private int people_current;
    private int people_max;

    public ProfileResponseDto(Long id, String title, String board_imgUrl, String location, LocalDateTime meetTime, int people_current, int people_max) {
        this.id = id;
        this.title = title;
        this.board_imgUrl = board_imgUrl;
        this.location = location;
        this.meetTime = meetTime;
        this.people_current = people_current;
        this.people_max = people_max;
    }

    public ProfileResponseDto(Long id, String title, LocalDateTime meetTime, LocalDateTime createdAt) {
        this.id = id;
        this.title = title;
        this.meetTime = meetTime;
        this.createdAt = createdAt;
    }
}
