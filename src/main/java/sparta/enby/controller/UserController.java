package sparta.enby.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import sparta.enby.service.UserService;

@RequiredArgsConstructor
@Controller
public class UserController {
    private final UserService userService;

    @GetMapping("/callback/kakao")
    public String oauth2AuthorizationKakao(@RequestParam("code") String code){
        userService.oauth2AuthorizationKakao(code);
        return "redirect:/";
    }
}
