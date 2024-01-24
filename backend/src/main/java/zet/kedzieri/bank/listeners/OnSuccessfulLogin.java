package zet.kedzieri.bank.listeners;

import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationListener;
import org.springframework.security.authentication.event.AuthenticationSuccessEvent;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.WebAuthenticationDetails;
import org.springframework.stereotype.Component;
import zet.kedzieri.bank.Util;
import zet.kedzieri.bank.config.UserAgentAuthenticationDetailsSourceImpl;
import zet.kedzieri.bank.domain.auth.loginattempt.LoginAttempt;
import zet.kedzieri.bank.domain.auth.loginattempt.LoginAttemptService;
import zet.kedzieri.bank.domain.auth.loginsession.ClientAuthDetails;
import zet.kedzieri.bank.domain.auth.loginsession.LoginSessionService;
import zet.kedzieri.bank.domain.client.Client;
import zet.kedzieri.bank.domain.client.ClientService;

@Component
@RequiredArgsConstructor
public class OnSuccessfulLogin implements ApplicationListener<AuthenticationSuccessEvent> {

    private final LoginSessionService loginSessionService;
    private final LoginAttemptService loginAttemptService;
    private final ClientService clientService;

    @Override
    public void onApplicationEvent(AuthenticationSuccessEvent event) {
        Object principal = event.getAuthentication().getPrincipal();
        if(!(principal instanceof ClientAuthDetails clientAuthDetails)) {
            return;
        }
        Client client = clientService.getClient(clientAuthDetails.getClientId());

        String remoteAddress = Util.fromFallible(() -> {
            Authentication authentication = (Authentication) event.getSource();
            WebAuthenticationDetails details = (WebAuthenticationDetails) authentication.getDetails();
            return details.getRemoteAddress();
        }).orElse("0.0.0.0");
        UserAgentAuthenticationDetailsSourceImpl.UserAgentInfo userAgent = Util.fromFallible(() -> {
            Authentication authentication = (Authentication) event.getSource();
            UserAgentAuthenticationDetailsSourceImpl.WebAuthenticationDetailsWithUserAgent details =
                    (UserAgentAuthenticationDetailsSourceImpl.WebAuthenticationDetailsWithUserAgent) authentication.getDetails();
            return details.getUserAgent();
        }).orElse(new UserAgentAuthenticationDetailsSourceImpl.UserAgentInfo("unknown", "unknown"));

        loginSessionService.rotateLoginSessionPasswordVariant(client);

        loginAttemptService.registerLoginAttempt(
                client,
                userAgent.getBrowser()+"/"+userAgent.getDevice(),
                remoteAddress,
                event.getTimestamp(),
                LoginAttempt.Type.SUCCESS
        );
    }

}
