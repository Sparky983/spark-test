package me.sparky983.spark;

import java.util.Objects;
import java.util.function.Consumer;
import java.util.function.Function;

public final class Given<T> {

    private final T given;

    public Given(final T given) {

        this.given = given;
    }

    public static <T> Given<T> given(final T t) {

        return new Given<>(t);
    }

    public static <T> Given<T> givenNull() {

        return new Given<>(null);
    }

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
