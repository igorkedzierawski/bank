package zet.kedzieri.bank.domain.auth.changepasswordtoken;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ChangePasswordTokenRepository extends JpaRepository<ChangePasswordToken, Long> {

    @Query("SELECT cpt FROM ChangePasswordToken cpt WHERE cpt.token = ?1")
    Optional<ChangePasswordToken> findByToken(String token);

}
