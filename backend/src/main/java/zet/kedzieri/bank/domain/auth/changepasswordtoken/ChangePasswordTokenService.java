package zet.kedzieri.bank.domain.auth.changepasswordtoken;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import zet.kedzieri.bank.domain.client.Client;
import zet.kedzieri.bank.domain.client.ClientRepository;

import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ChangePasswordTokenService {

    private final ChangePasswordTokenRepository tokenRepo;
    private final ClientRepository clientRepo;

    public static long EXPIRY_TIME_MINUTES = 10;

    public ChangePasswordToken generateByClientLogin(String login) {
        Optional<ChangePasswordToken> maybeToken =
                tokenRepo.findAll().stream().filter(token -> login.equals(token.getClient().getLogin())).findFirst();
        if(maybeToken.isPresent()) {
            return maybeToken.get();
        }
        Client client = clientRepo.findByLogin(login)
                .orElseThrow(() -> new UsernameNotFoundException("Nie znaleziono klienta o loginie " + login));
        ChangePasswordToken changePasswordToken = new ChangePasswordToken(client, UUID.randomUUID().toString(),
                System.currentTimeMillis() + EXPIRY_TIME_MINUTES * 60_000);
        return tokenRepo.save(changePasswordToken);
    }

    public Optional<ChangePasswordToken> findToken(String token) {
        return tokenRepo.findByToken(token);
    }

    @Transactional
    public void destroyToken(String token) {
        tokenRepo.findByToken(token).ifPresent(tokenRepo::delete);
    }

    @Transactional
    public void destroyToken(ChangePasswordToken token) {
        tokenRepo.delete(token);
    }

}
