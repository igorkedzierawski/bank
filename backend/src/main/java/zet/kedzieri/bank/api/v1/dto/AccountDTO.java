package zet.kedzieri.bank.api.v1.dto;

import zet.kedzieri.bank.domain.bank.account.Account;

public record AccountDTO(String accountNumber, long balance) {

    public static AccountDTO from(Account account) {
        return new AccountDTO(
                account.getAccountNumber(),
                account.getBalance()
        );
    }

}
