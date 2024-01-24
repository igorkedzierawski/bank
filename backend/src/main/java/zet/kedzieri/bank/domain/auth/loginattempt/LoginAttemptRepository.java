package zet.kedzieri.bank.domain.auth.loginattempt;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface LoginAttemptRepository extends JpaRepository<LoginAttempt, Long> {

    @Query("SELECT la FROM LoginAttempt la WHERE la.client.id = ?1")
    Page<LoginAttempt> findAllByClientId(long clientId, Pageable pageable);

    @Query("SELECT la FROM LoginAttempt la WHERE la.client.id = ?1 AND la.type = ?2")
    Page<LoginAttempt> findAllByClientIdAndType(long clientId, LoginAttempt.Type type, Pageable pageable);

    @Query("SELECT COUNT(la) FROM LoginAttempt la " +
            "WHERE la.client.id = ?1 " +
            "AND la.timestamp > ?2 " +
            "AND la.ip = ?3")
    Integer countYoungerThanTimestampAndByIpAndClient(long clientId, long timestamp, String ip);

    @Query("SELECT COUNT(la) FROM LoginAttempt la " +
            "WHERE la.timestamp > ?1 " +
            "AND la.ip = ?2")
    Integer countYoungerThanTimestampAndByIp(long timestamp, String ip);

}
