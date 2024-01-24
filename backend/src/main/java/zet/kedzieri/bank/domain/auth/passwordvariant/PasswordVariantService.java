package zet.kedzieri.bank.domain.auth.passwordvariant;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import zet.kedzieri.bank.config.ServerPasswordEncoder;
import zet.kedzieri.bank.domain.auth.changepasswordtoken.ChangePasswordToken;
import zet.kedzieri.bank.domain.auth.changepasswordtoken.ChangePasswordTokenService;
import zet.kedzieri.bank.domain.auth.loginsession.ClientAuthDetails;
import zet.kedzieri.bank.domain.auth.loginsession.LoginSession;
import zet.kedzieri.bank.domain.auth.loginsession.LoginSessionService;
import zet.kedzieri.bank.domain.auth.loginsession.WrongPasswordException;
import zet.kedzieri.bank.domain.auth.passwordvariant.exception.ChangePasswordTokenHasExpiredException;
import zet.kedzieri.bank.domain.auth.passwordvariant.exception.ChangePasswordTokenNotPresentException;
import zet.kedzieri.bank.domain.client.Client;
import zet.kedzieri.bank.domain.client.ClientService;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PasswordVariantService {

    private final PasswordVariantRepository passwordVariantRepo;

    private final ServerPasswordEncoder serverPasswordEncoder;
    private final PasswordVariantGenerator passwordVariantGenerator;

    private final ClientService clientService;
    private final LoginSessionService loginSessionService;
    private final ChangePasswordTokenService changePasswordTokenService;

    public void changePassword(ClientAuthDetails caller, String currentPassword, String newPassword) {
        Client client = clientService.getClient(caller);
        LoginSession session = loginSessionService.acquireSession(client);
        String encoded = session.getPasswordVariant().getPassword();
        if (!serverPasswordEncoder.matches(currentPassword, encoded)) {
            throw new WrongPasswordException();
        }
        upsertPasswordVariants(client, newPassword);
        loginSessionService.rotateLoginSessionPasswordVariant(client);
    }

    public void changePasswordViaChangePasswordToken(String token, String newPassword) {
        ChangePasswordToken changePasswordToken = changePasswordTokenService.findToken(token)
                .orElseThrow(ChangePasswordTokenNotPresentException::new);
        if(changePasswordToken.hasExpired()) {
            throw new ChangePasswordTokenHasExpiredException();
        }
        Client client = changePasswordToken.getClient();
        upsertPasswordVariants(client, newPassword);
        changePasswordTokenService.destroyToken(changePasswordToken);
        loginSessionService.rotateLoginSessionPasswordVariant(client);
    }

    @Transactional
    public void upsertPasswordVariants(Client client, String password) {
        List<PasswordVariant> variants = passwordVariantGenerator.generateVariants(client, password);
        passwordVariantRepo.deleteAllByClientId(client.getId());
        passwordVariantRepo.saveAll(variants);
    }

    @Transactional
    public void upsertPasswordVariants_unchecked(Client client, String password) {
        List<PasswordVariant> variants = passwordVariantGenerator.generateVariants_unchecked(client, password);
        passwordVariantRepo.deleteAllByClientId(client.getId());
        passwordVariantRepo.saveAll(variants);
    }

}
