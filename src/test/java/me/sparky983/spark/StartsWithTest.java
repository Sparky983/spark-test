package me.sparky983.spark;

import org.junit.jupiter.api.Test;

import static me.sparky983.spark.Assertion.doesNotThrow;
import static me.sparky983.spark.Assertion.startsWith;
import static me.sparky983.spark.Assertion.throwsException;
import static me.sparky983.spark.Given.given;
import static me.sparky983.spark.Given.givenNull;

class StartsWithTest {

    @Test
    void testStartsWithWhenPrefixNullThrows() {

        givenNull(String.class)
                .when(Assertion::startsWith)
                .then(throwsException(NullPointerException.class));
    }

    @Test
    void testStartsWith() {

        given(startsWith("prefix"))
                .whenDo((startsWith) -> startsWith.doAssertion(() -> "prefix and other stuff"))
                .then(doesNotThrow());

        given(startsWith("prefix"))
                .whenDo((startsWith) -> startsWith.doAssertion(
                        () -> "does not start with the prefix"))
                .then(throwsException(AssertionError.class));

        given(startsWith("prefix"))
                .whenDo((startsWith) -> startsWith.doAssertion(() -> null))
                .then(throwsException(AssertionError.class));
    }
}
