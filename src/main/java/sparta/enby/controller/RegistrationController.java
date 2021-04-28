package sparta.enby.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import sparta.enby.dto.RegisterRequestDto;
import sparta.enby.service.RegistrationService;

@RestController
@RequiredArgsConstructor
public class RegistrationController {
    private final RegistrationService registrationService;

    @PostMapping("/board/mating/{board_id}/register")
    public ResponseEntity makeRegistration(@RequestBody RegisterRequestDto registerRequestDto, @PathVariable Long board_id){
        return registrationService.makeRegistration(registerRequestDto, board_id);
    }
}
