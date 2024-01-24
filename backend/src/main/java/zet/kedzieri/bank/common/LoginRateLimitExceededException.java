package zet.kedzieri.bank.common;

import org.springframework.security.core.AuthenticationException;

import static zet.kedzieri.bank.domain.auth.loginattempt.LoginRateLimitingService.Config.*;

public class LoginRateLimitExceededException extends AuthenticationException {

    public LoginRateLimitExceededException(String msg) {
        super(msg);
    }

    public static LoginRateLimitExceededException exceededPerIpPerClientLimit() {
        return new LoginRateLimitExceededException(
                "Przekroczono limit logowań: Maksymalnie możesz się zalogować " + PER_CLIENT_PER_IP_MAX_LOGINS +
                        " razy w ciągu " + PER_CLIENT_PER_IP_TIME_WINDOW_MINS + " minut na to konto z tej sieci."
        );
    }

    public static LoginRateLimitExceededException exceededPerIpLimit() {
        return new LoginRateLimitExceededException(
                "Przekroczono limit logowań: Maksymalnie możesz się zalogować " + PER_IP_MAX_LOGINS +
                        " razy w ciągu " + PER_IP_TIME_WINDOW_MINS + " minut z tej sieci."
        );
    }

}
