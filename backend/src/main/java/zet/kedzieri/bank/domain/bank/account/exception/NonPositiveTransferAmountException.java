package zet.kedzieri.bank.domain.bank.account.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;
import zet.kedzieri.bank.common.AcceptableToRespondException;

@Getter
@ResponseStatus(HttpStatus.BAD_REQUEST)
public class NonPositiveTransferAmountException extends AcceptableToRespondException {

    public NonPositiveTransferAmountException(long transferAmount) {
        super(formatMessage(transferAmount));
    }

    private static String formatMessage(long transferAmount) {
        String formatted = "00"+transferAmount;
        int index = formatted.length() - 2;
        formatted = formatted.substring(0, index)+'.'+formatted.substring(index);
        return "Nie można przelać " + formatted + ". Kwota musi być większa niż 0.00";
    }

}