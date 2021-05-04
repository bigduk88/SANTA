package sparta.enby.service;

import com.sun.codemodel.internal.JFieldVar;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import sparta.enby.Uploader.FileUploaderService;
import sparta.enby.Uploader.S3Service;
import sparta.enby.dto.*;
import sparta.enby.model.Account;
import sparta.enby.model.Board;
import sparta.enby.model.Review;
import sparta.enby.repository.AccountRepository;
import sparta.enby.repository.BoardRepository;
import sparta.enby.repository.RegistrationRepository;
import sparta.enby.repository.ReviewRepository;
import sparta.enby.security.UserDetailsImpl;

import javax.transaction.Transactional;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class BoardService {

    private final BoardRepository boardRepository;
    private final S3Service uploadfer;
    private final FileUploaderService fileUploaderService;
    private AccountRepository accountRepository;
    private ReviewRepository reviewRepository;
    private RegistrationRepository registrationRepository;

    public ResponseEntity getBoardList() {
        List<Board> board = boardRepository.findAll(Sort.by(Sort.Direction.DESC,"createdAt"));
        List<BoardResponseDto> toList = board.stream().map(
                board -> new BoardRequestDto(
                        board.getId(),
                        board.getTitle(),
                        board.getContents(),
                        board.getLocation(),
                        board.getMeetTime()
                )
        ).collect(Collectors.toList());
        return ResponseEntity.ok().body(toList);
    }

    // 게시글 상세 페이지
    public ResponseEntity getDetailBoard(Long board_id, UserDetailsImpl userDetails) {

        List<Board> boards = boardRepository.findAllById(board_id);
        List<BoardDetailResponseDto> toList = boards.stream().map(
                board -> new BoardDetailResonseDto(
                        board.getId(),
                        board.getCreatedBy(),
                        board.getTitle(),
                        board.getMeetTime(),
                        board.getLocation(),
                        board.getBoard_imgUrl(),
                        board,getReviews().stream().map(
                                review -> new ReviewResponseDto(
                                        review.getId(),
                                        review.getReview_imgUrl(),
                                        review.getContents(),
                                        review.getBoard().getId()
                                )
                        ).collect(Collectors.toList()),
                        board.getAttendList().stream().map(
                                registration -> new RegistrationResponseDto(
                                        registration.isAccepted(),
                                        registration.getContents(),
                                        registration.getAccount(),getNickname(),
                                        registration.getAccount().getProfile_img(),
                                        registration.getAccount().getKakaoId()
                                )
                        ).collect(Collectors.toList())
                ))
                .collect(Collectors.toList());
        Map<String, Object> map = new HashMap<>();
        map.put("boards", toList);
        return ResponseEntity.ok().body(map);
    }

    // 전체 게시글 목록
    public Page<boardResponseDto> getBoard(int page, int size, UseDetailsImpl userDetails) {
        PageRequest pageRequest = PageRequest.of(page -1, size, Sort.by(Sort.Direction.DESC,"createdAt"));
        Page<Board> boards = boardRepository.findAll(pageRequest);
        if (page > boards.getTotalPages()) {
            return null;
        }
        if (board.isEmpty()) {
            return null;
        }
        Page<BoardResponseDto> toMap = board.map(board -> new BaordResponseDto (
                board.getId(),
                board.getContents(),
                board.getTitle(),
                board.getLoacation(),
                board.getMeettime()
        ));
        return toMap;
    }

    //게시글 쓰기
    @Transactional
    public ResponseEntity<String> writeBoard(BoardRequsetDto boardRequsetDto, UserDetailsImpl userDetails) throws IOException {
        if (boardRequsetDto.getBoardImg() == null || boardRequsetDto.getBoardImg().isEmpty()) {
            return new ResponseEntity<>("이미지를 올려주세요", HttpStatus.BAD_REQUEST);
        }
        if (boardRequsetDto.getContents() == null || boardRequsetDto.getContents().isEmpty()) {
            return new ResponseEntity<>("내용을 기입해주세요", HttpStatus.BAD_REQUEST);
        }
        if (boardRequsetDto.getLocation() == null || boardRequsetDto.getLocation().isEmpty()) {
            return new ResponseEntity<>("만날 장소를 올려주세요", HttpStatus.BAD_REQUEST);
        }
        if (boardRequsetDto.getTitle() == null || boardRequsetDto.getTitle().isEmpty()) {
            return new ResponseEntity<>("제목을 입력해 주세요", HttpStatus.BAD_REQUEST);
        }
        if (boardRequsetDto.getMeetTime() == null || boardRequsetDto.getMeetTime().isEmpty()) {
            return new ResponseEntity<>("모임 시간을 설정해 주세요", HttpStatus.BAD_REQUEST);
        }

        String board_imgUrl = fileUploaderService.uploadImage(boardRequsetDto.getBoardImg());
        String time = boardRequsetDto.getMeetTime();
        LocalDateTime meeting_time = LocalDateTime.parse(time, DateTimeFormatter.ISO_LOCAL_DATE_TIME);
        Board board = Board.builder()
                .title(boardRequsetDto.getTitle())
                .meetTime(meeting_time)
                .location(boardRequsetDto.getLocation())
                .contents(boardRequsetDto.getContents())
                .board_imgUrl(board_imgUrl).build();
        Board newboard = boardRepository.save(board);
        newBoard.addAccount(userDetails.getAccount());

        return new ResponseEntity<>("성공적으로 저장 완료하였씁니다", HttpStatus.OK);
    }

    // 게시글 수정
    @Transactional
    public ResponseEntity<String> editBoard(Long board_id, BoardRequestDto boardRequestDto, UserDetailsImpl userDetails) {
        Account account = accountRepository.findByKakaoId(userDetails.getAccount().getKakaoId());
        if (account == null) {
            return new ResponseEntity<>("존재하지 않는 사용자입니다", HttpStatus.BAD_REQUEST);
        }
        if (account != userDetails.getAccount()) {
            return new ResponseEntity<>("다른 사용자의 게시물은 수정할 수 없습니다", HttpStatus.BAD_REQUEST);
        }

        Board board = boardRepository.findById(board_id).orElse(null);

        if (board == null) {
            return new ResponseEntity<>("없는 게시판입니다", HttpStatus.BAD_REQUEST);
        } else {
            String board_imgUrl = null;
            if (boardRequestDto.getBoardImg() == null || boardRequestDto.getBoardImg().isEmpty()) {
                board_imgUrl = board.getBoard_imgUrl();
            }
            else {
                fileUploaderService.removeImage(board.getBoard_imgUrl());
                board_imgUrl = fileUploaderService.uploadImage(boardRequestDto.getTitle().isEmpty());
            }

            String title = null;
            if (boardRequestDto.getTitle() == null || boardRequestDto.getTitle().isEmpty()) {
                title = board.getTitle();
            } else {
                title = boardRequestDto.getTitle();
            }

            LocalDateTime time = null;
            if (boardRequestDto.getMeetTime() == null || boardRequestDto.getMeetTime().isEmpty()) {
                time = board.getMeetTime();
            } else {
                time = LocalDateTime.parse(boardRequestDto.getMeetTime());
            }

            String contents = null;
            if (boardRequestDto.getContents() == null || boardRequestDto.getContents().isEmpty()) {
                contents = board.getContents();
            } else {
                time = LocalDateTime.parse(boardRequestDto.getMeetTime());
            }

            String location = null;
            if (boardRequestDto.getLocation() == null || boardRequestDto.getLocation().isEmpty()) {
                location = board.getLocation();
            } else {
                location = boardRequestDto.getLocation();
            }

            board.update(board_imgUrl, title, contents, time, location);
            return new ResponseEntity<>("성공적으로 수정하였습니다", HttpStatus.OK);
        }
    }

    // 게시글 삭제
    @Transactional
    public ResposeEntity<String> deleteBoard(Long board_id, Account account) {
        Board board = boardRepository.findById(board_id).orElse(null);
        if (board == null) {
            return new ResponseEntity<>("없는 게시판 입니다", HttpStatus.BAD_REQUEST);
        }
        if (!board.getAccount().equals(account)) {
            return new ResponseEntity<>("없는 사용자이거나 다른 사용자의 게시글 입니다", HttpStatus.BAD_REQUEST);
        }
        if (board.getReviews() != null && board.getRegistrations() != null) {


            List<Review> reviews = reviewRepository.findAllByBoard(board);
            for (Review review : reviews) {
                fileUploaderService.removeImage(review.getReview_imgUrl());
                reviewRepository.deleteAllByBoard(board);
                System.out.println("review deleted");
                registrationRepository.deletaAllByBoard(board);
                System.out.println("register deleted");
            }
        }
        boardRepository.deleteById(board_id);
        System.out.println("board deleted");
        return new ResponseEntity<>("성공적으로 삭제 하였습니다", HttpStatus.OK)
    }
}