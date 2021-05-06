package sparta.enby.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.util.MultiValueMap;
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
import java.util.Map;
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
        List<ProfileResponseDto> myboardsList = new ArrayList<>();
        List<ProfileResponseDto> createdboardList = new ArrayList<>();
        for (Registration registration : registrations) {
            List<Board> boardList = boardRepository.findAllByRegistrations(registration);
            createdboardList.addAll(boardList.stream().map(
                    board -> new ProfileResponseDto(
                            board.getId(),
                            board.getTitle(),
                            board.getBoard_imgUrl(),
                            board.getLocation(),
                            board.getMeetTime(),
                            board.getPeople_current(),
                            board.getPeople_max()
                    )
            ).collect(Collectors.toList()));
        }
        List<Board> myboards = boardRepository.findAllByCreatedBy(name);
        myboardsList = myboards.stream().map(
                board -> new ProfileResponseDto(
                        board.getId(),
                        board.getTitle(),
                        board.getMeetTime(),
                        board.getCreatedAt()
                )
        ).collect(Collectors.toList());
        return toList = Stream.concat(createdboardList.stream(), myboardsList.stream()).collect(Collectors.toList());
    }
}