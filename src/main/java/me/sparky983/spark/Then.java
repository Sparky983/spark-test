package me.sparky983.spark;

import java.util.Objects;
import java.util.function.Function;

/**
 * Represents optional steps after final required stage of a test.
 *
 * @author Sparky983
 * @param <T> the type of the given object.
 * @param <R> the type of the result.
 * @since 1.0
 */
public final class Then<T, R> {

    private final T given;
    private final Function<T, R> when;

    Then(final T given, final Function<T, R> when) {

        this.given = given;
        this.when = Objects.requireNonNull(when, "when");
    }

    /**
     * Performs an additional assertion.
     *
     * @param assertion the additional assertion.
     * @return the then instance (for chaining).
     * @throws NullPointerException if the assertion is {@code null}.
     * @since 1.0
     */
    public Then<T, R> and(final Assertion<R> assertion) {

        Objects.requireNonNull(assertion, "assertion");
        assertion.doAssertion(() -> when.apply(given));
        return this;
    }
}
