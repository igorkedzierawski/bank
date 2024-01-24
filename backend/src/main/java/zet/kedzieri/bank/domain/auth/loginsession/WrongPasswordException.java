package zet.kedzieri.bank.domain.auth.loginsession;

import zet.kedzieri.bank.common.AcceptableToRespondException;

public class WrongPasswordException extends AcceptableToRespondException {

    public WrongPasswordException() {
        super("Niepoprawne hasło");
    }

}
