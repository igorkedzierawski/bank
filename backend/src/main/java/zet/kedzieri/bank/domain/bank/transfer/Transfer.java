package zet.kedzieri.bank.domain.bank.transfer;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Transfer {

    @Id
    @GeneratedValue
    private long id;

    private String senderAccountNumber;

    private String senderName;

    private String receiverAccountNumber;

    private String receiverName;

    private long transferAmount;

    private String title;

    private long timestamp;

    public Transfer(
            String senderAccountNumber, String senderName,
            String receiverAccountNumber, String receiverName,
            long transferAmount, String title, long timestamp
    ) {
        this.senderAccountNumber = senderAccountNumber;
        this.senderName = senderName;
        this.receiverAccountNumber = receiverAccountNumber;
        this.receiverName = receiverName;
        this.title = title;
        this.transferAmount = transferAmount;
        this.timestamp = timestamp;
    }

    @Override
    public String toString() {
        return "Transfer{" +
                "id=" + id +
                ", senderAccountNumber='" + senderAccountNumber + '\'' +
                ", senderName='" + senderName + '\'' +
                ", receiverAccountNumber='" + receiverAccountNumber + '\'' +
                ", receiverName='" + receiverName + '\'' +
                ", transferAmount=" + transferAmount +
                ", title='" + title + '\'' +
                ", timestamp=" + timestamp +
                '}';
    }

}
