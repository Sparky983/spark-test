package me.sparky983.spark;

import org.junit.jupiter.api.Test;

import static me.sparky983.spark.Assertion.doesNotThrow;
import static me.sparky983.spark.Assertion.isNotNull;
import static me.sparky983.spark.Assertion.throwsException;
import static me.sparky983.spark.Given.given;

class IsNotNullTest {

    @Test
    void testIsNotNull() {

        given(isNotNull())
                .whenDo((isNotNull) -> isNotNull.doAssertion(() -> null))
                .then(throwsException(AssertionError.class));

        given(isNotNull())
                .whenDo((isNotNull) -> isNotNull.doAssertion(() -> "not null"))
                .then(doesNotThrow());
    }
}
