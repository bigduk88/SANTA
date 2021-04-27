package sparta.enby.dto;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class BoardResponseDto {
    private Long id;
    private String title;
    private String contents;
    private String board_imgUrl;
    private String location;
    private Float longitude;
    private Float latitude;
    private String meetDate;
    private String meetTime;
    private List<ReviewResponseDto> reviews = new ArrayList<>();

    public BoardResponseDto(Long id, String title, String contents, String meetTime, String location, Float latitude, Float longitude, String board_imgUrl, List<ReviewResponseDto>reviews) {
        this.id = id;
        this.title = title;
        this.contents = contents;
        this.meetTime = meetTime;
        this.location = location;
        this.longitude = longitude;
        this.latitude = latitude;
        this.board_imgUrl = board_imgUrl;
        this.reviews = reviews;
    }

    public BoardResponseDto(Long id, String contents, String title, List<ReviewResponseDto> collect) {
    }
}
