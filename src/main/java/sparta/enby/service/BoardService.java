package sparta.enby.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import sparta.enby.dto.BoardResponseDto;
import sparta.enby.model.Board;
import sparta.enby.repository.BoardRepository;
import sparta.enby.security.UserDetailsImpl;

import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class BoardService {
    private BoardRepository boardRepository;

    public Page<BoardResponseDto> getBoard(int page, int size, UserDetailsImpl userDetails){
        PageRequest pageRequest = PageRequest.of(page -1, size, Sort.by(Sort.Direction.DESC, "createdAt"));
        Page<Board> boards = boardRepository.findAll(pageRequest);
        if (page > boards.getTotalPages()){
            return null;
        }

        if (boards.equals(null) || boards.isEmpty()) {
            Page<BoardResponseDto> toMap = boards.map(board -> new BoardResponseDto(
                    board.getId(),
                    board.getContents(),
                    board.getTitle()
            ));
            return toMap;
        }
        return null;
    }
}
