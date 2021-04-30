package sparta.enby.service;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import sparta.enby.dto.RegisterRequestDto;
import sparta.enby.model.Board;
import sparta.enby.model.Registration;
import sparta.enby.repository.BoardRepository;
import sparta.enby.repository.RegistrationRepository;

import javax.transaction.Transactional;

@Service
@RequiredArgsConstructor
public class RegistrationService {
    private final RegistrationRepository registrationRepository;
    private final BoardRepository boardRepository;

    @Transactional
    public ResponseEntity makeRegistration(RegisterRequestDto registerRequestDto, Long board_id){
        Board board = boardRepository.findById(board_id).orElse(null);

        if (board == null){
            return new ResponseEntity<>("없는 게시글입니다", HttpStatus.BAD_REQUEST);
        }

        Registration registration = Registration.builder()
                .register(registerRequestDto.isRegister())
                .contents(registerRequestDto.getContents()).build();
        registrationRepository.save(registration);
        registration.addBoard(board);
        return new ResponseEntity<>("성공적으로 신청하였습니다", HttpStatus.OK);
    }
}
