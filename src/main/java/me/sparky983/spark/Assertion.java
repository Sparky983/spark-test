package me.sparky983.spark;

import java.util.Objects;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.regex.Pattern;

/**
 * Represents an assertion.
 *
 * @param <T> the type of the object where the assertion is being made on.
 */
@FunctionalInterface
public interface Assertion<T> {

    /**
     * Performs the assertion on the result.
     *
     * @param when a supplier that supplies the result.
     * <p>
     * Callers should note that the supplier may throw exceptions.
     * <p>
     * Callers should also be careful about supplying a {@code null} supplier, because
     * implementations are not required to handle {@code null} suppliers.
     * @throws AssertionError if the assertion failed.
     * @since 1.0
     */
    void doAssertion(Supplier<T> when);

    /**
     * Creates a new assertion that succeeds if the specified assertion fails, otherwise it
     * succeeds.
     *
     * @param assertion the assertion.
     * @return the new assertion.
     * @param <T> the type of the result.
     * @throws NullPointerException if the assertion is {@code null}.
     * @since 1.0
     */
    static <T> Assertion<T> not(final Assertion<T> assertion) {

        Objects.requireNonNull(assertion, "assertion");
        return (inputSupplier) -> {
            try {
                assertion.doAssertion(inputSupplier);
            } catch (final AssertionError error) {
                return;
            }
            throw new AssertionError("Expected `" + assertion + "` to fail");
        };
    }

    /*
    General validators
     */

    /**
     * Creates a new assertion that succeeds if the supplier returns an instance of the specified
     * class, otherwise fails.
     *
     * @param cls the class.
     * @return the new assertion.
     * @param <T> the type of the result.
     * @throws NullPointerException if the class is {@code null}.
     * @since 1.0
     */
    static <T> Assertion<T> isInstanceOf(final Class<?> cls) {

        Objects.requireNonNull(cls, "cls");
        return (inputSupplier) -> {
            final Object input = inputSupplier.get();
            if (!cls.isInstance(input)) {
                throw new AssertionError("Input must be of instance " + cls.getName() + ", was "
                        + input.getClass().getName());
            }
        };
    }

    /**
     * Creates a new assertion that fails if applying the method reference on the result is not
     * equal to the other object.
     * <p>
     * Equality is defined by the {@code Objects.equals(o, result}.
     *
     * @param methodReference the method reference.
     * @param o the other object.
     * @return the new assertion.
     * @param <T> the type of the result.
     * @throws NullPointerException if the method reference is {@code null}.
     * @since 1.0
     */
    static <T> Assertion<T> isEqualTo(final Function<T, ?> methodReference, final Object o) {

        Objects.requireNonNull(methodReference, "methodReference");
        return (receiverSupplier) -> {
            final T receiver = receiverSupplier.get();
            if (receiver == null) {
                throw new AssertionError("Receiver was `null`");
            }
            final Object input = methodReference.apply(receiver);
            if (!Objects.equals(o, input)) {
                throw new AssertionError("Expected `" + o + "`, found `" + input + "`");
            }
        };
    }

    /**
     * Creates a new assertion that fails if the result is not equal to the other object.
     * <p>
     * Equality is defined by the {@code Objects.equals(o, result}.
     *
     * @param o the other object
     * @return the new assertion.
     * @param <T> the type of the result.
     * @since 1.0
     */
    static <T> Assertion<T> isEqualTo(final Object o) {

        return (inputSupplier) -> {
            final Object input = inputSupplier.get();
            if (!Objects.equals(o, input)) {
                throw new AssertionError("Expected `" + o + "`, found `" + input + "`");
            }
        };
    }

    /**
     * Creates a new assertion that fails if the result is equal to the other object.
     * <p>
     * Equality is defined by the {@code Objects.equals(o, result}.
     *
     * @param o the other object.
     * @return the new assertion.
     * @param <T> the type of the result.
     * @since 1.0
     */
    static <T> Assertion<T> isNotEqualTo(final Object o) {

        return (inputSupplier) -> {
            final Object input = inputSupplier.get();
            if (Objects.equals(o, input)) {
                throw new AssertionError(
                        "Expected anything but `" + o + "`, found `" + input + "`");
            }
        };
    }

    /**
     * Creates an assertion that fails if the result is {@code null}.
     * <p>
     * The same as {@code Assertion.isNotEqualTo(null)}.
     *
     * @return the new assertion.
     * @param <T> the type of the result.
     * @since 1.0
     */
    static <T> Assertion<T> isNotNull() {

        return isNotEqualTo(null);
    }

