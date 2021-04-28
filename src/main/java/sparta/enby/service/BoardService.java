package sparta.enby.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import sparta.enby.dto.BoardRequestDto;
import sparta.enby.dto.BoardResponseDto;
import sparta.enby.dto.ReviewResponseDto;
import sparta.enby.model.Board;
import sparta.enby.repository.BoardRepository;
import sparta.enby.security.UserDetailsImpl;
import sparta.enby.uploader.Uploader;

import javax.transaction.Transactional;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class BoardService {
    private final BoardRepository boardRepository;
    private final Uploader uploader;

    //게시글 상세 페이지
    public ResponseEntity getDetailBoard(Long board_id) {

        List <Board> boards = boardRepository.findAllById(board_id);
        List <BoardResponseDto> toList = boards.stream().map(
                board -> new BoardResponseDto(
                        board.getId(),
                        board.getTitle(),
                        board.getContents(),
                        board.getMeetTime(),
                        board.getLocation(),
                        board.getLatitude(),
                        board.getLongitude(),
                        board.getBoard_imgUrl(),
                        board.getReviews().stream().map(
                                review -> new ReviewResponseDto(
                                        review.getReview_imgUrl(),
                                        review.getContents()
                                )
                        ).collect(Collectors.toList())))
                .collect(Collectors.toList());
        Map<String, Object> map = new HashMap<>();
        map.put("boards", toList);
        return ResponseEntity.ok().body(map);
    }

    //메인 페이지 목록
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
                board.getLocation()
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
        String board_imgUrl = uploader.upload(boardRequestDto.getBoardImg(), "static");

        Board board = Board.builder()
                .title(boardRequestDto.getTitle())
                .meetDate(boardRequestDto.getMeetDate())
                .meetTime(boardRequestDto.getMeetTime())
                .location(boardRequestDto.getLocation())
                .contents(boardRequestDto.getContents())
                .latitude(boardRequestDto.getLatitude())
                .longitude(boardRequestDto.getLongitude())
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
        }
        board.update(boardRequestDto, board_id);
        return new ResponseEntity<>("성공적으로 수정하였습니다", HttpStatus.OK);
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
