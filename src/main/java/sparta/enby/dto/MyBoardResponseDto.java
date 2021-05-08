package sparta.enby.dto;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class MyBoardResponseDto {
    private Long id;
    private String title;
    private String board_name;
    private LocalDateTime meetTime;
    private LocalDateTime createdAt;


    public MyBoardResponseDto(Long id, String title, LocalDateTime meetTime, LocalDateTime createdAt) {
        this.id = id;
        this.title = title;
        this.board_name = "작성한 글";
        this.meetTime = meetTime;
        this.createdAt = createdAt;
    }
}
