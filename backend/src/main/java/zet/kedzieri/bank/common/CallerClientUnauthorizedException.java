package zet.kedzieri.bank.common;

import lombok.Getter;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@Getter
@ResponseStatus(HttpStatus.FORBIDDEN)
public class CallerClientUnauthorizedException extends RuntimeException {

    private final long callerClientId;

    public CallerClientUnauthorizedException(long callerClientId) {
        this.callerClientId = callerClientId;
    }

    public CallerClientUnauthorizedException(long callerClientId, String message) {
        super(message);
        this.callerClientId = callerClientId;
    }

}