    /**
     * Creates a new assertion that succeeds only if the supplying the result fails with an
     * exception that is an instance of the specified type.
     *
     * @param exception the exception type.
     * @return the new assertion.
     * @param <T> the type of the result.
     * @throws NullPointerException if the exception type is {@code null}.
     * @since 1.0
     */
    static <T> Assertion<T> throwsException(final Class<? extends Throwable> exception) {

        Objects.requireNonNull(exception, "exception");
        return (inputSupplier) -> {
            try {
                inputSupplier.get();
            } catch (final Throwable throwable) {
                if (!exception.isInstance(throwable)) {
                    throw new AssertionError("Expected exception of type `" + exception.getName()
                            + "` to be thrown, found `" + throwable.getClass().getName() + ": "
                            + throwable.getMessage() + "`");
                }
                return;
            }
            throw new AssertionError("Expected exception of type `" + exception.getName()
                    + "` to be thrown, found `null`");
        };
    }

    /**
     * Creates a new assertion that fails if supplying the result fails.
     *
     * @return the new assertion.
     * @param <T> the type of the result.
     * @since 1.0
     */
    static <T> Assertion<T> doesNotThrow() {

        return (inputSupplier) -> {
            try {
                inputSupplier.get();
            } catch (final Throwable throwable) {
                throw new AssertionError(
                        "Expected no exception, found `" + throwable.getClass().getName() + ": "
                                + throwable.getMessage() + "`");
            }
        };
    }

    /*
    CharSequence validators
     */

    /**
     * Creates a new assertion that fails if the result does not start with the specified prefix.
     *
     * @param prefix the prefix.
     * @return the new assertion.
     * @param <T> the type of the result.
     * @throws NullPointerException if the prefix is {@code null}.
     * @since 1.0
     */
    static <T extends CharSequence> Assertion<T> startsWith(final String prefix) {

        Objects.requireNonNull(prefix, "prefix");
        return (inputSupplier) -> {
            final CharSequence input = inputSupplier.get();
            if (input == null) {
                throw new AssertionError("Input was `null`");
            }
            if (!input.toString().startsWith(prefix)) {
                throw new AssertionError("Expected `" + input + "` to start with `" + prefix + "`");
            }
        };
    }

    /**
     * Creates a new assertion that fails if the result does not end with the specified suffix.
     *
     * @param suffix the suffix.
     * @return the new assertion.
     * @param <T> the type of the result.
     * @throws NullPointerException if the suffix is {@code null}.
     * @since 1.0
     */
    static <T extends CharSequence> Assertion<T> endsWith(final String suffix) {

        Objects.requireNonNull(suffix, "suffix");
        return (inputSupplier) -> {
            final CharSequence input = inputSupplier.get();
            if (input == null) {
                throw new AssertionError("Input was `null`");
            }
            if (!input.toString().endsWith(suffix)) {
                throw new AssertionError("Expected `" + input + "` to end with `" + suffix + "`");
            }
        };
    }

    /**
     * Creates a new assertion that fails if the result does contain the specified substring.
     *
     * @param sub the substring.
     * @return the new assertion.
     * @param <T> the type of the result.
     * @throws NullPointerException if the substring is {@code null}.
     * @since 1.0
     */
    static <T extends CharSequence> Assertion<T> contains(final CharSequence sub) {

        Objects.requireNonNull(sub, "sub");
        return (inputSupplier) -> {
            final CharSequence input = inputSupplier.get();
            if (input == null) {
                throw new AssertionError("Input was `null`");
            }
            if (!input.toString().contains(sub)) {
                throw new AssertionError("Expected `" + input + "` to contain `" + sub + "`");
            }
        };
    }

    /**
     * Creates a new assertion that fails if the result does not match the regex.
     *
     * @param regex the regex.
     * @return the new assertion.
     * @param <T> the type of the result.
     * @throws NullPointerException if the regex is {@code null}.
     * @since 1.0
     */
    static <T extends CharSequence> Assertion<T> matches(final Pattern regex) {

        Objects.requireNonNull(regex, "regex");
        return (inputSupplier) -> {
            final CharSequence input = inputSupplier.get();
            if (input == null) {
                throw new AssertionError("Input was `null`");
            }
            if (!regex.matcher(input).matches()) {
                throw new AssertionError("Expected `" + input + "` to match `" + regex + "`");
            }
        };
    }

