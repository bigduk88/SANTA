package sparta.enby.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import sparta.enby.dto.*;
import sparta.enby.model.Account;
import sparta.enby.model.Board;
import sparta.enby.model.Review;
import sparta.enby.repository.AccountRepository;
import sparta.enby.repository.BoardRepository;
import sparta.enby.repository.RegistrationRepository;
import sparta.enby.repository.ReviewRepository;
import sparta.enby.security.UserDetailsImpl;
import sparta.enby.uploader.FileUploaderService;
import sparta.enby.uploader.S3Service;

import javax.transaction.Transactional;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class BoardService {
    private final BoardRepository boardRepository;
    private final S3Service uploader;
    private final FileUploaderService fileUploaderService;
    private AccountRepository accountRepository;
    private ReviewRepository reviewRepository;
    private RegistrationRepository registrationRepository;


    public ResponseEntity getBoardList() {
        List<Board> boards = boardRepository.findAll(Sort.by(Sort.Direction.DESC, "createdAt"));
        List<BoardResponseDto> toList = boards.stream().map(
                board -> new BoardResponseDto(
                        board.getId(),
                        board.getBoard_imgUrl(),
                        board.getContents(),
                        board.getTitle(),
                        board.getLocation(),
                        board.getMeetTime(),
                        board.getDeadlineStatus()
                )
        ).collect(Collectors.toList());
        toList.forEach(deadlineStatus -> this.updateDeadlineStatus(deadlineStatus));
        return ResponseEntity.ok().body(toList);
    }

    @Transactional
    public void updateDeadlineStatus(BoardResponseDto boardResponse) {
        Board board = boardRepository.findById(boardResponse.getId()).orElse(null);
        if (board != null) {
            board.update(boardResponse.getBoard_imgUrl(),
                    boardResponse.getTitle(),
                    boardResponse.getContents(),
                    boardResponse.getMeetTime(),
                    boardResponse.getLocation(),
                    boardResponse.getDeadlineStatus());
            boardRepository.save(board);
        }
    }

    //게시글 상세 페이지
    public ResponseEntity getDetailBoard(Long board_id, UserDetailsImpl userDetails) {

        List<Board> boards = boardRepository.findAllById(board_id);
        List<BoardDetailResponseDto> toList = boards.stream().map(
                board -> new BoardDetailResponseDto(
                        board.getId(),
                        board.getCreatedBy(),
                        board.getTitle(),
                        board.getContents(),
                        board.getMeetTime(),
                        board.getLocation(),
                        board.getBoard_imgUrl(),
                        board.getReviews().stream().map(
                                review -> new ReviewResponseDto(
                                        review.getId(),
                                        review.getReview_imgUrl(),
                                        review.getContents(),
                                        review.getBoard().getId()
                                )
                        ).collect(Collectors.toList()),
                        board.getRegistrations().stream().map(
                                registration -> new RegistrationResponseDto(
                                        registration.getId(),
                                        registration.isAccepted(),
                                        registration.getContents(),
                                        registration.getAccount().getNickname(),
                                        registration.getAccount().getProfile_img(),
                                        registration.getKakao_id()
                                )
                        ).collect(Collectors.toList()),
                        board.getDeadlineStatus()
                ))
                .collect(Collectors.toList());
        Map<String, Object> map = new HashMap<>();
        map.put("boards", toList);
        return ResponseEntity.ok().body(map);
    }

    //전체 게시글 목록
    public Page<BoardResponseDto> getBoard(int page, int size, UserDetailsImpl userDetails) {
        PageRequest pageRequest = PageRequest.of(page - 1, size, Sort.by(Sort.Direction.DESC, "createdAt"));
        Page<Board> boards = boardRepository.findAll(pageRequest);
        //        Page<Board> boards = boardRepository.findAllByMeetTimeBefore(now,pageRequest);
        if (page > boards.getTotalPages()) {
            return null;
        }
        if (boards.isEmpty()) {
            return null;
        }
        Page<BoardResponseDto> toMap = boards.map(board -> new BoardResponseDto(
                board.getId(),
                board.getBoard_imgUrl(),
                board.getContents(),
                board.getTitle(),
                board.getLocation(),
                board.getMeetTime(),
                board.getDeadlineStatus()
        ));
        return toMap;
    }

    // 게시글 쓰기
    @Transactional
    public ResponseEntity<String> writeBoard(BoardRequestDto boardRequestDto, UserDetailsImpl userDetails) throws IOException {
//    public ResponseEntity<String> writeBoard(BoardRequestDto boardRequestDto, Account account) throws IOException {
        if (boardRequestDto.getBoardImg() == null || boardRequestDto.getBoardImg().isEmpty()) {
            return new ResponseEntity<>("이미지를 올려주세요", HttpStatus.BAD_REQUEST);
        }
        if (boardRequestDto.getContents() == null || boardRequestDto.getContents().isEmpty()) {
            return new ResponseEntity<>("내용을 기입해주세요", HttpStatus.BAD_REQUEST);
        }
        if (boardRequestDto.getLocation() == null || boardRequestDto.getLocation().isEmpty()) {
            return new ResponseEntity<>("만날 장소를 올려주세요", HttpStatus.BAD_REQUEST);
        }
        if (boardRequestDto.getTitle() == null || boardRequestDto.getTitle().isEmpty()) {
            return new ResponseEntity<>("제목을 입력해 주세요", HttpStatus.BAD_REQUEST);
        }
        if (boardRequestDto.getMeetTime() == null || boardRequestDto.getMeetTime().isEmpty()) {
            return new ResponseEntity<>("모임 시간을 설정해주세요", HttpStatus.BAD_REQUEST);
        }

        String board_imgUrl = fileUploaderService.uploadImage(boardRequestDto.getBoardImg());
        String time = boardRequestDto.getMeetTime();
        LocalDateTime meeting_time = LocalDateTime.parse(time, DateTimeFormatter.ISO_LOCAL_DATE_TIME);
        Board board = Board.builder()
                .title(boardRequestDto.getTitle())
                .meetTime(meeting_time)
                .people_max(boardRequestDto.getPeople_max())
                .location(boardRequestDto.getLocation())
                .contents(boardRequestDto.getContents())
                .board_imgUrl(board_imgUrl)
                .deadlineStatus(false).build();

        Board newBoard = boardRepository.save(board);
//        newBoard.addAccount(userDetails.getAccount());
        return new ResponseEntity<>("성공적으로 저장 완료하였습니다", HttpStatus.OK);
    }

    //게시글 수정
    @Transactional
    public ResponseEntity<String> editBoard(Long board_id, BoardRequestDto boardRequestDto, UserDetailsImpl userDetails) {
        Board board = boardRepository.findById(board_id).orElse(null);
//        if (!board.getAccount().getNickname().equals(userDetails.getUsername())) {
//            return new ResponseEntity<>("다른 사용자의 게시글을 수정하실 수 없습니다", HttpStatus.BAD_REQUEST);
//        }
        if (board == null) {
            return new ResponseEntity<>("없는 게시판입니다", HttpStatus.BAD_REQUEST);
        } else {
            String board_imgUrl = null;
            if (boardRequestDto.getBoardImg() == null || boardRequestDto.getBoardImg().isEmpty()) {
                board_imgUrl = board.getBoard_imgUrl();
            } else {
                fileUploaderService.removeImage(board.getBoard_imgUrl());
                board_imgUrl = fileUploaderService.uploadImage(boardRequestDto.getBoardImg());
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
                contents = boardRequestDto.getContents();
            }

            String location = null;
            if (boardRequestDto.getLocation() == null || boardRequestDto.getLocation().isEmpty()) {
                location = board.getLocation();
            } else {
                location = boardRequestDto.getLocation();
            }

            Boolean deadlineStatus = boardRequestDto.getDeadlineStatus();

            board.update(board_imgUrl, title, contents, time, location, deadlineStatus);
            return new ResponseEntity<>("성공적으로 수정하였습니다", HttpStatus.OK);
        }
    }


    //게시글 삭제
    @Transactional
    public ResponseEntity<String> deleteBoard(Long board_id, Account account) {
        Board board = boardRepository.findById(board_id).orElse(null);
        if (board == null) {
            return new ResponseEntity<>("없는 게시판입니다", HttpStatus.BAD_REQUEST);
        }
        if (!board.getAccount().getNickname().equals(account.getNickname())) {
            return new ResponseEntity<>("없는 사용자이거나 다른 사용자의 게시글입니다", HttpStatus.BAD_REQUEST);
        }
        if (board.getReviews() != null && board.getRegistrations() != null){
            List<Review>reviews = board.getReviews();
            for (Review review : reviews) {
                fileUploaderService.removeImage(review.getReview_imgUrl());
                reviewRepository.deleteAllByBoard(board);
                System.out.println("review deleted");
                registrationRepository.deleteAllByBoard(board);
                System.out.println("register deleted");
            }
        }
        board.deleteBoard(board);
        boardRepository.deleteById(board_id);
        System.out.println("board deleted");
        return new ResponseEntity<>("성공적으로 삭제 하였습니다",HttpStatus.OK);
}

    public ResponseEntity clickFinish(Long board_id, ChangeDeadlineRequestDto changeDeadlineRequestDto, Account account) {
        Board board = boardRepository.findById(board_id).orElseThrow(
                () -> new IllegalArgumentException("해당 아이디의 게시글이 없습니다")
        );
        board.changeDeadlineStatus(changeDeadlineRequestDto);
        return new ResponseEntity<>("성공적으로 마감상태가 변경되었습니다", HttpStatus.OK);
    }

}
