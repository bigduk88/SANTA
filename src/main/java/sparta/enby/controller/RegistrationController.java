package sparta.enby.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import sparta.enby.dto.RegisterRequestDto;
import sparta.enby.security.UserDetailsImpl;
import sparta.enby.service.RegistrationService;

@RestController
@RequiredArgsConstructor
public class RegistrationController {
    private final RegistrationService registrationService;

    @PostMapping("/board/mating/{board_id}/register")
    public ResponseEntity makeRegistration(@RequestBody RegisterRequestDto registerRequestDto, @PathVariable Long board_id, @AuthenticationPrincipal UserDetailsImpl userDetails){
        return registrationService.makeRegistration(registerRequestDto, board_id, userDetails);
    }
}
