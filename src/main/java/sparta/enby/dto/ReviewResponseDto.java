package sparta.enby.dto;

import lombok.Data;
import lombok.Getter;
import sparta.enby.model.Board;

@Data
public class ReviewResponseDto {
    private Long review_id;
    private String review_imgUrl;
    private String contents;
    private Long board_id;

    public ReviewResponseDto() {}

    public ReviewResponseDto(Long review_id, String contents) {
        this.review_id = review_id;
        this.contents = contents;
    }

    public ReviewResponseDto(Long review_id, String review_imgUrl, String contents, Long board_id){
        this.review_id =review_id;
        this.review_imgUrl = review_imgUrl;
        this.contents = contents;
        this.board_id = board_id;

    }

}
