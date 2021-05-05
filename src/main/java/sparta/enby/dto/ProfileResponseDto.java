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
    private int people_max;
    private List<MyBoardResponseDto> myBoardResponseDto;
    private List<BoardDetailResponseDto>boardDetailResponseDtos;

    public ProfileResponseDto(Long id, String title, String board_imgUrl, LocalDateTime meetTime, String location, int people_max) {
        this.id = id;
        this.title = title;
        this.board_imgUrl = board_imgUrl;
        this.meetTime = meetTime;
        this.location = location;
        this.people_max = people_max;
    }
}
