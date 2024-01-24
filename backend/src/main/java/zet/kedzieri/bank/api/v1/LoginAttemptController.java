package zet.kedzieri.bank.api.v1;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import zet.kedzieri.bank.Util;
import zet.kedzieri.bank.api.v1.dto.LoginAttemptDTO;
import zet.kedzieri.bank.domain.auth.loginattempt.LoginAttempt;
import zet.kedzieri.bank.domain.auth.loginattempt.LoginAttemptService;
import zet.kedzieri.bank.domain.auth.loginsession.ClientAuthDetails;
import zet.kedzieri.bank.domain.client.ClientService;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@Controller
@RequestMapping("/api/v1/login_attempt")
@RequiredArgsConstructor
public class LoginAttemptController {

    private final LoginAttemptService loginAttemptService;
    private final ClientService clientService;

    @ResponseBody
    @GetMapping(value = "page", produces = APPLICATION_JSON_VALUE)
    public Page<LoginAttemptDTO> page(
            @AuthenticationPrincipal ClientAuthDetails details,
            @RequestParam(value = "page", defaultValue = "0") int page,
            @RequestParam(value = "size", defaultValue = "10") int size,
            @RequestParam(value = "type", defaultValue = "") String type
    ) {
        if (size <= 0) {
            return Page.empty();
        }
        LoginAttempt.Type attemptType = Util.fromFallible(() ->
                LoginAttempt.Type.valueOf(type.trim().toUpperCase())
        ).orElse(null);
        return loginAttemptService
                .getLoginAttempts(clientService.getClient(details), attemptType, PageRequest.of(page, size))
                .map(LoginAttemptDTO::from);
    }

}
