package zet.kedzieri.bank.domain.client;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import zet.kedzieri.bank.domain.auth.loginsession.ClientAuthDetails;

@Service
@RequiredArgsConstructor
public class ClientService {

    private final ClientRepository clientRepo;

    public Client getClient(long clientId) {
        return clientRepo.findById(clientId)
                .orElseThrow(() -> new ClientNotFoundException(clientId));
    }

    public Client getClient(String login) {
        return clientRepo.findByLogin(login)
                .orElseThrow(() -> new ClientNotFoundException(login));
    }

    public Client getClient(ClientAuthDetails details) {
        return getClient(details.getClientId());
    }

}
