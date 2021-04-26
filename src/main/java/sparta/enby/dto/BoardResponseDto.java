package sparta.enby.dto;

import lombok.Data;
import sparta.enby.model.Board;

import java.util.ArrayList;
import java.util.List;

@Data
public class BoardResponseDto {
    private Long id;
    private String title;
    private String image_url;

    public BoardResponseDto(Long id, String title, String image_url){
        this.id = id;
        this.title = title;
        this.image_url = image_url;
    }
}
