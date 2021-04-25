package sparta.enby.security;

import lombok.RequiredArgsConstructor;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import sparta.enby.security.kakao.KakaoOAuth2UserService;


@RequiredArgsConstructor
//@Configuration
@EnableWebSecurity
//@EnableGlobalMethodSecurity(securedEnabled = true)
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {
    private final KakaoOAuth2UserService kakaoOAuth2UserService;

    @Override
    protected void configure(HttpSecurity http) throws Exception{
        http.oauth2Login().userInfoEndpoint().userService(kakaoOAuth2UserService);
    }


//
//    @Bean
//    @Override
//    public AuthenticationManager authenticationManagerBean() throws Exception{
//        return super.authenticationManagerBean();
//    }
//
//    @Bean
//    public BCryptPasswordEncoder encodePassword(){
//        return new BCryptPasswordEncoder();
//    }
//    @Override
//    protected void configure(HttpSecurity http) throws Exception{
//        http.csrf().disable();
//        http.headers().frameOptions().disable();
//        http.authorizeRequests().antMatchers("/user/**").permitAll().antMatchers("/login").permitAll()
//        .anyRequest().authenticated().and().formLogin().disable();
//    }
//
}
