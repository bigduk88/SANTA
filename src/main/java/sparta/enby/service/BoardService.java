package sparta.enby.service;

import lombok.RequiredArgsConstructor;
import org.springframework.boot.SpringApplication;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import sparta.enby.dto.BoardRequestDto;
import sparta.enby.dto.BoardResponseDto;
import sparta.enby.dto.RegistrationResponseDto;
import sparta.enby.dto.ReviewResponseDto;
import sparta.enby.model.Board;
import sparta.enby.repository.BoardRepository;
import sparta.enby.security.UserDetailsImpl;
import sparta.enby.uploader.FileUploaderService;
import sparta.enby.uploader.S3Service;

import javax.transaction.Transactional;
import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class BoardService {
    private final BoardRepository boardRepository;
    private final S3Service uploader;
    private final FileUploaderService fileUploaderService;


    public ResponseEntity getBoardList() {
        List<Board> boards = boardRepository.findAll(Sort.by(Sort.Direction.DESC, "createdAt"));
        List<BoardResponseDto> toList = boards.stream().map(
                board -> new BoardResponseDto(
                        board.getId(),
                        board.getTitle(),
                        board.getContents(),
                        board.getLocation(),
                        board.getMeetTime()
                )
        ).collect(Collectors.toList());
        return ResponseEntity.ok().body(toList);
    }


    //게시글 상세 페이지
    public ResponseEntity getDetailBoard(Long board_id) {

        List<Board> boards = boardRepository.findAllById(board_id);
        List<BoardResponseDto> toList = boards.stream().map(
                board -> new BoardResponseDto(
                        board.getId(),
                        board.getTitle(),
                        board.getContents(),
                        board.getMeetTime(),
                        board.getLocation(),
                        board.getBoard_imgUrl(),
                        board.getReviews().stream().map(
                                review -> new ReviewResponseDto(
                                        review.getReview_imgUrl(),
                                        review.getContents()
                                )
                        ).collect(Collectors.toList()),
                        board.getAttendList().stream().map(
                                registration -> new RegistrationResponseDto(
                                        registration.isRegister(),
                                        registration.getContents()
                                )
                        ).collect(Collectors.toList())
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
        if (page > boards.getTotalPages()) {
            return null;
        }
        if (boards.equals(null) || boards.isEmpty()) {
            return null;
        }
        Page<BoardResponseDto> toMap = boards.map(board -> new BoardResponseDto(
                board.getId(),
                board.getContents(),
                board.getTitle(),
                board.getLocation(),
                board.getMeetTime()
        ));
        return toMap;
    }

    // 게시글 쓰기
    @Transactional
    public ResponseEntity<String> writeBoard(BoardRequestDto boardRequestDto) throws IOException {
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
                .location(boardRequestDto.getLocation())
                .contents(boardRequestDto.getContents())
                .board_imgUrl(board_imgUrl).build();
        Board newBoard = boardRepository.save(board);
//        newBoard.addAccount(account);

        return new ResponseEntity<>("성공적으로 저장 완료하였습니다", HttpStatus.OK);
    }


    //게시글 수정
    @Transactional
    public ResponseEntity<String> editBoard(Long board_id, BoardRequestDto boardRequestDto) {
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
//            if (boardRequestDto.getLocation().isEmpty() || boardRequestDto.getLocation() == null) {
                location = board.getLocation();
            } else {
                location = boardRequestDto.getLocation();
            }

            board.update(board_imgUrl, title, contents, time, location, board_id);
            return new ResponseEntity<>("성공적으로 수정하였습니다", HttpStatus.OK);
        }
    }


    //게시글 삭제
    @Transactional
    public ResponseEntity<String> deleteBoard(Long board_id) {
        Board board = boardRepository.findById(board_id).orElse(null);
        if (board == null) {
            return new ResponseEntity<>("없는 게시판입니다", HttpStatus.BAD_REQUEST);
        }
        boardRepository.deleteById(board_id);
        return new ResponseEntity<>("성공적으로 삭제 하였습니다", HttpStatus.OK);
    }
}
