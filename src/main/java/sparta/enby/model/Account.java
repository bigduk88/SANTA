package sparta.enby.model;

import lombok.*;
import javax.persistence.*;
import java.sql.Array;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@ToString(of = {"id", "nickname", "email", "profile_img", "kakaoId"})
public class Account extends BaseTimeEntity{

    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    @Column(name = "account_id")
    private Long id;

    @Column(nullable = false)
    private String nickname;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false)
    private String email;

    @Column(nullable = false)
    private String profile_img;

    @Column(nullable = false)
    private Long kakaoId;

    @ElementCollection(fetch = FetchType.EAGER)
    @Builder.Default
    private List<String> roles = new ArrayList<>();

    @OneToMany(mappedBy = "account")
    @Builder.Default
    List<Board>boards = new ArrayList<>();

    @OneToMany(mappedBy = "account")
    @Builder.Default
    List<Review> reviews = new Array<>();

    @OneToMany(mappedBy = "account")
    @Builder.Default
    List<Registration>registrations = new ArrayList<>();
}