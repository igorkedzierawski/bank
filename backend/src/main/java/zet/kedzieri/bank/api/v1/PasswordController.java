package zet.kedzieri.bank.api.v1;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import zet.kedzieri.bank.api.v1.form.ChangePasswordEmailForm;
import zet.kedzieri.bank.api.v1.form.ChangePasswordForm;
import zet.kedzieri.bank.domain.auth.changepasswordtoken.ChangePasswordToken;
import zet.kedzieri.bank.domain.auth.changepasswordtoken.ChangePasswordTokenService;
import zet.kedzieri.bank.domain.auth.loginsession.ClientAuthDetails;
import zet.kedzieri.bank.domain.auth.passwordvariant.PasswordVariantService;

import static org.springframework.http.MediaType.ALL_VALUE;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@Controller
@RequestMapping("/api/v1/password")
@RequiredArgsConstructor
public class PasswordController {

    private final PasswordVariantService passwordVariantService;
    private final ChangePasswordTokenService changePasswordTokenService;

    @ResponseBody
    @PostMapping(value = "change_password", produces = APPLICATION_JSON_VALUE, consumes = APPLICATION_JSON_VALUE)
    public String changePassword(
            @AuthenticationPrincipal ClientAuthDetails caller,
            @RequestBody ChangePasswordForm form
    ) {
        passwordVariantService.changePassword(caller, form.currentPasswordVariant(), form.newPasswordFull());
        return "{}";
    }

    @ResponseBody
    @PostMapping(value = "change_password_email", produces = APPLICATION_JSON_VALUE, consumes = APPLICATION_JSON_VALUE)
    public String changePasswordEmail(
            @AuthenticationPrincipal ClientAuthDetails caller,
            @RequestBody ChangePasswordEmailForm form
    ) {
        passwordVariantService.changePasswordViaChangePasswordToken(form.token(), form.newPasswordFull());
        return "{}";
    }

    @ResponseBody
    @GetMapping(value = "change_password_email_generate", produces = APPLICATION_JSON_VALUE, consumes =
            ALL_VALUE)
    public String changePasswordEmailGenerate(
            @RequestParam("login") String login
    ) {
        ChangePasswordToken changePasswordToken = changePasswordTokenService.generateByClientLogin(login);
        System.out.println("Wysłałbym na maila użytkownikowi link: localhost/change_password?token="+changePasswordToken.getToken());
        System.out.println("Wysłałbym na maila użytkownikowi link: localhost/change_password?token="+changePasswordToken.getToken());
        System.out.println("Wysłałbym na maila użytkownikowi link: localhost/change_password?token="+changePasswordToken.getToken());
        System.out.println("Wysłałbym na maila użytkownikowi link: localhost/change_password?token="+changePasswordToken.getToken());
        return "{}";
    }

}
