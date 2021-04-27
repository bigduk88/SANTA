package sparta.enby.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import sparta.enby.model.Review;

public interface ReviewRepository extends JpaRepository<Review, Long> {
}
