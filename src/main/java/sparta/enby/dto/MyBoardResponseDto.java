package sparta.enby.dto;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class MyBoardResponseDto {
    private Long id;
    private String title;
    private LocalDateTime meetTime;
    private LocalDateTime createdAt;


    public MyBoardResponseDto(Long id, String title, LocalDateTime meetTime, LocalDateTime createdAt) {
        this.id = id;
        this.title = title;
        this.meetTime = meetTime;
        this.createdAt = createdAt;
    }
}
