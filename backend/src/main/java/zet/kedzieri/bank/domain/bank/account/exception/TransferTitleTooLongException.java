package zet.kedzieri.bank.domain.bank.account.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;
import zet.kedzieri.bank.common.AcceptableToRespondException;

@Getter
@ResponseStatus(HttpStatus.BAD_REQUEST)
public class TransferTitleTooLongException extends AcceptableToRespondException {

    public TransferTitleTooLongException(int maxSize, int actualSize) {
        super("Długość tytułu przelewu "+actualSize+" przekroczyła maksymalną długość "+maxSize+" znaków");
    }

}