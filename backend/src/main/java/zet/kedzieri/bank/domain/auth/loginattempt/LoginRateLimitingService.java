package zet.kedzieri.bank.domain.auth.loginattempt;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import zet.kedzieri.bank.domain.client.Client;

import static zet.kedzieri.bank.domain.auth.loginattempt.LoginRateLimitingService.Config.*;

@Service
@RequiredArgsConstructor
public class LoginRateLimitingService {

    private final LoginAttemptRepository loginAttemptRepo;

    public boolean hasLoginRateLimitPerClientPerIpBeenExceeded(Client client, String ip) {
        long timestamp = System.currentTimeMillis() - PER_CLIENT_PER_IP_TIME_WINDOW_MINS * 60 * 1000;
        return loginAttemptRepo.countYoungerThanTimestampAndByIpAndClient(client.getId(), timestamp, ip) >= PER_CLIENT_PER_IP_MAX_LOGINS;
    }

    public boolean hasLoginRateLimitPerIpBeenExceeded(String ip) {
        long timestamp = System.currentTimeMillis() - PER_IP_TIME_WINDOW_MINS * 60 * 1000;
        return loginAttemptRepo.countYoungerThanTimestampAndByIp(timestamp, ip) >= PER_IP_MAX_LOGINS;
    }

    public static class Config {

        public static final int PER_CLIENT_PER_IP_MAX_LOGINS = 15;
        public static final int PER_CLIENT_PER_IP_TIME_WINDOW_MINS = 1;

        public static final int PER_IP_MAX_LOGINS = 15;
        public static final int PER_IP_TIME_WINDOW_MINS = 1;

    }

}
