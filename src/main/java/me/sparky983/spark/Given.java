package me.sparky983.spark;

import java.util.Objects;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;

/**
 * Represents given object and the start of a test.
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
     * Creates a new given with the specified given object supplier.
     * <p>
     * This is often used when setup logic is more complex, such as if you need to set up mocks.
     * <pre>{@code
     * given(() -> {
     *     mock = mock(Object.class);
     *     return new TestedObject(mock);
     * )}.when(...)
     *         .then(...)
     * }</pre>
     * Examples:
     * <pre>{@code
     * import static me.sparky983.spark.Given.given;
     *
     * given(() -> "a string")
     *         .when(...)
     *         .then(...);
     * }</pre>
     *
     * @param given the given object supplier.
     * @return the newly created given.
     * @param <T> the type of the given object.
     * @throws NullPointerException if the given object supplier is {@code null}.
     * @see #given(Object)
     * @since 1.0
     */
    public static <T> Given<T> given(final Supplier<T> given) {

        return given(given.get());
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
     * can be used instead of
     * <pre>{@code
     * import me.sparky983.spark.Given;
     *
     * Given.<String>givenNull()
     *         .when(...)
     *         .then(...);
     * }</pre>
     * <p>
     * Sometimes, we still want to use static imports, but our type is parameterized. In this case,
     * the preferred method is to use {@code given((T) null)}.
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

    /**
     * Creates an action (the when-function) that is used to perform assertions on with
     * {@link When#then(Assertion)}.
     * <p>
     * If the method returns void, you should use {@link #whenDo(Consumer)}.
     * <p>
     * Note that the when-function is not called immediately, it is only called when an assertion is
     * made.
     *
     * @param when the when-function
     * @return a new when that uses the specified when-function.
     * @param <R> the type of the result of the when-function.
     * @throws NullPointerException if the when-function is {@code null}.
     * @see #whenDo(Consumer)
     * @since 1.0
     */
    public <R> When<T, R> when(final Function<T, R> when) {

        return new When<>(given, when);
    }

    /**
     * Creates a void action (the when-function) that is used to perform assertions on.
     * <p>
     * <p>
     * Note that the when-function is not called immediately, it is only called when an assertion is
     * made.
     * Examples:
     * <pre>
     * class Car {
     *     boolean isDriving;
     *     void drive() {
     *         isDriving = true;
     *     }
     *     boolean isDriving() {
     *         return isDriving;
     *     }
     * }
     *
     * import static me.sparky983.spark.When;
     *
     * given(new Car())
     *         .whenDo(Car::drive)
     *         .then(Car::isDriving)
     * </pre>
     * @param when the when-function.
     * @return a new when that uses the specified when-function and where its result is the given
     * object.
     * @throws NullPointerException if the when-function is {@code null}.
     * @see #when(Function)
     * @since 1.0
     */
    public When<T, T> whenDo(final Consumer<T> when) {

        Objects.requireNonNull(when, "when");
        return when((t) -> {
            when.accept(t);
            return t;
        });
    }

    /**
     * Performs no operation.
     * <p>
     * This is essentially the same as {@link When#when(Supplier)}, but states the intentions more
     * accurately.
     *
     * @return a new when that uses the given object as its result.
     * @since 1.0
     */
    public When<T, T> noop() {

        return new When<>(given, (__) -> given);
    }
}
