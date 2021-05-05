package sparta.enby.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import sparta.enby.dto.BoardDetailResponseDto;
import sparta.enby.dto.BoardResponseDto;
import sparta.enby.dto.MyBoardResponseDto;
import sparta.enby.dto.ProfileResponseDto;
import sparta.enby.model.Account;
import sparta.enby.model.Board;
import sparta.enby.model.Registration;
import sparta.enby.repository.AccountRepository;
import sparta.enby.repository.BoardRepository;
import sparta.enby.repository.RegistrationRepository;
import sparta.enby.repository.ReviewRepository;
import sparta.enby.security.UserDetailsImpl;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
@RequiredArgsConstructor
public class ProfileService {
    private final BoardRepository boardRepository;
    private final AccountRepository accountRepository;
    private final ReviewRepository reviewRepository;
    private final RegistrationRepository registrationRepository;

    public List<ProfileResponseDto> getProfile(String name, UserDetailsImpl userDetails) {
        Account account = accountRepository.findByNickname(userDetails.getUsername()).orElse(null);
        if (account == null) {
            return null;
        }
        List<Registration> registrations = registrationRepository.findAllByCreatedBy(name);
        List<ProfileResponseDto> toList = new ArrayList<>();
        for (Registration registration : registrations) {
            List<Board> boards = boardRepository.findAllByRegistrations(registration);
            System.out.println(boards);
             toList = boards.stream().map(
                    board -> new ProfileResponseDto(
                            board.getId(),
                            board.getTitle(),
                            board.getBoard_imgUrl(),
                            board.getMeetTime(),
                            board.getLocation(),
                            board.getPeople_max()
                    )
            ).collect(Collectors.toList());
        }
        return toList;
    }
}