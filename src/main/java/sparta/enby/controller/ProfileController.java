package sparta.enby.controller;

import com.google.common.collect.Lists;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import sparta.enby.dto.BoardResponseDto;
import sparta.enby.dto.MyBoardResponseDto;
import sparta.enby.dto.ProfileResponseDto;
import sparta.enby.model.Board;
import sparta.enby.security.UserDetailsImpl;
import sparta.enby.service.ProfileService;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
public class ProfileController {
    private final ProfileService profileService;

    @GetMapping("/mypage/{name}")
    public List<ProfileResponseDto> getProfile(@PathVariable String name, @AuthenticationPrincipal UserDetailsImpl userDetails){
        return profileService.getProfile(name, userDetails);
    }
}
