package zet.kedzieri.bank.domain.auth.changepasswordtoken;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;
import zet.kedzieri.bank.domain.client.Client;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class ChangePasswordToken {

    @Id
    @GeneratedValue
    private long id;

    @OneToOne
    @JoinColumn(nullable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Client client;

    private String token;

    private long expiryTimestamp;

    public ChangePasswordToken(
            Client client,
            String token, long expiryTimestamp
    ) {
        this.client = client;
        this.token = token;
        this.expiryTimestamp = expiryTimestamp;
    }

    public boolean hasExpired() {
        return getExpiryTimestamp() < System.currentTimeMillis();
    }

    @Override
    public String toString() {
        return "Transfer{" +
                "id=" + id +
                ", token='" + token + '\'' +
                ", expiryTimestamp='" + expiryTimestamp + '\'' +
                '}';
    }

}
