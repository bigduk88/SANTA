package sparta.enby.dto;

import lombok.Data;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Data
public class BoardResponseDto {
    private Long id;
    private String title;
    String board_imgUrl;
    private String contents;
    private String location;
    private LocalDateTime meetTime;
    private Boolean deadlineStatus;

    public BoardResponseDto(Long id, String board_imgUrl,String contents, String title, String location, LocalDateTime meetTime, Boolean deadlineStatus) {
        this.id = id;
        this.board_imgUrl = board_imgUrl;
        this.contents = contents;
        this.title = title;
        this.location = location;
        this.meetTime = meetTime;
        this.deadlineStatus = deadlineStatus;
    }

//    public BoardResponseDto(Long id, String title, String contents, String location, LocalDateTime meetTime, Boolean deadlineStatus) {
//        this.id = id;
//        this.title = title;
//        this.contents = contents;
//        this.location = location;
//        this.meetTime = meetTime;
//        this.deadlineStatus = deadlineStatus;
//    }
}