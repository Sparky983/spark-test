package me.sparky983.spark;

import java.util.Objects;
import java.util.function.Function;
import java.util.function.Supplier;

public final class When<T, R> {

    private final T given;
    private final Function<T, R> when;

    public When(final T given, final Function<T, R> when) {

        this.given = given;
        this.when = Objects.requireNonNull(when, "when");
    }

    public static <R> When<Void, R> when(final Supplier<R> when) {

        return new When<>(null, (void1) -> when.get());
    }

    public Then<T, R> then(final Assertion<R> assertion) {

        Objects.requireNonNull(assertion, "assertion");
        assertion.doAssertion(() -> when.apply(given));
        return new Then<>(given, when);
    }
}
