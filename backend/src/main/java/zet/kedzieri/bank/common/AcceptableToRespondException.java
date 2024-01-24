package zet.kedzieri.bank.common;

/**
 * Abstrakcyjny wyjątek reprezentujący sytuację, w której odpowiedź na nieudane wykonanie zapytania:
 *    - Nie zawiera żadnych nowych wrażliwych informacji pochodzących ze strony serwera,
 *      których wysyłający zapytanie nie znał wcześniej
 *    - Zawiera takie wrażliwe informacje, ale specyfika usługi wymusza ich upublicznienie,
 *      np. przelew na nieisniejące konto wymusza odpowiedź o nieistnieniu adresata
 */
public abstract class AcceptableToRespondException extends RuntimeException {

    public AcceptableToRespondException() {
        super();
    }

    public AcceptableToRespondException(String message) {
        super(message);
    }

    public AcceptableToRespondException(String message, Throwable cause) {
        super(message, cause);
    }

    public AcceptableToRespondException(Throwable cause) {
        super(cause);
    }

    protected AcceptableToRespondException(
            String message, Throwable cause,
            boolean enableSuppression,
            boolean writableStackTrace
    ) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}