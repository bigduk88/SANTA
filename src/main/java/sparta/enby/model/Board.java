package sparta.enby.model;

import lombok.*;
import org.checkerframework.checker.units.qual.C;
import sparta.enby.dto.BoardRequestDto;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
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

    @Column(nullable = false)
    private String title;

    @Column(nullable = false)
    private String contents;

    @Column(nullable = false)
    private String meetDate;

    @Column(nullable = false)
    private String meetTime;

    @Column(nullable = false)
    private String location;

    @Column(nullable = false)
    private Float longitude;

    @Column(nullable = false)
    private Float latitude;

    @Column(nullable = false)
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
