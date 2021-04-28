package sparta.enby.dto;

import lombok.Getter;

@Getter
public class ReviewRequestDto {
    private Long id;
    private String title;
    private String contents;
    private String imgUrl;
    private String date;
}
