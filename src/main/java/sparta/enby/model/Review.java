package sparta.enby.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import sparta.enby.dto.ReviewRequestDto;

import javax.persistence.*;

@NoArgsConstructor
@Getter
@Entity
public class Review {

        @Id
        @GeneratedValue(strategy = GenerationType.AUTO)
        private Long id;

        @Column
        private String title;

        @Column
        private String contents;

        @Column
        private String imgUrl;

        @Column
        private String date;

        public Review(String title, String contents, String imgUrl, String date) {
                this.title = title;
                this.contents = contents;
                this.imgUrl = imgUrl;
                this.date = date;
        }

        public Review(ReviewRequestDto requestDto) {
                this.title = requestDto.getTitle();
                this.contents = requestDto.getContents();
                this.imgUrl = requestDto.getImgUrl();
                this.date = requestDto.getDate();
        }

        public void update(ReviewRequestDto requestDto) {
                this.title = requestDto.getTitle();
                this.contents = requestDto.getContents();
                this.imgUrl = requestDto.getImgUrl();
                this.date = requestDto.getDate();
        }
}
