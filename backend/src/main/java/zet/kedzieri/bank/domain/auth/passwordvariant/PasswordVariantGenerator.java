package zet.kedzieri.bank.domain.auth.passwordvariant;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import zet.kedzieri.bank.config.ServerPasswordEncoder;
import zet.kedzieri.bank.domain.auth.passwordvariant.exception.WeakPasswordException;
import zet.kedzieri.bank.domain.client.Client;

import java.util.*;
import java.util.regex.Pattern;

@Component
@RequiredArgsConstructor
public class PasswordVariantGenerator {

    public static final int PASSWORD_MIN_LENGTH = 12;
    public static final int PASSWORD_MAX_LENGTH = 40;
    public static final int VARIANTS_COUNT = 3;
    public static final int CHARACTERS_PER_VARIANT = 3;

    private final ServerPasswordEncoder serverPasswordEncoder;

    public List<PasswordVariant> generateVariants(Client client, String password) {
        return generateVariants0(client, password, true);
    }

    public List<PasswordVariant> generateVariants_unchecked(Client client, String password) {
        return generateVariants0(client, password, false);
    }

    private List<PasswordVariant> generateVariants0(Client client, String password, boolean checkValidity) {
        if (checkValidity) {
            assertPasswordValidityAndStrength(password);
        }
        return generatePatternToRawVariantMap(password)
                .entrySet().parallelStream()
                .map(e -> new PasswordVariant(client,
                        e.getKey(),
                        serverPasswordEncoder.encode(e.getValue())
                ))
                .toList();
    }

    private static Map<String, String> generatePatternToRawVariantMap(String password) {
        Random random = new Random(password.hashCode());
        int length = password.length();
        Map<String, String> variantMap = new HashMap<>();
        while (variantMap.size() < VARIANTS_COUNT) {
            Set<Integer> positions = new HashSet<>();
            while (positions.size() < CHARACTERS_PER_VARIANT) {
                positions.add(random.nextInt(length));
            }
            StringBuilder patternBuilder = new StringBuilder(length);
            StringBuilder passwordBuilder = new StringBuilder(CHARACTERS_PER_VARIANT);
            for (int i = 0; i < length; i++) {
                if (positions.contains(i)) {
                    passwordBuilder.append(password.charAt(i));
                    patternBuilder.append('.');
                } else {
                    patternBuilder.append('_');
                }
            }
            variantMap.put(patternBuilder.toString(), passwordBuilder.toString());
        }
        return variantMap;
    }

    private static void assertPasswordValidityAndStrength(String password) {
        if (password.length() < PASSWORD_MIN_LENGTH) {
            throw new WeakPasswordException("Hasło musi mieć co najmniej " + PASSWORD_MIN_LENGTH + " znaków.");
        }
        if (password.length() > PASSWORD_MAX_LENGTH) {
            throw new WeakPasswordException("Hasło musi mieć co najwyżej " + PASSWORD_MAX_LENGTH + " znaków.");
        }
        if (!Pattern.compile("[A-Z]").matcher(password).find()) {
            throw new WeakPasswordException("Hasło musi zawierać co najmniej jedną wielką literę.");
        }
        if (!Pattern.compile("[a-z]").matcher(password).find()) {
            throw new WeakPasswordException("Hasło musi zawierać co najmniej jedną małą literę.");
        }
        if (!Pattern.compile("[0-9]").matcher(password).find()) {
            throw new WeakPasswordException("Hasło musi zawierać co najmniej jedną cyfrę.");
        }
        if (!Pattern.compile("[!@#$%^&*()_+\\-=\\[\\]{};':\"\\\\|,.<>/?]").matcher(password).find()) {
            throw new WeakPasswordException("Hasło musi zawierać co najmniej jeden znak specjalny.");
        }
    }

}
