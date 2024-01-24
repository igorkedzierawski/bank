package zet.kedzieri.bank.domain.bank.account.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;
import zet.kedzieri.bank.common.AcceptableToRespondException;

@Getter
@ResponseStatus(HttpStatus.FORBIDDEN)
public class UnauthorizedTransferException extends AcceptableToRespondException {

    public UnauthorizedTransferException() {
        super("Aby zrealizować przelew, klient musi być właścicielem konta nadawcy");
    }

}