package zet.kedzieri.bank.domain.bank.account.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;
import zet.kedzieri.bank.common.AcceptableToRespondException;

@Getter
@ResponseStatus(HttpStatus.NOT_FOUND)
public class ClientAccountNotFoundException extends AcceptableToRespondException {

    public ClientAccountNotFoundException() {
        super("Nie przydzielono konta bankowego do Twojego konta klienckiego");
    }

}