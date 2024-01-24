package zet.kedzieri.bank.api.v1.form;

public record ChangePasswordEmailForm(String token, String newPasswordFull) {
}
