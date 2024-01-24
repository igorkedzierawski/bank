package zet.kedzieri.bank.api.v1.form;

public record MakeTransferForm(
        String senderAccountNumber, String receiverAccountNumber,
        String title, long transferAmount
) {
}
