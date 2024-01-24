package zet.kedzieri.bank.domain.sensitivedata;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import zet.kedzieri.bank.config.SensitiveDataCipher;
import zet.kedzieri.bank.domain.auth.loginsession.ClientAuthDetails;
import zet.kedzieri.bank.domain.bank.account.AccountService;
import zet.kedzieri.bank.domain.client.Client;
import zet.kedzieri.bank.domain.client.ClientService;

@Service
@RequiredArgsConstructor
public class SensitiveDataService {

    private final ClientService clientService;
    private final AccountService accountService;
    private final SensitiveDataCipher sensitiveDataCipher;

    public String getCallerIdNumber(ClientAuthDetails caller) {
        return sensitiveDataCipher.decrypt(clientService.getClient(caller).getEncryptedIdNumber());
    }

    public String getCallerCreditCardNumber(ClientAuthDetails caller) {
        return sensitiveDataCipher.decrypt(accountService.getCallerAccount(caller).getEncryptedCreditCardNumber());
    }

}
