package me.sparky983.spark;

import static me.sparky983.spark.Assertion.doesNotThrow;
import static me.sparky983.spark.Assertion.isIn;
import static me.sparky983.spark.Assertion.throwsException;
import static me.sparky983.spark.Given.given;

import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.Collection;

class IsInTest {

    @Test
    void testIsInWhenCollectionNullThrows() {

        given((Collection<?>) null)
                .when(Assertion::isIn)
                .then(throwsException(NullPointerException.class));
    }

    @Test
    void testIsIn() {

        given(isIn(Arrays.asList("not a string", "also not a string")))
                .whenDo((isIn) -> isIn.doAssertion(() -> "a string"))
                .then(throwsException(AssertionError.class));

        given(isIn(Arrays.asList("not a string", "also not a string")))
                .whenDo((isIn) -> isIn.doAssertion(() -> null))
                .then(throwsException(AssertionError.class));

        given(isIn(Arrays.asList("a string", "not a string")))
                .whenDo((isIn) -> isIn.doAssertion(() -> "a string"))
                .then(doesNotThrow());

        given(isIn(Arrays.asList(null, null)))
                .whenDo((isIn) -> isIn.doAssertion(() -> null))
                .then(doesNotThrow());
    }
}
