package sparta.enby.security.kakao;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.json.JSONObject;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;
import sparta.enby.model.Account;
import sparta.enby.model.AuthorizationKakao;
import sparta.enby.model.UserRole;
import sparta.enby.repository.AccountRepository;

import javax.transaction.Transactional;
import java.util.Collections;

@Service
@RequiredArgsConstructor
public class OAuth2Kakao {
    private final RestTemplate restTemplate;
    private final ObjectMapper objectMapper;
    private final AccountRepository accountRepository;

    private final String KakaoOauth2ClientId = "17fb08cb376f564b3375667a799fda1f";
    private final String frontendRedirectUrl = "http://localhost:8080";

    public AuthorizationKakao callTokenApi(String code) {
        String grantType = "authorization_code";

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("grant_type", grantType);
        params.add("client_id", KakaoOauth2ClientId);
        params.add("redirect_uri", frontendRedirectUrl + "/callback/kakao");
        params.add("code", code);

        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(params, headers);

        String url = "https://kauth.kakao.com/oauth/token";
        try {
            ResponseEntity<String> response = restTemplate.postForEntity(url, request, String.class);
            AuthorizationKakao authorization = objectMapper.readValue(response.getBody(), AuthorizationKakao.class);
            return authorization;
        } catch (RestClientException | JsonProcessingException ex) {
            ex.printStackTrace();
            throw new IllegalArgumentException("Failed");
        }
    }

    @Transactional
    public Account callGetUserByAcessToken(String accessToken) {
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + accessToken);
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(params, headers);

        String url = "https://kapi.kakao.com/v2/user/me";
        try {
            ResponseEntity<String> response = restTemplate.postForEntity(url, request, String.class);
            JsonNode root = objectMapper.readTree(response.getBody());
            JsonNode kakao_account = root.path("kakao_account");
            String password = "AAABnv/xRVklrnYxKZ0aHgTBcXukeZygoC";
            Account account = accountRepository.findByKakaoId(root.get("id").asLong()).orElse(null);
            if (account == null) {
                accountRepository.save(Account.builder()
                        .kakaoId(root.get("id").asLong())
                        .password(password)
                        .email(kakao_account.get("email").asText())
                        .nickname(kakao_account.path("profile").path("nickname").asText())
                        .profile_img(kakao_account.path("profile").path("profile_image_url").asText())
                        .roles(Collections.singletonList("ROLE_USER"))
                        .build());
            }
            return account;
        } catch (RestClientException | JsonProcessingException ex) {
            ex.printStackTrace();
            throw new IllegalArgumentException("Failed");
        }
    }
}
