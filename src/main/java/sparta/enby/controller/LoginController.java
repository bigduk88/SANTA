package sparta.enby.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import sparta.enby.service.LoginService;

@Controller
@RequiredArgsConstructor
public class LoginController {

    private final LoginService loginService;

    @GetMapping("/login/oauth2/code/kakao")
    public String kakaoLogin(String code){
        loginService.kakaoLogin(code);

        return "redirect: /";
    }
}
