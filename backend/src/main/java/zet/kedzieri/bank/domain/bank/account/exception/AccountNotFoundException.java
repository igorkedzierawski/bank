package zet.kedzieri.bank.domain.bank.account.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;
import zet.kedzieri.bank.common.AcceptableToRespondException;

@Getter
@ResponseStatus(HttpStatus.NOT_FOUND)
public class AccountNotFoundException extends AcceptableToRespondException {

    public AccountNotFoundException(String accountNumber) {
        super("Nie znaleziono konta bankowego o numerze "+accountNumber);
    }

}