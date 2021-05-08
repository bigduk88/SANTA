package sparta.enby.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import sparta.enby.dto.BoardRequestDto;
import sparta.enby.dto.BoardResponseDto;
import sparta.enby.dto.ChangeDeadlineRequestDto;
import sparta.enby.security.UserDetailsImpl;
import sparta.enby.service.BoardService;

import java.io.IOException;

@RestController
@RequiredArgsConstructor
public class BoardController {

    private final BoardService boardService;
    //게시글 리스트
    @GetMapping("/main/board")
    public ResponseEntity getBoardList() {
        return boardService.getBoardList();
    }

    //게시글 페이지
    @GetMapping("/board/mating")
    public Page<BoardResponseDto> getBoard(@RequestParam("page") int page, @RequestParam("size") int size, @AuthenticationPrincipal UserDetailsImpl userDetails) {
        return boardService.getBoard(page, size, userDetails);
    }

    //게시판 상세
    @GetMapping("/board/mating/{board_id}")
    public ResponseEntity getDetailBoard(@PathVariable Long board_id, @AuthenticationPrincipal UserDetailsImpl userDetails) {
        return boardService.getDetailBoard(board_id, userDetails);
    }

    //게시글 적기
    @PostMapping("/board/mating")
    public ResponseEntity<String> writeBoard(@ModelAttribute BoardRequestDto boardRequestDto, @AuthenticationPrincipal UserDetailsImpl userDetails) throws IOException {
        return boardService.writeBoard(boardRequestDto, userDetails);
    }

    //게시글 수정
    @PutMapping("/board/mating/{board_id}")
    public ResponseEntity editBoard(@PathVariable Long board_id, @ModelAttribute BoardRequestDto boardRequestDto, @AuthenticationPrincipal UserDetailsImpl userDetails) throws IOException {
        return boardService.editBoard(board_id, boardRequestDto, userDetails);
    }

    //게시글 삭제
    @DeleteMapping("/board/mating/{board_id}")
    public ResponseEntity deleteBoard(@PathVariable Long board_id, @AuthenticationPrincipal UserDetailsImpl userDetails) {
        return boardService.deleteBoard(board_id, userDetails.getAccount());
    }

    // 마감버튼 눌러서 마감하기
    @PutMapping("/board/mating/{board_id}/deadline")
    public ResponseEntity clickFinish(@PathVariable Long board_id, @RequestBody ChangeDeadlineRequestDto changeDeadlineRequestDto, @AuthenticationPrincipal UserDetailsImpl userDetails) {
        return boardService.clickFinish(board_id, changeDeadlineRequestDto, userDetails.getAccount());
    }


}