package zet.kedzieri.bank.domain.auth.loginattempt;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import zet.kedzieri.bank.domain.client.Client;

@Service
@RequiredArgsConstructor
public class LoginAttemptService {

    private final LoginAttemptRepository loginAttemptRepo;

    public LoginAttempt registerLoginAttempt(Client client, String device, String ip, long timestamp, LoginAttempt.Type type) {
        LoginAttempt loginAttempt = new LoginAttempt(client, device, ip, timestamp, type);
        return loginAttemptRepo.save(loginAttempt);
    }

    public Page<LoginAttempt> getLoginAttempts(Client client, LoginAttempt.Type type, Pageable pageable) {
        if(type != null) {
            return loginAttemptRepo.findAllByClientIdAndType(client.getId(), type, pageable);
        } else {
            return loginAttemptRepo.findAllByClientId(client.getId(), pageable);
        }
    }

}
