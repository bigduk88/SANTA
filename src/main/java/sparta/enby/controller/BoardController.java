package sparta.enby.controller;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import sparta.enby.dto.BoardResponseDto;
import sparta.enby.security.UserDetailsImpl;
import sparta.enby.service.BoardService;

@RestController
@RequiredArgsConstructor
public class BoardController {

    private final BoardService boardService;

    @GetMapping("/board/mating")
    public Page<BoardResponseDto> getBoard(@RequestParam("page") int page, @RequestParam("size") int size, @AuthenticationPrincipal UserDetailsImpl userDetails){
        return boardService.getBoard(page, size, userDetails);

    }
}
