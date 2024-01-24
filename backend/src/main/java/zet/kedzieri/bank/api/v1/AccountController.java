package zet.kedzieri.bank.api.v1;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import zet.kedzieri.bank.api.v1.dto.AccountDTO;
import zet.kedzieri.bank.api.v1.dto.DirectedTransferDTO;
import zet.kedzieri.bank.api.v1.form.MakeTransferForm;
import zet.kedzieri.bank.domain.auth.loginsession.ClientAuthDetails;
import zet.kedzieri.bank.domain.bank.account.AccountService;
import zet.kedzieri.bank.domain.bank.transfer.Transfer;
import zet.kedzieri.bank.domain.bank.transfer.TransferService;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@Controller
@RequestMapping("/api/v1/account")
@RequiredArgsConstructor
public class AccountController {

    private final AccountService accountService;
    private final TransferService transferService;

    @ResponseBody
    @PostMapping(value = "make_transfer", produces = APPLICATION_JSON_VALUE, consumes = APPLICATION_JSON_VALUE)
    public Transfer makeTransfer(
            @AuthenticationPrincipal ClientAuthDetails caller,
            @RequestBody MakeTransferForm form
    ) {
        return accountService.makeTransfer(
                caller,
                form.senderAccountNumber(), form.receiverAccountNumber(),
                form.transferAmount(), form.title()
        );
    }

    @ResponseBody
    @GetMapping(value = "info", produces = APPLICATION_JSON_VALUE)
    public AccountDTO accountInfo(@AuthenticationPrincipal ClientAuthDetails caller) {
        return AccountDTO.from(accountService.getCallerAccount(caller));
    }

    @ResponseBody
    @GetMapping(value = "transfers_page", produces = APPLICATION_JSON_VALUE)
    public Page<DirectedTransferDTO> transfersPage(
            @AuthenticationPrincipal ClientAuthDetails caller,
            @RequestParam(value = "page", defaultValue = "0") int page,
            @RequestParam(value = "size", defaultValue = "10") int size
    ) {
        if (size <= 0) {
            return Page.empty();
        }
        String accountNumber = accountService.getCallerAccount(caller).getAccountNumber();
        return transferService
                .getTransfersAssociatedWithCallerAccount(caller, accountNumber, PageRequest.of(page, size))
                .map(t -> DirectedTransferDTO.from(t, accountNumber));
    }

}
