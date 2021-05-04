package sparta.enby.dto;

import lombok.Data;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Data
public class BoardResponseDto {
    private Long id;
    private String title;
    private String contents;
    private String location;
    private LocalDateTime meetTime;

    public BoardResponseDto(Long id, String contents, String title, String location, LocalDateTime meetTime) {
        this.id = id;
        this.contents = contents;
        this.title = title;
        this.location = location;
        this.meetTime = meetTime;
    }
}