package zet.kedzieri.bank.domain.auth.passwordvariant.exception;

import zet.kedzieri.bank.common.AcceptableToRespondException;

public class ChangePasswordTokenHasExpiredException extends AcceptableToRespondException {

    public ChangePasswordTokenHasExpiredException() {
        super("Token się przedawnił");
    }

}
