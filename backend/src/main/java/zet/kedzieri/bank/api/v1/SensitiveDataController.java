package zet.kedzieri.bank.api.v1;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import zet.kedzieri.bank.api.v1.dto.AccountDTO;
import zet.kedzieri.bank.api.v1.dto.DirectedTransferDTO;
import zet.kedzieri.bank.api.v1.dto.StringValueDTO;
import zet.kedzieri.bank.api.v1.form.MakeTransferForm;
import zet.kedzieri.bank.domain.auth.loginsession.ClientAuthDetails;
import zet.kedzieri.bank.domain.bank.account.AccountService;
import zet.kedzieri.bank.domain.bank.transfer.Transfer;
import zet.kedzieri.bank.domain.bank.transfer.TransferService;
import zet.kedzieri.bank.domain.client.ClientService;
import zet.kedzieri.bank.domain.sensitivedata.SensitiveDataService;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@Controller
@RequestMapping("/api/v1/sensitive_data")
@RequiredArgsConstructor
public class SensitiveDataController {

    private final SensitiveDataService sensitiveDataService;

    @ResponseBody
    @GetMapping(value = "get_id_number", produces = APPLICATION_JSON_VALUE)
    public StringValueDTO getIdNumber(@AuthenticationPrincipal ClientAuthDetails caller) {
        return StringValueDTO.from(sensitiveDataService.getCallerIdNumber(caller));
    }

    @ResponseBody
    @GetMapping(value = "get_credit_card_number", produces = APPLICATION_JSON_VALUE)
    public StringValueDTO getCreditCardNumber(@AuthenticationPrincipal ClientAuthDetails caller) {
        return StringValueDTO.from(sensitiveDataService.getCallerCreditCardNumber(caller));
    }

}
