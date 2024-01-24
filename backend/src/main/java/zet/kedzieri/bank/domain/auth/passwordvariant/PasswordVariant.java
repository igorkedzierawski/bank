package zet.kedzieri.bank.domain.auth.passwordvariant;

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
public class PasswordVariant {

    @Id
    @GeneratedValue
    private long id;

    @ManyToOne
    @JoinColumn(nullable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Client client;

    private String pattern;

    private String password;

    public PasswordVariant(Client client, String pattern, String password) {
        this.client = client;
        this.pattern = pattern;
        this.password = password;
    }

    @Override
    public String toString() {
        return "PasswordVariant{" +
                "id=" + id +
                ", clientId=" + client.getId() +
                ", pattern='" + pattern + '\'' +
                ", password='" + password + '\'' +
                '}';
    }

}
