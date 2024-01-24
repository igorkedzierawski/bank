package zet.kedzieri.bank;

import java.util.Optional;

public class Util {

    public static <T> Optional<T> fromFallible(FallibleSupplier<T> fallible) {
        try {
            return Optional.ofNullable(fallible.get());
        } catch (Throwable ignored) {
            return Optional.empty();
        }
    }

    public interface FallibleSupplier<T> {
        T get() throws Throwable;
    }

}
