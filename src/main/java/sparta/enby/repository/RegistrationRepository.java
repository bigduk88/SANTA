package sparta.enby.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import sparta.enby.model.Board;
import sparta.enby.model.Registration;

import java.util.Optional;

public interface RegistrationRepository extends JpaRepository<Registration, Long> {
    void deleteAllByBoard(Board board);
    Optional<Registration> findByAccount_KakaoId(Long id);
    boolean existsByBoard(Board board);
}
