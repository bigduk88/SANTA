package sparta.enby.service;

import com.fasterxml.jackson.databind.JsonNode;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import sparta.enby.model.Account;
import sparta.enby.model.AuthorizationKakao;
import sparta.enby.security.JwtTokenProvider;
import sparta.enby.security.kakao.OAuth2Kakao;

@Service
@RequiredArgsConstructor
public class UserService {
    private final OAuth2Kakao oAuth2Kakao;
    private final JwtTokenProvider jwtTokenProvider;

    public ResponseEntity<String> oauth2AuthorizationKakao(String code){
        AuthorizationKakao authorization = oAuth2Kakao.callTokenApi(code);
        Account account = oAuth2Kakao.callGetUserByAcessToken(authorization.getAccess_token());
        System.out.println(account);
        System.out.println(account.getNickname());
        System.out.println(account.getProfile_img());
        return ResponseEntity.ok().body(jwtTokenProvider.createToken(account.getNickname(), account.getProfile_img(), account.getRoles()));
    }
}
