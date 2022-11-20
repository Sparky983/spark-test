package me.sparky983.spark;

import java.util.Objects;
import java.util.function.Function;

public final class Then<T, R> {

    private final T given;
    private final Function<T, R> when;

    Then(final T given, final Function<T, R> when) {

        this.given = given;
        this.when = Objects.requireNonNull(when, "when");
    }

    public Then<T, R> and(final Assertion<R> assertion) {

        Objects.requireNonNull(assertion, "assertion");
        assertion.doAssertion(() -> when.apply(given));
        return this;
    }
}
