package zet.kedzieri.bank.domain.bank.transfer;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface TransferRepository extends JpaRepository<Transfer, Long> {

    @Query("SELECT t FROM Transfer t WHERE t.senderAccountNumber = ?1 OR t.receiverAccountNumber = ?1")
    Page<Transfer> findAllAssociatedWithAccountNumber(String accountNumber, Pageable pageable);

}
