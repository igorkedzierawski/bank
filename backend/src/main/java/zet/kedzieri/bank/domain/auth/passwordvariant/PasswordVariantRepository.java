package zet.kedzieri.bank.domain.auth.passwordvariant;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface PasswordVariantRepository extends JpaRepository<PasswordVariant, Long> {

    @Query("SELECT pv FROM PasswordVariant pv WHERE pv.client.id = ?1")
    List<PasswordVariant> findAllByClientId(long clientId);

    @Transactional
    @Modifying
    @Query("DELETE FROM PasswordVariant pv WHERE pv.client.id = ?1")
    void deleteAllByClientId(long clientId);

}
