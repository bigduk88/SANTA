package sparta.enby.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import sparta.enby.service.UserService;

@RequiredArgsConstructor
@RestController
public class UserController {
    private final UserService userService;

    @GetMapping("/callback/kakao")
    public ResponseEntity<String> oauth2AuthorizationKakao(@RequestParam("code") String code){
        return userService.oauth2AuthorizationKakao(code);

    }
}
