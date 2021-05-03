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

    @PutMapping("/board/mating/{board_id}/register/{register_id}")
    public ResponseEntity acceptRegistration(@RequestBody RegisterRequestDto registerRequestDto, @PathVariable Long board_id, @PathVariable Long register_id, @AuthenticationPrincipal UserDetailsImpl userDetails){
        return registrationService.acceptRegistration(registerRequestDto, board_id,register_id, userDetails);
    }

    @DeleteMapping("/board/mating/{board_id}/register/{register_id}")
    public ResponseEntity declinedRegistration(@PathVariable Long board_id, @PathVariable Long register_id, @AuthenticationPrincipal UserDetailsImpl userDetails){
        return registrationService.declinedRegistration(board_id, register_id, userDetails);
    }
}
