package sparta.enby.service;

import com.google.gson.JsonObject;
import lombok.RequiredArgsConstructor;
import org.json.JSONObject;
import org.springframework.stereotype.Service;
import sparta.enby.model.Account;
import sparta.enby.model.AuthorizationKakao;
import sparta.enby.security.kakao.OAuth2Kakao;

@Service
@RequiredArgsConstructor
public class UserService {
    private final OAuth2Kakao oAuth2Kakao;

    public void oauth2AuthorizationKakao(String code){
        AuthorizationKakao authorization = oAuth2Kakao.callTokenApi(code);
        String userInfoFromKako = oAuth2Kakao.callGetUserByAcessToken(authorization.getAccess_token());
        String nickname = userInfoFromKako;

    }
}
