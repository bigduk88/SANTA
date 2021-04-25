package sparta.enby.security.kakao;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.json.JSONObject;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;
import sparta.enby.model.Account;
import sparta.enby.model.UserRole;
import sparta.enby.repository.AccountRepository;

import javax.servlet.http.HttpSession;
import javax.transaction.Transactional;
import java.util.Collections;
import java.util.Map;

@Slf4j
@RequiredArgsConstructor
@Service
public class KakaoOAuth2UserService extends DefaultOAuth2UserService {
    private final HttpSession httpSession;
    private final AccountRepository accountRepository;
    private final BCryptPasswordEncoder encodePassword;

    @Override
    @Transactional
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException{
        OAuth2User oAuth2User = super.loadUser(userRequest);
        Map<String, Object> attributes = oAuth2User.getAttributes();
        JSONObject body = new JSONObject(attributes);
        Long kakaoId = body.getLong("id");
        String email = body.getJSONObject("kakao_account").getString("email");
        String password = "AAABnv/xRVklrnYxKZ0aHgTBcXukeZygoC";
        password = encodePassword.encode(password);
        String nickname = body.getJSONObject("properties").getString("nickname");
        String profile_img = body.getJSONObject("properties").getString("profile_image");
        UserRole role = UserRole.USER;
        httpSession.setAttribute("login_info", attributes);

        Account account = new Account(nickname, password, email, profile_img, role, kakaoId);
        accountRepository.save(account);
        return new DefaultOAuth2User(Collections.singleton(new SimpleGrantedAuthority("ROLE_USER")), attributes, "id");
    }
}
