package sparta.enby.dto;

import lombok.Data;
import lombok.Getter;

@Data
public class ReviewResponseDto {
    private String review_imgUrl;
    private String contents;


    public ReviewResponseDto() {}

    public ReviewResponseDto(String review_imgUrl, String contents){
        this.review_imgUrl = review_imgUrl;
        this.contents = contents;
    }

}
