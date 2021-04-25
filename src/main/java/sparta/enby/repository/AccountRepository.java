package sparta.enby.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import sparta.enby.model.Account;

import java.util.Optional;

public interface AccountRepository extends JpaRepository<Account, Long> {

    Optional <Account> findByNickname(String nickname);
    Optional<Account> findByKakaoId(Long kakaoId);
    Optional<Account> findByEmail(String email);
}
