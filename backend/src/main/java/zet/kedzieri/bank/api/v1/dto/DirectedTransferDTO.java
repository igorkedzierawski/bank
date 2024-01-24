package zet.kedzieri.bank.api.v1.dto;

import zet.kedzieri.bank.domain.bank.transfer.Transfer;

import java.util.Objects;

public record DirectedTransferDTO(
        String senderAccountNumber, String senderName,
        String receiverAccountNumber, String receiverName,
        long transferAmount, String title, long timestamp,
        boolean incoming
) {

    public static DirectedTransferDTO from(Transfer transfer, String selfAccountNumber) {
        return new DirectedTransferDTO(
                transfer.getSenderAccountNumber(), transfer.getSenderName(),
                transfer.getReceiverAccountNumber(), transfer.getReceiverName(),
                transfer.getTransferAmount(), transfer.getTitle(), transfer.getTimestamp(),
                Objects.equals(transfer.getReceiverAccountNumber(), selfAccountNumber)
        );
    }

}
