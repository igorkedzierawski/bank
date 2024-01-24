package zet.kedzieri.bank.domain.bank.transfer;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import zet.kedzieri.bank.domain.auth.loginsession.ClientAuthDetails;

@Service
@RequiredArgsConstructor
public class TransferService {

    private final TransferRepository transferRepo;

    public Page<Transfer> getTransfersAssociatedWithCallerAccount(ClientAuthDetails caller, String accountNumber, Pageable pageable) {
        return transferRepo.findAllAssociatedWithAccountNumber(accountNumber, pageable);
    }

    public Transfer registerTransfer(
            String senderAccountNumber, String senderName,
            String receiverAccountNumber, String receiverName,
            String title, long transferAmount, long timestamp
    ) {
        return transferRepo.save(new Transfer(
                senderAccountNumber, senderName,
                receiverAccountNumber, receiverName,
                transferAmount, title, timestamp
        ));
    }

}
