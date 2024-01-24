package zet.kedzieri.bank.domain.auth.loginsession;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import zet.kedzieri.bank.domain.auth.passwordvariant.PasswordVariant;
import zet.kedzieri.bank.domain.auth.passwordvariant.PasswordVariantRepository;
import zet.kedzieri.bank.domain.client.Client;
import zet.kedzieri.bank.domain.client.ClientNotFoundException;
import zet.kedzieri.bank.domain.client.ClientService;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.ThreadLocalRandom;

@Service
@RequiredArgsConstructor
public class LoginSessionService implements UserDetailsService {

    private final LoginSessionRepository loginSessionRepo;
    private final PasswordVariantRepository passwordVariantRepo;

    private final ClientService clientService;

    @Override
    public UserDetails loadUserByUsername(String login) {
        try {
            Client client = clientService.getClient(login);
            LoginSession session = acquireSession(client);
            return new ClientAuthDetails(client.getId(), client.getLogin(), session.getPasswordVariant().getPassword());
        } catch (ClientNotFoundException e) {
            throw new UsernameNotFoundException(e.getMessage());
        }
    }

    public LoginSession acquireSession(Client client) {
        return getSession(client).orElse(rotateLoginSessionPasswordVariant(client));
    }

    public Optional<LoginSession> getSession(Client client) {
        return loginSessionRepo.findByClientId(client.getId());
    }

    public LoginSession rotateLoginSessionPasswordVariant(Client client) {
        List<PasswordVariant> passwordVariants = passwordVariantRepo.findAllByClientId(client.getId());
        if (passwordVariants.isEmpty()) {
            throw new IllegalStateException("client has no password set " + client.getId());
        }
        int randomIndex = ThreadLocalRandom.current().nextInt(passwordVariants.size());
        PasswordVariant password = passwordVariants.get(randomIndex);
        LoginSession session = loginSessionRepo.findByClientId(client.getId()).orElse(null);
        if(session == null) {
            session = new LoginSession(client, password);
        } else {
            session.setPasswordVariant(password);
        }
        return loginSessionRepo.save(session);
    }

}
