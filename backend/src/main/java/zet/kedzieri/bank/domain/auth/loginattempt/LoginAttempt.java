package zet.kedzieri.bank.domain.auth.loginattempt;

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
public class LoginAttempt {

    @Id
    @GeneratedValue
    private long id;

    @ManyToOne
    @JoinColumn(nullable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Client client;

    private String device;
    private String ip;

    private long timestamp;

    private Type type;

    public LoginAttempt(Client client, String device, String ip, long timestamp, Type type) {
        this.client = client;
        this.device = device;
        this.ip = ip;
        this.timestamp = timestamp;
        this.type = type;
    }

    @Override
    public String toString() {
        return "LoginAttempt{" +
                "id=" + id +
                ", clientId=" + client.getId() +
                ", device='" + device + '\'' +
                ", ip='" + ip + '\'' +
                ", timestamp=" + timestamp +
                ", type=" + type +
                '}';
    }

    public enum Type {
        SUCCESS, FAILURE;
    }

}
