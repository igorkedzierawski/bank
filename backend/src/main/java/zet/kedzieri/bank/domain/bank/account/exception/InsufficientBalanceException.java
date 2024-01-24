package zet.kedzieri.bank.domain.bank.account.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;
import zet.kedzieri.bank.common.AcceptableToRespondException;

@Getter
@ResponseStatus(HttpStatus.PRECONDITION_FAILED)
public class InsufficientBalanceException extends AcceptableToRespondException {

    public InsufficientBalanceException() {
        super("Niewystarczające środki na koncie nadawcy");
    }

}