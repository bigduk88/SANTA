package sparta.enby.security.kakao;

import org.json.JSONObject;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

@Component
public class KakaoOAuth2 {
    public KakaoUserInfo getUserInfo(String authorizedCode){
        String accessToken = getAccessToken(authorizedCode);
        KakaoUserInfo userInfo = getUserInfoByToken(accessToken);

        return userInfo;
    }

    private String getAccessToken(String authorizedCode){
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-type", "application/x-www-form-urlencoded;charset=utf-8");

        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("grant_type","authorization_cde");
        params.add("client_id", "17fb08cb376f564b3375667a799fda1f");
        params.add("redirect_uri","http://localhost:8080/login/oauth2/code/kakao");
        params.add("code",authorizedCode);

        RestTemplate rt = new RestTemplate();
        HttpEntity<MultiValueMap<String, String>> kakaoTokenRequest = new HttpEntity<>(params,headers);

        ResponseEntity<String> response = rt.exchange("https://kauth.kakao.com/oauth/token", HttpMethod.POST, kakaoTokenRequest, String.class);

        String tokenJson = response.getBody();
        JSONObject rjson = new JSONObject(tokenJson);
        String accessToken = rjson.getString("access_token");

        return accessToken;
    }

    private KakaoUserInfo getUserInfoByToken(String accessToken){
        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", "Bearer" + accessToken);
        headers.add("Content-type","application/x-www-urlencoded;charset=utf-8");

        RestTemplate rt = new RestTemplate();
        HttpEntity<MultiValueMap<String, String>> kakaoProfileRequest = new HttpEntity<>(headers);

        ResponseEntity<String> response = rt.exchange("https://kauth.kakao.com/v2/user/me", HttpMethod.POST, kakaoProfileRequest, String.class);

        JSONObject body = new JSONObject(response.getBody());
        Long id = body.getLong("id");
        String email = body.getJSONObject("kakao_account").getString("email");
        String nickname = body.getJSONObject("properties").getString("nickname");
        String profile_image = body.getJSONObject("properties").getString("profile_image");

        return new KakaoUserInfo(id, email, nickname, profile_image);
    }
}
