package me.sparky983.spark;

import org.junit.jupiter.api.Test;

import static me.sparky983.spark.Assertion.doesNotThrow;
import static me.sparky983.spark.Assertion.isEqualTo;
import static me.sparky983.spark.Assertion.not;
import static me.sparky983.spark.Assertion.throwsException;
import static me.sparky983.spark.Given.given;

class NotAssertionTest {

    @Test
    void testNotWhenAssertionNull() {

        given((Assertion<?>) null)
                .when(Assertion::not)
                .then(throwsException(NullPointerException.class));
    }

    @Test
    void testNot() {

        given(not(isEqualTo("some value")))
                .whenDo((not) -> not.doAssertion(() -> "some value"))
                .then(throwsException(AssertionError.class));

        given(not(isEqualTo("some value")))
                .whenDo((not) -> not.doAssertion(() -> "some other value"))
                .then(doesNotThrow());
    }
}
