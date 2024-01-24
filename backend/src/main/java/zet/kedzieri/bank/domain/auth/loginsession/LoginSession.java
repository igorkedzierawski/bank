package zet.kedzieri.bank.domain.auth.loginsession;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;
import zet.kedzieri.bank.domain.auth.passwordvariant.PasswordVariant;
import zet.kedzieri.bank.domain.client.Client;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class LoginSession {

    @Id
    @GeneratedValue
    private long id;

    @OneToOne
    @JoinColumn(nullable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Client client;

    @OneToOne
    @JoinColumn(nullable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    private PasswordVariant passwordVariant;

    public LoginSession(Client client, PasswordVariant passwordVariant) {
        this.client = client;
        this.passwordVariant = passwordVariant;
    }

    @Override
    public String toString() {
        return "LoginSession{" +
                "id=" + id +
                ", clientId=" + client.getId() +
                ", passwordVariant=" + passwordVariant +
                '}';
    }

}
