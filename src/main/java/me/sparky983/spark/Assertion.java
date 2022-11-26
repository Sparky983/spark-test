package me.sparky983.spark;

import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.function.Function;
import java.util.function.Predicate;
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
        return (resultSupplier) -> {
            try {
                assertion.doAssertion(resultSupplier);
            } catch (final AssertionError error) {
                return;
            }
            throw new AssertionError("Expected <" + assertion + "> to fail");
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
        return (resultSupplier) -> {
            final Object result = resultSupplier.get();
            if (!cls.isInstance(result)) {
                throw new AssertionError("Input must be of instance <" + cls.getName() + ">, was "
                        + result.getClass().getName());
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
                throw new AssertionError("Receiver was <null>");
            }
            final Object result = methodReference.apply(receiver);
            if (!Objects.equals(o, result)) {
                throw new AssertionError("Expected <" + o + ">, found <" + result + ">");
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

        return (resultSupplier) -> {
            final Object result = resultSupplier.get();
            if (!Objects.equals(o, result)) {
                throw new AssertionError("Expected <" + o + ">, found <" + result + ">");
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

        return (resultSupplier) -> {
            final Object result = resultSupplier.get();
            if (Objects.equals(o, result)) {
                throw new AssertionError(
                        "Expected anything but <" + o + ">, found <" + result + ">");
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
     * Creates a new assertion that succeeds only if supplying the result fails with an
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
        return (resultSupplier) -> {
            try {
                resultSupplier.get();
            } catch (final Throwable throwable) {
                if (exception.isInstance(throwable)) {
                    return;
                }
                throw new AssertionError("Expected exception of type <" + exception.getName()
                        + "> to be thrown, found <" + throwable.getClass().getName() + ": "
                        + throwable.getMessage() + ">", throwable);
            }
            throw new AssertionError("Expected exception of type <" + exception.getName()
                    + "> to be thrown, found <null>");
        };
    }

    /**
     * Creates a new assertion that succeeds only if supplying the result fails with an exception
     * that is an instance of the specified type and with the specified message.
     *
     * @param exception the type.
     * @param message the message.
     * @return the new assertion.
     * @param <T> the type of the result.
     * @throws NullPointerException if the exception or message is {@code null}.
     * @since 1.1
     */
    static <T> Assertion<T> throwsException(final Class<? extends Throwable> exception,
                                            final String message) {

        Objects.requireNonNull(exception, "exception cannot be null");
        Objects.requireNonNull(message, "message cannot be null");

        return (resultSupplier) -> {
            try {
                resultSupplier.get();
            } catch (final Throwable throwable) {
                if (exception.isInstance(throwable) && message.equals(throwable.getMessage())) {
                    return;
                }
                throw new AssertionError("Expected <"
                        + exception.getName() + ": " + message
                        + "> to be thrown, found <"
                        + throwable.getClass().getName() + ": " + throwable.getMessage() +
                        ">", throwable);
            }
            throw new AssertionError("Expected exception of type <"
                    + exception.getName() + ": " + message
                    + "> to be thrown, found <null>");
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

        return (resultSupplier) -> {
            try {
                resultSupplier.get();
            } catch (final Throwable throwable) {
                throw new AssertionError(
                        "Expected no exception, found <" + throwable.getClass().getName() + ": "
                                + throwable.getMessage() + ">", throwable);
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
        return (resultSupplier) -> {
            final CharSequence result = resultSupplier.get();
            if (result == null) {
                throw new AssertionError("Input was <null>");
            }
            if (!result.toString().startsWith(prefix)) {
                throw new AssertionError("Expected <" + result + "> to start with <" + prefix + ">");
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
        return (resultSupplier) -> {
            final CharSequence result = resultSupplier.get();
            if (result == null) {
                throw new AssertionError("Input was <null>");
            }
            if (!result.toString().endsWith(suffix)) {
                throw new AssertionError("Expected <" + result + "> to end with <" + suffix + ">");
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
        return (resultSupplier) -> {
            final CharSequence result = resultSupplier.get();
            if (result == null) {
                throw new AssertionError("Input was <null>");
            }
            if (!result.toString().contains(sub)) {
                throw new AssertionError("Expected <" + result + "> to contain <" + sub + ">");
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
        return (resultSupplier) -> {
            final CharSequence result = resultSupplier.get();
            if (result == null) {
                throw new AssertionError("Input was <null>");
            }
            if (!regex.matcher(result).matches()) {
                throw new AssertionError("Expected <" + result + "> to match <" + regex + ">");
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

    /*
    Collection assertions
     */

    /**
     * Creates a new assertion that fails if the result is not in the specified collection.
     *
     * @param collection the collection.
     * @return the new assertion.
     * @param <T> the type of the result.
     * @throws NullPointerException if the collection is {@code null}.
     * @see #isNotIn(Collection)
     * @since 1.0
     */
    static <T> Assertion<T> isIn(final Collection<?> collection) {

        Objects.requireNonNull(collection, "collection");
        return (resultSupplier) -> {
            final Object result = resultSupplier.get();
            if (!collection.contains(result)) {
                throw new AssertionError("Expected <" + collection + "> to contain <" + result + ">");
            }
        };
    }

    /**
     * Creates a new assertion that fails if the result is in the specified collection.
     *
     * @param collection the collection.
     * @return the new assertion.
     * @param <T> the type of the result.
     * @throws NullPointerException if the collection is {@code null}.
     * @see #isIn(Collection)
     * @since 1.0
     */
    static <T> Assertion<T> isNotIn(final Collection<?> collection) {

        Objects.requireNonNull(collection, "collection");
        return (resultSupplier) -> {
            final Object result = resultSupplier.get();
            if (collection.contains(result)) {
                throw new AssertionError("Expected <" + collection + "> to not contain <" + result + ">");
            }
        };
    }

    /**
     * Creates a new assertion that fails if any elements don't match the specified predicate.
     *
     * @param predicate the predicate.
     * @return the new assertion.
     * @param <T> the type of the result.
     * @param <E> the type of the collection's elements.
     * @throws NullPointerException if the predicate is {@code null}.
     * @since 1.0
     */
    static <T extends Collection<E>, E> Assertion<T> allMatch(final Predicate<E> predicate) {

        Objects.requireNonNull(predicate, "predicate");
        return (resultSupplier) -> {
            final T result = resultSupplier.get();
            int i = 0;
            for (E e : result) {
                if (!predicate.test(e)) {
                    throw new AssertionError("Item at result[" + i + "] of <" + result + "> (<" + e + ">) did not match the given predicate");
                }
                i++;
            }
        };
    }

    /**
     * Creates a new assertion that fails if all elements don't match the specified predicate.
     *
     * @param predicate the predicate.
     * @return the new assertion.
     * @param <T> the type of the result.
     * @param <E> the type of the collection's elements.
     * @throws NullPointerException if the predicate is {@code null}.
     * @since 1.0
     */
    static <T extends Collection<E>, E> Assertion<T> anyMatch(final Predicate<E> predicate) {

        Objects.requireNonNull(predicate, "predicate");
        return (resultSupplier) -> {
            final T result = resultSupplier.get();
            for (E e : result) {
                if (predicate.test(e)) {
                    return;
                }
            }
            throw new AssertionError("No elements of <" + result + "> matched the given predicate");
        };
    }

    /**
     * Creates a new assertion that fails if any elements match the specified predicate.
     *
     * @param predicate the predicate.
     * @return the new assertion.
     * @param <T> the type of the result.
     * @param <E> the type of the collection's elements.
     * @throws NullPointerException if the predicate is {@code null}.
     * @since 1.0
     */
    static <T extends Collection<E>, E> Assertion<T> noneMatch(final Predicate<E> predicate) {

        Objects.requireNonNull(predicate, "predicate");
        return (resultSupplier) -> {
            final T result = resultSupplier.get();
            int i = 0;
            for (E e : result) {
                if (predicate.test(e)) {
                    throw new AssertionError("Item at index result[" + i + "] of <" + result + "> (<" + e + ">) matched the given predicate");
                }
                i++;
            }
        };
    }

    /**
     * Creates an assertion that fails if the result does not contain any of the specified objects.
     *
     * @param objects the objects
     * @return the new assertion.
     * @param <T> the type of the result.
     * @throws NullPointerException if the objects is {@code null}.
     * @since 1.0
     */
    static <T extends Collection<?>> Assertion<T> contains(final Object... objects) {

        Objects.requireNonNull(objects, "objects");
        return (resultSupplier) -> {
            final T result = resultSupplier.get();
            if (result == null) {
                throw new AssertionError("Result was <null>");
            }
            for (Object o : objects) {
                if (!result.contains(o)) {
                    throw new AssertionError("Expected <" + result + "> to contain <" + o + ">");
                }
            }
        };
    }

    /**
     * Creates a new assertion that fails if the element at the specified index in the result does
     * not equal the other object or the list is shorter than the index.
     *
     * @param o the other object.
     * @return the new assertion.
     * @param <T> the type of the result.
     * @throws IndexOutOfBoundsException if the index is negative.
     * @since 1.0
     */
    static <T extends List<?>> Assertion<T> indexEquals(final int index, final Object o) {

        if (index < 0) {
            throw new IndexOutOfBoundsException("index must be positive");
        }
        return (resultSupplier) -> {
            final T result = resultSupplier.get();
            if (result == null) {
                throw new AssertionError("result was <null>");
            }
            if (index >= result.size()) {
                throw new AssertionError("index (" + index + ") is greater than the result's size (" + result.size() + ")");
            }
            if (!Objects.equals(o, result.get(index))) {
                throw new AssertionError("result[" + index + "] does not equal <" + o + ">");
            }
        };
    }

    /**
     * Returns a new assertion that fails if the resulting collection is unmodifiable.
     * <p>
     * Note that this may modify the collection.
     *
     * @return the new assertion.
     * @param <T> the type of the result.
     * @since 1.0
     */
    static <T extends Collection<E>, E> Assertion<T> isModifiable() {

        return (resultSupplier) -> {
            final T result = resultSupplier.get();
            try {
                result.add(null);
            } catch (final UnsupportedOperationException e) {
                throw new AssertionError("result (<" + result + ">) is unmodifiable");
            }
        };
    }

    /**
     * Returns a new assertion that fails if the resulting collection is modifiable.
     * <p>
     * Note that this may modify the collection.
     *
     * @return the new assertion.
     * @param <T> the type of the result.
     * @since 1.0
     */
    static <T extends Collection<?>> Assertion<T> isUnmodifiable() {

        return (resultSupplier) -> {
            final T result = resultSupplier.get();
            try {
                result.add(null);
                throw new AssertionError("result (<" + result + ">) is modifiable");
            } catch (final UnsupportedOperationException e) {
                // it is unmodifiable
            }
        };
    }

    /*
    Range assertions
     */

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
                throw new AssertionError("Result was <null>");
            }
            if (min > result.intValue() || max < result.intValue()) {
                throw new AssertionError("Expected <" + result + "> to be in range <" + min + "-"
                        + max + ">");
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
                throw new AssertionError("Result was <null>");
            }
            if (min >= result.intValue()) {
                throw new AssertionError("Expected <" + result + "> to be greater than <" + min
                        + ">");
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
                throw new AssertionError("Result was <null>");
            }
            if (max <= result.doubleValue()) {
                throw new AssertionError("Expected <" + result + "> to be less than <" + max + ">");
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
                throw new AssertionError("Result was <null>");
            }
            if (result.doubleValue() <= 0) {
                throw new AssertionError("Expected result to be positive, was <" + result.doubleValue() + ">");
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
                throw new AssertionError("Result was <null>");
            }
            if (result.doubleValue() >= 0) {
                throw new AssertionError("Expected result to be negative, was <" + result.doubleValue() + ">");
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
                throw new AssertionError("Result was <null>");
            }
            if (result.doubleValue() > 0) {
                throw new AssertionError("Expected result to be not positive, was <" + result.doubleValue() + ">");
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
                throw new AssertionError("Result was <null>");
            }
            if (result.doubleValue() < 0) {
                throw new AssertionError("Expected result to be not negative, was <" + result.doubleValue() + ">");
            }
        };
    }
}
