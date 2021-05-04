package sparta.enby.model;

import lombok.*;
import sparta.enby.dto.RegisterRequestDto;

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
    @Column(name = "registration_id")
    private Long id;

    private boolean register;

    private String contents;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "account_id")
    private Account account;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "board_id")
    private Board board;

    public void addBoard(Board board) {
        this.board = board;
        this.account = account;
        board.getAttendList().add(this);
        account.getRegistrations().add(this);
    }

    public void update(RegisterRequestDto registrationRequestDto) {
        this.accepted = registrationRequestDto.isAccepted();
    }
}
