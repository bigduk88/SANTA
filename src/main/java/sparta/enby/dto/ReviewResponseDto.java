package sparta.enby.dto;

import lombok.Data;

@Data
public class ReviewResponseDto {
    private Long review_id;
    private String title;
    private String review_imgUrl;
    private String contents;
    private Long board_id;

    public ReviewResponseDto(Long review_id, String title, String review_imgUrl, String contents, Long board_id) {
        this.review_id = review_id;
        this.title = title;
        this.review_imgUrl = review_imgUrl;
        this.contents = contents;
        this.board_id = board_id;

    }

}
