package sparta.enby.model;

import lombok.*;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@ToString(of = {"id","nickname", "email", "profile_img", "kakaoId"})
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

    @Column(nullable = true)
    private Long kakaoId;

    @Column(nullable = false)
    @Enumerated(value = EnumType.STRING)
    private UserRole role;

    public Account(String nickname, String password, String email, String profile_img, UserRole role, Long kakaoId){
        this.nickname = nickname;
        this.password = password;
        this.email = email;
        this.profile_img =profile_img;
        this.role = role;
        this.kakaoId = kakaoId;
    }

}
