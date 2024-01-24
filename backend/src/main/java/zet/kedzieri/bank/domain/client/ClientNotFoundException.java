package zet.kedzieri.bank.domain.client;

import zet.kedzieri.bank.common.AcceptableToRespondException;

public class ClientNotFoundException extends AcceptableToRespondException {

    public ClientNotFoundException(long clientId) {
        super("Nie znaleziono klienta o id "+clientId);
    }

    public ClientNotFoundException(String login) {
        super("Nie znaleziono klienta o loginie "+login);
    }

}
