package me.sparky983.spark;

import org.junit.jupiter.api.Test;

import static me.sparky983.spark.Assertion.doesNotThrow;
import static me.sparky983.spark.Assertion.isNotEqualTo;
import static me.sparky983.spark.Assertion.throwsException;
import static me.sparky983.spark.Given.given;

class IsNotEqualToTest {

    @Test
    void testIsNotEqualTo() {

        given(isNotEqualTo("some string"))
                .whenDo((isNotEqualTo) -> isNotEqualTo.doAssertion(() -> "some other string"))
                .then(doesNotThrow());

        given(isNotEqualTo(null))
                .whenDo((isNotEqualTo) -> isNotEqualTo.doAssertion(() -> "not null"))
                .then(doesNotThrow());

        given(isNotEqualTo("some string"))
                .whenDo((isNotEqualTo) -> isNotEqualTo.doAssertion(() -> "some string"))
                .then(throwsException(AssertionError.class));

        given(isNotEqualTo("not null"))
                .whenDo((isNotEqualTo) -> isNotEqualTo.doAssertion(() -> null))
                .then(throwsException(AssertionError.class));
    }
}
