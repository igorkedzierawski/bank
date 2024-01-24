package zet.kedzieri.bank.api.v1.dto;

public record ErrorDTO(String message, String type) {

    public static ErrorDTO from(Throwable throwable) {
        return new ErrorDTO(throwable.getMessage(), throwable.getClass().getSimpleName());
    }

}
