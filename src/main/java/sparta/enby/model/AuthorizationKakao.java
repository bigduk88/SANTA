package sparta.enby.model;

import lombok.Getter;

@Getter
public class AuthorizationKakao {
    private String access_token;
    private String tokoen_type;
    private String refresh_token;
    private String expires_in;
    private String scope;
    private String refresh_token_expires_in;
}
