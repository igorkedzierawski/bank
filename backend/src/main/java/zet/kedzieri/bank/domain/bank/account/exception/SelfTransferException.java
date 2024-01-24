package zet.kedzieri.bank.domain.bank.account.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;
import zet.kedzieri.bank.common.AcceptableToRespondException;

@Getter
@ResponseStatus(HttpStatus.BAD_REQUEST)
public class SelfTransferException extends AcceptableToRespondException {

    public SelfTransferException() {
        super("Nie można przesyłać pieniędzy samemu sobie");
    }

}