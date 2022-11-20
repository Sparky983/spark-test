package me.sparky983.spark;

import java.util.Objects;
import java.util.function.Consumer;
import java.util.function.Function;

/**
 * A given object.
 * <p>
 * In BDD, the given is state of a system before it has been acted upon.
 * <p>
 * After the system has been set up, you may act upon it using {@link #when(Function)}, or
 * {@link #whenDo(Consumer)} (if the action returns void).
 *
 * @author Sparky983
 * @param <T> The type of the given object.
 * @since 1.0
 */
public final class Given<T> {

    private final T given;

    private Given(final T given) {

        this.given = given;
    }

    /**
     * Creates a new given with the specified given object.
     * <p>
     * This method is often statically-imported to make a test more human-readable.
     * <p>
     * This method is often statically-imported to make a test more human-readable.
     * <p>
     * Example:
     * <pre>
     * import static me.sparky983.spark.Given.given;
     *
     * given("a string")
     *         .when(...)
     *         .then(...);
     * </pre>
     * as opposed to
     * <pre>
     * import me.sparky983.spark.Given;
     *
     * Given.given("a string")
     *         .when(...)
     *         .then(...);
     * </pre>
     *
     * @param given the given object.
     * @return the newly created given.
     * @param <T> the type of the given object.
     * @see #givenNull()
     * @see #givenNull(Class)
     * @since 1.0
     */
    public static <T> Given<T> given(final T given) {

        return new Given<>(given);
    }

    /**
     * Creates a new given of {@code null}.
     * <p>
     * In certain circumstances (when the {@code T} cannot be inferred), it is better to use
     * {@link #givenNull(Class)}, or {@link #given(Object) given((T) null)} rather than the
     * lengthier {@code Given.<T>givenNull()}.
     * <p>
     * This method is often statically-imported to make a test more human-readable.
     * <p>
     * Example:
     * <pre>
     * import static me.sparky983.spark.Given.givenNull;
     *
     * givenNull()
     *         .when(...)
     *         .then(...);
     * </pre>
     * as opposed to
     * <pre>
     * import me.sparky983.spark.Given;
     *
     * Given.givenNull()
     *         .when(...)
     *         .then(...);
     * </pre>
     *
     * @return the newly created given.
     * @param <T> the type of the given object.
     * @see #given(Object)
     * @see #givenNull(Class)
     * @since 1.0
     */
    public static <T> Given<T> givenNull() {

        return new Given<>(null);
    }

    /**
     * Creates a new given of {@code null}.
     * <p>
     * This method is an alternative to {@link #givenNull()}, which lets you avoid doing the
     * standard (non-static method) call.
     * <p>
     * Example:
     * <pre>
     * import static me.sparky983.spark.Given.givenNull;
     *
     * givenNull(String.class)
     *         .when(...)
     *         .then(...);
     * </pre>
     * as opposed to
     * <pre>{@code
     * import me.sparky983.spark.Given;
     *
     * Given.<String>givenNull()
     *         .when(...)
     *         .then(...);
     * }</pre>
     *
     * @param cls the type of the given object.
     * @return the newly created given.
     * @param <T> the type of the given object.
     * @throws NullPointerException if the type is {@code null}.
     * @see #given(Object)
     * @see #givenNull()
     * @since 1.0
     */
    public static <T> Given<T> givenNull(final Class<T> cls) {

        Objects.requireNonNull(cls, "cls");
        return new Given<>(null);
    }

    public <R> When<T, R> when(final Function<T, R> when) {

        return new When<>(given, when);
    }

    public When<T, Void> whenDo(final Consumer<T> when) {

        Objects.requireNonNull(when, "when");
        return when((t) -> {
            when.accept(t);
            return null;
        });
    }

    public When<T, T> noop() {

        return new When<>(given, (__) -> given);
    }
}
