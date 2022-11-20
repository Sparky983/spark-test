package me.sparky983.spark;

import java.util.Objects;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.regex.Pattern;

@FunctionalInterface
public interface Assertion<T> {

    void doAssertion(Supplier<T> then);

    static <T> Assertion<T> not(Assertion<T> assertion) {

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

    static <T> Assertion<T> isEqualTo(final Object o) {

        return (inputSupplier) -> {
            final Object input = inputSupplier.get();
            if (!Objects.equals(o, input)) {
                throw new AssertionError("Expected `" + o + "`, found `" + input + "`");
            }
        };
    }

    static <T> Assertion<T> isNotEqualTo(final Object o) {

        return (inputSupplier) -> {
            final Object input = inputSupplier.get();
            if (Objects.equals(o, input)) {
                throw new AssertionError(
                        "Expected anything but `" + o + "`, found `" + input + "`");
            }
        };
    }

    static <T> Assertion<T> isNotNull() {

        return isNotEqualTo(null);
    }

    static <T> Assertion<T> throwsException(final Class<? extends Throwable> exception) {

        return (inputSupplier) -> {
            try {
                inputSupplier.get();
                throw new AssertionError("Expected exception of type `" + exception.getName()
                        + "` to be thrown, found `null`");
            } catch (final Throwable throwable) {
                if (!exception.isInstance(throwable)) {
                    throw new AssertionError("Expected exception of type `" + exception.getName()
                            + "` to be thrown, found `" + throwable.getClass().getName() + ": "
                            + throwable.getMessage() + "`");
                }
            }
        };
    }

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

    static <T extends CharSequence> Assertion<T> matches(final String regex) {

        Objects.requireNonNull(regex, "regex");
        return matches(Pattern.compile(regex));
    }
}
