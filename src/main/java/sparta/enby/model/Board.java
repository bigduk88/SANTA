package sparta.enby.model;

import lombok.*;
import sparta.enby.dto.BoardRequestDto;
import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Builder
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@ToString(of = {"id", "title", "contents", "location", "board_imgUrl"})
public class Board extends BaseEntity {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    @Column(name = "board_id")
    private Long id;

    private String title;

    private String contents;

    private String meetDate;

    private String meetTime;

    private String location;

    private Float longitude;

    private Float latitude;

    private String board_imgUrl;

    @ManyToOne
    @JoinColumn(name = "account_id")
    private Account account;

    @OneToMany (mappedBy = "board")
    @Builder.Default
    List<Review> reviews = new ArrayList<>();

    public void addAccount(Account account) {
        this.account = account;
    }

    public void update(BoardRequestDto boardRequestDto, Long board_id){
        this.board_imgUrl = board_imgUrl;
        this.title = title;
        this.contents = contents;
        this.latitude = latitude;
        this.longitude = longitude;
        this.meetTime = meetTime;
        this.location = location;
    }
}
