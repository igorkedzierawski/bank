package zet.kedzieri.bank.api.v1;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.WebAttributes;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import zet.kedzieri.bank.Util;
import zet.kedzieri.bank.api.v1.dto.PasswordVariantPatternDTO;
import zet.kedzieri.bank.domain.auth.loginsession.ClientAuthDetails;
import zet.kedzieri.bank.domain.auth.loginsession.LoginSession;
import zet.kedzieri.bank.domain.auth.loginsession.LoginSessionService;
import zet.kedzieri.bank.domain.client.Client;
import zet.kedzieri.bank.domain.client.ClientService;

@Controller
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthController {

    private final ClientService clientService;
    private final LoginSessionService loginSessionService;

    @GetMapping("login_error")
    public @ResponseBody String login(HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        String errorMessage = null;
        if (session != null) {
            AuthenticationException ex = (AuthenticationException) session
                    .getAttribute(WebAttributes.AUTHENTICATION_EXCEPTION);
            if (ex != null) {
                errorMessage = ex.getMessage();
            }
        }
        return "\""+errorMessage+"\"";
    }

    @GetMapping("get_pattern")
    public @ResponseBody PasswordVariantPatternDTO get_pattern(@RequestParam("login") String login) {
        Client client = clientService.getClient(login);
        LoginSession session = loginSessionService.acquireSession(client);
        return PasswordVariantPatternDTO.fromPattern(session.getPasswordVariant().getPattern());
    }

    @GetMapping("get_pattern_self")
    public @ResponseBody PasswordVariantPatternDTO get_pattern_self(@AuthenticationPrincipal ClientAuthDetails caller) {
        Client client = clientService.getClient(caller.getUsername());
        LoginSession session = loginSessionService.acquireSession(client);
        return PasswordVariantPatternDTO.fromPattern(session.getPasswordVariant().getPattern());
    }

    @GetMapping("login_success")
    public @ResponseBody String login_success() {
        return "\"Login was successfull and you may follow to your account page\"";
    }

    @GetMapping("perform_logout")
    @PostMapping("perform_logout")
    public @ResponseBody String perform_logout() {
        throw new UnsupportedOperationException("perform_logout is not bound to form login");
    }

    @GetMapping("perform_login")
    @PostMapping("perform_login")
    public @ResponseBody String perform_login() {
        throw new UnsupportedOperationException("perform_logout is not bound to form login");
    }

}
