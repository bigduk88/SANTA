package sparta.enby.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import sparta.enby.model.Registration;

public interface RegistrationRepository extends JpaRepository<Registration, Long> {
}
