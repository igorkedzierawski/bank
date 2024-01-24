package zet.kedzieri.bank.listeners;

import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationListener;
import org.springframework.security.authentication.event.AbstractAuthenticationFailureEvent;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;
import zet.kedzieri.bank.Util;
import zet.kedzieri.bank.config.UserAgentAuthenticationDetailsSourceImpl;
import zet.kedzieri.bank.domain.auth.loginattempt.LoginAttempt;
import zet.kedzieri.bank.domain.auth.loginattempt.LoginAttemptService;
import zet.kedzieri.bank.domain.auth.loginsession.ClientAuthDetails;
import zet.kedzieri.bank.domain.client.Client;
import zet.kedzieri.bank.domain.client.ClientService;

@Component
@RequiredArgsConstructor
public class OnUnsuccessfulLogin implements ApplicationListener<AbstractAuthenticationFailureEvent> {

    private final LoginAttemptService loginAttemptService;
    private final ClientService clientService;

    @Override
    public void onApplicationEvent(AbstractAuthenticationFailureEvent event) {
        Object principal = event.getAuthentication().getPrincipal();
        Client client;
        if(principal instanceof ClientAuthDetails clientAuthDetails) {
            client = clientService.getClient(clientAuthDetails.getClientId());
        } else if(principal instanceof String login) {
            client = clientService.getClient(login);
        } else {
            return;
        }

        String remoteAddress = Util.fromFallible(() -> {
            Authentication authentication = (Authentication) event.getSource();
            UserAgentAuthenticationDetailsSourceImpl.WebAuthenticationDetailsWithUserAgent details =
                    (UserAgentAuthenticationDetailsSourceImpl.WebAuthenticationDetailsWithUserAgent) authentication.getDetails();
            return details.getRemoteAddress();
        }).orElse("0.0.0.0");
        UserAgentAuthenticationDetailsSourceImpl.UserAgentInfo userAgent = Util.fromFallible(() -> {
            Authentication authentication = (Authentication) event.getSource();
            UserAgentAuthenticationDetailsSourceImpl.WebAuthenticationDetailsWithUserAgent details =
                    (UserAgentAuthenticationDetailsSourceImpl.WebAuthenticationDetailsWithUserAgent) authentication.getDetails();
            return details.getUserAgent();
        }).orElse(new UserAgentAuthenticationDetailsSourceImpl.UserAgentInfo("unknown", "unknown"));

        loginAttemptService.registerLoginAttempt(
                client,
                userAgent.getBrowser()+"/"+userAgent.getDevice(),
                remoteAddress,
                event.getTimestamp(),
                LoginAttempt.Type.FAILURE
        );
    }

}
