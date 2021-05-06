package sparta.enby.dto;

import lombok.Data;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Data
public class BoardResponseDto {
    private Long id;
    private String title;
    String board_imgUrl;
    private String contents;
    private String location;
    private LocalDateTime meetTime;
    private LocalDateTime createdAt;


    public BoardResponseDto(Long id, String title, String board_imgUrl, String contents, String location, LocalDateTime meetTime) {
        this.id = id;
        this.title = title;
        this.board_imgUrl = board_imgUrl;
        this.contents = contents;
        this.location = location;
        this.meetTime = meetTime;
    }

    public BoardResponseDto(Long id, String title, LocalDateTime meetTime, LocalDateTime createdAt) {
        this.id = id;
        this.title = title;
        this.meetTime = meetTime;
        this.createdAt = createdAt;
    }
}
