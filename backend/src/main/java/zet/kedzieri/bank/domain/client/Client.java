package zet.kedzieri.bank.domain.client;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import zet.kedzieri.bank.domain.auth.loginsession.LoginSession;
import zet.kedzieri.bank.domain.bank.account.Account;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Client {

    @Id
    @GeneratedValue
    private long id;

    private String givenName;

    private String familyName;

    private String login;

    @Column(length = 1023)
    private String encryptedIdNumber;

    @OneToOne(mappedBy = "client", cascade = CascadeType.ALL)
    private LoginSession loginSession;

    @OneToOne(mappedBy = "client", cascade = CascadeType.ALL)
    private Account account;

    public Client(String givenName, String familyName, String login, String encryptedIdNumber) {
        this.givenName = givenName;
        this.familyName = familyName;
        this.login = login;
        this.encryptedIdNumber = encryptedIdNumber;
    }

    public String getFullName() {
        return givenName+" "+familyName;
    }

    @Override
    public String toString() {
        return "Client{" +
                "id=" + id +
                ", givenName='" + givenName + '\'' +
                ", familyName='" + familyName + '\'' +
                ", login='" + login + '\'' +
                '}';
    }

}
