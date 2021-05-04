package sparta.enby.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import sparta.enby.service.UserService;

@RequiredArgsConstructor
@Controller
public class UserController {
    private final UserService userService;

    @RequestMapping("/callback/kakao")
    public ResponseEntity oauth2AuthorizationKakao(@RequestParam("code") String code) {
        if (code == null || code.isEmpty()) {
            return ResponseEntity.badRequest().body("인가코드가 없습니다.");
        }
        return ResponseEntity.ok().body(userService.oauth2AuthorizationKakao(code));
    }
}
