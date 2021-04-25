package sparta.enby.service;

import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import sparta.enby.model.Account;
import sparta.enby.model.UserRole;
import sparta.enby.repository.AccountRepository;
import sparta.enby.security.UserDetailsImpl;
import sparta.enby.security.kakao.KakaoUserInfo;


@Service
@RequiredArgsConstructor
public class LoginService {
    private final KakaoOAuth2 kakaoOAuth2;
    private final AuthenticationManager authenticationManager;
    private final PasswordEncoder passwordEncoder;
    private final AccountRepository accountRepository;
    private static final String ADMIN_TOKEN = "AAABnv/xRVklrnYxKZ0aHgTBcXukeZygoC";

    public void kakaoLogin(String authorizedCode){
        KakaoUserInfo userInfo = kakaoOAuth2.getUserInfo(authorizedCode);
        Long kakaoId = userInfo.getId();
        String nickname = userInfo.getNickname();
        String email = userInfo.getEmail();
        String profile_image = userInfo.getProfile_image();

        String username = nickname;
        String password = kakaoId +  ADMIN_TOKEN;
        Account kakaoUser = accountRepository.findByKakaoId(kakaoId).orElse(null);

        if (kakaoUser == null){
            Account loginEmail = accountRepository.findByEmail(email).orElse(null);
            if (loginEmail == null){
                kakaoUser = loginEmail;
                kakaoUser.setKakaoId(kakaoId);
                accountRepository.save(kakaoUser);
            }
            String encodedPassword = passwordEncoder.encode(password);
            UserRole role = UserRole.USER;
            kakaoUser = new Account(nickname, encodedPassword, email, profile_image, role, kakaoId);
            accountRepository.save(kakaoUser);
        }
        UserDetailsImpl userDetails = new UserDetailsImpl(kakaoUser);
        Authentication authentication = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
        SecurityContextHolder.getContext().setAuthentication(authentication);


    }
}
