package me.sparky983.spark;

import java.util.Objects;
import java.util.function.Function;
import java.util.function.Supplier;

/**
 * The final stage of a test where an assertion is made.
 *
 * @author Sparky983
 * @param <T> the type of the given object.
 * @param <R> the type of the result.
 * @since 1.0
 */
public final class When<T, R> {

    private final T given;
    private final Function<T, R> when;

    When(final T given, final Function<T, R> when) {

        this.given = given;
        this.when = Objects.requireNonNull(when, "when");
    }

    /**
     * Creates a new when without a given object that uses the specified action (when-function) to
     * perform assertions on with {@link #then(Assertion)}.
     *
     * @param when then when-function.
     * @return a new when that uses the specified when-function.
     * @param <R> the type of the result.
     * @throws NullPointerException if the when-function is {@code null}.
     * @since 1.0
     */
    public static <R> When<Void, R> when(final Supplier<R> when) {

        Objects.requireNonNull(when, "when");
        return new When<>(null, (void1) -> when.get());
    }

    /**
     * Creates a new then after performing the specified assertion with
     * {@link Assertion#doAssertion(Supplier)} where the when-argument is the when-function.
     *
     * @param assertion the assertion.
     * @return a new then that can be used to perform additional assertions via
     * {@link Then#and(Assertion)}.
     * @throws NullPointerException if the assertion is {@code null}.
     * @since 1.0
     */
    public Then<T, R> then(final Assertion<R> assertion) {

        Objects.requireNonNull(assertion, "assertion");
        assertion.doAssertion(() -> when.apply(given));
        return new Then<>(given, when);
    }
}
