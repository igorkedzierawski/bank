package zet.kedzieri.bank.api.v1.dto;

import zet.kedzieri.bank.domain.auth.loginattempt.LoginAttempt;

public record LoginAttemptDTO(long id, String device, String ip, long timestamp, LoginAttempt.Type type) {

    public static LoginAttemptDTO from(LoginAttempt attempt) {
        return new LoginAttemptDTO(
                attempt.getId(),
                attempt.getDevice(),
                attempt.getIp(),
                attempt.getTimestamp(),
                attempt.getType()
        );
    }

}
