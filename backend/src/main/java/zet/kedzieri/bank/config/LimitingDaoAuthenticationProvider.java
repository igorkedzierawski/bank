package zet.kedzieri.bank.config;

import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Component;
import zet.kedzieri.bank.Util;
import zet.kedzieri.bank.common.LoginRateLimitExceededException;
import zet.kedzieri.bank.domain.auth.loginattempt.LoginRateLimitingService;
import zet.kedzieri.bank.domain.client.Client;
import zet.kedzieri.bank.domain.client.ClientService;

@Component
public class LimitingDaoAuthenticationProvider extends DaoAuthenticationProvider {

    private final LoginRateLimitingService loginRateLimitingService;
    private final ClientService clientService;

    public LimitingDaoAuthenticationProvider(
            UserDetailsService userDetailsService,
            ServerPasswordEncoder serverPasswordEncoder,
            LoginRateLimitingService loginRateLimitingService,
            ClientService clientService
    ) {
        this.loginRateLimitingService = loginRateLimitingService;
        this.clientService = clientService;
        setUserDetailsService(userDetailsService);
        setPasswordEncoder(serverPasswordEncoder);
        setHideUserNotFoundExceptions(true);

    }

    @Override
    protected void additionalAuthenticationChecks(
            UserDetails user,
            UsernamePasswordAuthenticationToken authentication
    ) throws AuthenticationException {
        Client client = Util
                .fromFallible(() -> clientService.getClient(user.getUsername()))
                .orElseThrow(() -> new BadCredentialsException("Bad credentials"));

        String remoteAddress = Util.fromFallible(() -> {
            UserAgentAuthenticationDetailsSourceImpl.WebAuthenticationDetailsWithUserAgent details =
                    (UserAgentAuthenticationDetailsSourceImpl.WebAuthenticationDetailsWithUserAgent) authentication.getDetails();
            return details.getRemoteAddress();
        }).orElse("0.0.0.0");
        if (loginRateLimitingService.hasLoginRateLimitPerClientPerIpBeenExceeded(client, remoteAddress)) {
            throw LoginRateLimitExceededException.exceededPerIpPerClientLimit();
        }
        if (loginRateLimitingService.hasLoginRateLimitPerIpBeenExceeded(remoteAddress)) {
            throw LoginRateLimitExceededException.exceededPerIpLimit();
        }
        super.additionalAuthenticationChecks(user, authentication);
    }

}