    /**
     * Creates a new assertion that fails if the result does not match the regex.
     *
     * @param regex the regex.
     * @return the new assertion.
     * @param <T> the type of the result.
     * @throws NullPointerException if the regex is {@code null}.
     * @since 1.0
     */
    static <T extends CharSequence> Assertion<T> matches(final String regex) {

        Objects.requireNonNull(regex, "regex");
        return matches(Pattern.compile(regex));
    }

    /**
     * Creates a new assertion that fails if the result is not in the range of min and max.
     *
     * @param min the min.
     * @param max the max.
     * @return the new assertion.
     * @param <T> the type of the result.
     * @throws IllegalArgumentException if min is greater than max.
     * @since 1.0
     */
    static <T extends Number> Assertion<T> isInRange(final int min, final int max) {

        if (min > max) {
            throw new IllegalArgumentException("min must be less than or equal to max");
        }
        return (resultSupplier) -> {
            final Number result = resultSupplier.get();
            if (result == null) {
                throw new AssertionError("Result was `null`");
            }
            if (min > result.intValue() || max < result.intValue()) {
                throw new AssertionError("Expected `" + result + "` to be in range `" + min + "-"
                        + max + "`");
            }
        };
    }

    /**
     * Creates a new assertion that fails if the result is less than or equal to the specified
     * value.
     *
     * @param min the min (exclusive).
     * @return the new assertion.
     * @param <T> the type of the result.
     * @since 1.0
     */
    static <T extends Number> Assertion<T> isGreaterThan(final int min) {

        return (resultSupplier) -> {
            final Number result = resultSupplier.get();
            if (result == null) {
                throw new AssertionError("Result was `null`");
            }
            if (min >= result.intValue()) {
                throw new AssertionError("Expected `" + result + "` to be greater than `" + min
                        + "`");
            }
        };
    }

    /**
     * Creates a new assertion that fails if the result is greater than or equal to the specified
     * value.
     *
     * @param max the max (exclusive).
     * @return the new assertion.
     * @param <T> the type of the result.
     * @since 1.0
     */
    static <T extends Number> Assertion<T> isLessThan(final int max) {

        return (resultSupplier) -> {
            final Number result = resultSupplier.get();
            if (result == null) {
                throw new AssertionError("Result was `null`");
            }
            if (max <= result.doubleValue()) {
                throw new AssertionError("Expected `" + result + "` to be less than `" + max + "`");
            }
        };
    }

    /**
     * Creates an assertion that fails if the result is not positive.
     *
     * @return the new assertion.
     * @param <T> the type of the result.
     * @since 1.0
     */
    static <T extends Number> Assertion<T> isPositive() {

        return (resultSupplier) -> {
            final Number result = resultSupplier.get();
            if (result == null) {
                throw new AssertionError("Result was `null`");
            }
            if (result.doubleValue() <= 0) {
                throw new AssertionError("Expected result to be positive, was `" + result.doubleValue() + "`");
            }
        };
    }

    /**
     * Creates an assertion that fails if the result is not negative.
     *
     * @return the new assertion.
     * @param <T> the type of the result.
     * @since 1.0
     */
    static <T extends Number> Assertion<T> isNegative() {

        return (resultSupplier) -> {
            final Number result = resultSupplier.get();
            if (result == null) {
                throw new AssertionError("Result was `null`");
            }
            if (result.doubleValue() >= 0) {
                throw new AssertionError("Expected result to be negative, was `" + result.doubleValue() + "`");
            }
        };
    }

    /**
     * Creates an assertion that fails if the result is positive.
     *
     * @return the new assertion.
     * @param <T> the type of the result.
     * @since 1.0
     */
    static <T extends Number> Assertion<T> isNotPositive() {

        return (resultSupplier) -> {
            final Number result = resultSupplier.get();
            if (result == null) {
                throw new AssertionError("Result was `null`");
            }
            if (result.doubleValue() > 0) {
                throw new AssertionError("Expected result to be not positive, was `" + result.doubleValue() + "`");
            }
        };
    }

    /**
     * Creates an assertion that fails if the result is negative.
     *
     * @return the new assertion.
     * @param <T> the type of the result.
     * @since 1.0
     */
    static <T extends Number> Assertion<T> isNotNegative() {

        return (resultSupplier) -> {
            final Number result = resultSupplier.get();
            if (result == null) {
                throw new AssertionError("Result was `null`");
            }
            if (result.doubleValue() < 0) {
                throw new AssertionError("Expected result to be not negative, was `" + result.doubleValue() + "`");
            }
        };
    }
}
