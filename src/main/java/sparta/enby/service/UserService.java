package sparta.enby.service;

import lombok.RequiredArgsConstructor;
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

    public String oauth2AuthorizationKakao(String code){
        AuthorizationKakao authorization = oAuth2Kakao.callTokenApi(code);
        Account account = oAuth2Kakao.callGetUserByAcessToken(authorization.getAccess_token());
        return jwtTokenProvider.createToken(account.getNickname(), account.getProfile_img(), account.getRoles());
    }
}
