package zet.kedzieri.bank.api.v1.dto;

public record PasswordVariantPatternDTO(String message) {

    public static PasswordVariantPatternDTO fromPattern(String pattern) {
        StringBuilder builder = new StringBuilder("Wpisz podane litery hasła: ");
        for (int i = 0; i < pattern.length(); i++) {
            if(pattern.charAt(i) == '.') {
                builder.append(i+1).append(", ");
            }
        }
        builder.setLength(builder.length()-2);
        return new PasswordVariantPatternDTO(builder.toString());
    }

}
