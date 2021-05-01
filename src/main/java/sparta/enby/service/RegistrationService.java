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
import sparta.enby.security.UserDetailsImpl;

import javax.transaction.Transactional;

@Service
@RequiredArgsConstructor
public class RegistrationService {
    private final RegistrationRepository registrationRepository;
    private final BoardRepository boardRepository;

    @Transactional
    public ResponseEntity makeRegistration(RegisterRequestDto registerRequestDto, Long board_id, UserDetailsImpl userDetails){
        Board board = boardRepository.findById(board_id).orElse(null);
        Registration registration = registrationRepository.findByAccount_KakaoId(userDetails.getAccount().getKakaoId()).orElse(null);

        if (board == null){
            return new ResponseEntity<>("없는 게시글입니다", HttpStatus.BAD_REQUEST);
        }

        if (registration!=null){
            return new ResponseEntity<>("중복 요청은 안됩니다.",HttpStatus.BAD_REQUEST);
        }

        Registration newRegistration = Registration.builder()
                .contents(registerRequestDto.getContents())
                .account(userDetails.getAccount())
                .accepted(false)
                .build();
        registrationRepository.save(newRegistration);
        newRegistration.addBoard(board);
        return new ResponseEntity<>("성공적으로 신청하였습니다", HttpStatus.OK);
    }
}
