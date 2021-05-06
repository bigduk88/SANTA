package sparta.enby.dto;

import lombok.Data;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Data
public class BoardDetailResponseDto {
    private String createdBy;
    private Long id;
    private String title;
    private String contents;
    private String board_imgUrl;
    private String location;
    private LocalDateTime meetTime;
    private List<ReviewResponseDto> reviews = new ArrayList<>();
    private List<RegistrationResponseDto> registrations = new ArrayList<>();
    private int people_current;
    private int people_max;

    public BoardDetailResponseDto(Long id, String createdBy, String title, String contents, LocalDateTime meetTime, String location, String board_imgUrl, int people_current, int people_max, List<ReviewResponseDto> reviews, List<RegistrationResponseDto> registrations) {
        this.id = id;
        this.createdBy = createdBy;
        this.title = title;
        this.contents = contents;
        this.meetTime = meetTime;
        this.location = location;
        this.board_imgUrl = board_imgUrl;
        this.people_current = people_current;
        this.people_max = people_max;
        this.reviews = reviews;
        this.registrations = registrations;
    }

    public BoardDetailResponseDto(Long id, String title, String board_imgUrl, String location, LocalDateTime meetTime) {
        this.id = id;
        this.title = title;
        this.board_imgUrl = board_imgUrl;
        this.location = location;
        this.meetTime = meetTime;
    }
}
