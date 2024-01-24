package zet.kedzieri.bank.domain.auth.loginsession;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface LoginSessionRepository extends JpaRepository<LoginSession, Long> {

    @Query("SELECT ls FROM LoginSession ls WHERE ls.client.id = ?1")
    Optional<LoginSession> findByClientId(long clientId);

}
