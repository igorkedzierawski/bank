package zet.kedzieri.bank.domain.auth.passwordvariant.exception;

import zet.kedzieri.bank.common.AcceptableToRespondException;

public class WeakPasswordException extends AcceptableToRespondException {

    public WeakPasswordException(String msg) {
        super(msg);
    }

}
