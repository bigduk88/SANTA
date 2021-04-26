package sparta.enby.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import sparta.enby.model.Board;

import java.util.Optional;

public interface BoardRepository extends JpaRepository<Board, Long> {
}
