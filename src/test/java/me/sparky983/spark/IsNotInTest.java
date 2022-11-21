package me.sparky983.spark;

import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.Collection;

import static me.sparky983.spark.Assertion.doesNotThrow;
import static me.sparky983.spark.Assertion.isNotIn;
import static me.sparky983.spark.Assertion.throwsException;
import static me.sparky983.spark.Given.given;

class IsNotInTest {

    @Test
    void testIsNotInWhenNullCollectionThrows() {

        given((Collection<?>) null)
                .when(Assertion::isNotIn)
                .then(throwsException(NullPointerException.class));
    }

    @Test
    void testIsNotInCollection() {

        given(isNotIn(Arrays.asList("a string", "another string")))
                .whenDo((isNotIn) -> isNotIn.doAssertion(() -> "a string"))
                .then(throwsException(AssertionError.class));

        given(isNotIn(Arrays.asList(null, null)))
                .whenDo((isNotIn) -> isNotIn.doAssertion(() -> null))
                .then(throwsException(AssertionError.class));

        given(isNotIn(Arrays.asList("a string", "another string")))
                .whenDo((isNotIn) -> isNotIn.doAssertion(() -> "not a string"))
                .then(doesNotThrow());

        given(isNotIn(Arrays.asList("a string", "another string")))
                .whenDo((isNotIn) -> isNotIn.doAssertion(() -> null))
                .then(doesNotThrow());
    }
}
