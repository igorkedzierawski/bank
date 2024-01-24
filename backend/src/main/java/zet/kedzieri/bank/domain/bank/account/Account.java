package zet.kedzieri.bank.domain.bank.account;

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
public class Account {

    @Id
    @GeneratedValue
    private long id;

    @OneToOne
    @JoinColumn(nullable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Client client;

    private String accountNumber;

    private long balance;

    @Column(length = 1023)
    private String encryptedCreditCardNumber;

    public Account(Client client, String accountNumber, long balance, String encryptedCreditCardNumber) {
        this.client = client;
        this.accountNumber = accountNumber;
        this.balance = balance;
        this.encryptedCreditCardNumber = encryptedCreditCardNumber;
    }

    @Override
    public String toString() {
        return "Account{" +
                "id=" + id +
                ", clientId=" + client.getId() +
                ", accountNumber=" + accountNumber +
                ", balance=" + balance +
                '}';
    }

}
