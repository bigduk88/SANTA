package sparta.enby.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.jpa.repository.JpaRepository;
import sparta.enby.model.Board;
import sparta.enby.model.Review;

import java.util.List;
import java.util.Optional;

public interface BoardRepository extends JpaRepository<Board, Long> {
    List<Board> findAllById(Long board_id);
    Board findByReviews(Review review);
}
