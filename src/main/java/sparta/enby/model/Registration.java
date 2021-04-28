package sparta.enby.model;

import lombok.*;

import javax.persistence.*;

@Builder
@Getter
@Setter
@Entity
@NoArgsConstructor
@AllArgsConstructor
@ToString(of = {"id", "register", "contents"})
public class Registration extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "regisitration_id")
    private Long id;

    private boolean register;

    private String contents;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "board_id")
    private Board board;

    public void addBoard(Board board){
        this.board = board;
        board.getAttendList().add(this);

    }
}
