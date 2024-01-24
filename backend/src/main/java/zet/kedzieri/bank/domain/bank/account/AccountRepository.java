package zet.kedzieri.bank.domain.bank.account;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AccountRepository extends JpaRepository<Account, Long> {

    Optional<Account> findByAccountNumber(String accountNumber);

    @Query("SELECT a FROM Account a WHERE a.client.id = ?1")
    Optional<Account> findByClientId(long clientId);

}
