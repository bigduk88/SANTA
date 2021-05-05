package sparta.enby.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import sparta.enby.model.Board;
import sparta.enby.model.Registration;
import sparta.enby.model.Review;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

public interface BoardRepository extends JpaRepository<Board, Long> {
    List<Board> findAllById(Long board_id);
    List<Board> findAllByCreatedBy(String name);
    List<Board> findAllByRegistrations(Registration registration);
}
