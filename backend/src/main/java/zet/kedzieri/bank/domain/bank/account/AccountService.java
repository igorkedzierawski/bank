package zet.kedzieri.bank.domain.bank.account;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import zet.kedzieri.bank.domain.auth.loginsession.ClientAuthDetails;
import zet.kedzieri.bank.domain.bank.account.exception.*;
import zet.kedzieri.bank.domain.bank.transfer.Transfer;
import zet.kedzieri.bank.domain.bank.transfer.TransferService;

import java.util.Objects;

@Service
@RequiredArgsConstructor
public class AccountService {

    public final int TITLE_MAX_LENGTH = 250;

    private final AccountRepository accountRepo;

    private final TransferService transferService;

    @Transactional
    public Transfer makeTransfer(
            ClientAuthDetails caller,
            String senderAccountNumber, String receiverAccountNumber,
            long transferAmount, String title
    ) {
        if(Objects.equals(senderAccountNumber, receiverAccountNumber)) {
            throw new SelfTransferException();
        }
        if(transferAmount <= 0) {
            throw new NonPositiveTransferAmountException(transferAmount);
        }
        if(title.length() > TITLE_MAX_LENGTH) {
            throw new TransferTitleTooLongException(TITLE_MAX_LENGTH, title.length());
        }
        Account senderAccount = getAccountByAccountNumber(senderAccountNumber);
        if(caller.getClientId() != senderAccount.getClient().getId()) {
            throw new UnauthorizedTransferException();
        }
        if (senderAccount.getBalance() < transferAmount) {
            throw new InsufficientBalanceException();
        }
        Account receiverAccount = getAccountByAccountNumber(receiverAccountNumber);

        senderAccount.setBalance(senderAccount.getBalance() - transferAmount);
        receiverAccount.setBalance(receiverAccount.getBalance() + transferAmount);
        accountRepo.save(senderAccount);
        accountRepo.save(receiverAccount);
        return transferService.registerTransfer(
                senderAccountNumber, senderAccount.getClient().getFullName(),
                receiverAccountNumber, receiverAccount.getClient().getFullName(),
                title, transferAmount, System.currentTimeMillis()
        );
    }

    public Account getCallerAccount(ClientAuthDetails caller) {
        return accountRepo.findByClientId(caller.getClientId())
                .orElseThrow(ClientAccountNotFoundException::new);
    }

    public Account getAccountByAccountNumber(String accountNumber) {
        return accountRepo.findByAccountNumber(accountNumber)
                .orElseThrow(() -> new AccountNotFoundException(accountNumber));
    }

}
