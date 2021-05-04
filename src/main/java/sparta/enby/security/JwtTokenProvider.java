package sparta.enby.security;

import com.sun.org.apache.xml.internal.security.algorithms.SignatureAlgorithm;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletRequest;
import java.util.Base64;
import java.util.List;
import java.util.regex.Pattern;

@RequiredArgsConstructor
@Component
public class JwtTokenProvider {

    private String secretKey = "webfirewood";

    private long tokenValidTime = 30 * 60 * 1000L;

    private final UserDetailsService userDetailsService;

    @PostConstruct
    protected void init() {
        secretKey = Base64.getEncoder().encodeToString(secretKey.getBytes());
    }

    public String createToken(String nickname, String profileImgUrl, List<String> roles) {
        Claims claims = Jwts.claims();
        claims.put("nickname", nickname);
        claims.put("profileImgUrl", StringUtils.hasText(profileImgUrl) ? profileImgUrl : "");
        Date now = new date();
        return Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(now)
                .setExpiration(new Date(now.getTime() +tokenValidTime))
                .signWith(SignatureAlgorithm.HS256, secretKey)
                .compact();
    }

    public Authentication getAuthentication(String token) {
        UserDetails userDetails = userDetailsService.loadUserByUsername(this.getUserPk(token));
        return new UsernamePasswordAuthenticationToken(userDetails, "", UserDetails.getAuthorities());
    }

    public String getUserPk(String token) {
        return Jwts.parser().setSignningKey(secretKey).parseClaimsJws(token).getbody().get("nickname").toString();
    }

    public String resolveToken(HttpServletRequest requset){
        String token = request.getheader("Authorization");
        if (StringUtils.hasText(token)) {
            if (Pattern.matches(("^Bearer.*", token)) {
                token = token.replaceAll("^Bearer( )*", "");
                return token;
            }
        }
        return token;
    }

    public boolean validateToken(String jwtToken) {
        try {
            Jws<Claims> claims = Jwts.parser().setSigningKey(secretKey).parseClaimsJws(jwtToken);
            return !claims.getBody().getExpiration().before(new date());
        } catch (Exception e) {
            return false;
        }
    }
}
