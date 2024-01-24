package zet.kedzieri.bank.domain.auth.passwordvariant.exception;

import zet.kedzieri.bank.common.AcceptableToRespondException;

public class ChangePasswordTokenNotPresentException extends AcceptableToRespondException {

    public ChangePasswordTokenNotPresentException() {
        super("Taki token nie istnieje");
    }

}
