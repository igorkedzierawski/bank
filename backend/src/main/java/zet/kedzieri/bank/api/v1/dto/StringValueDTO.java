package zet.kedzieri.bank.api.v1.dto;

public record StringValueDTO(String value) {

    public static StringValueDTO from(String value) {
        return new StringValueDTO(value);
    }

}
