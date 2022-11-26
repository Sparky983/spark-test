package me.sparky983.spark;

import static me.sparky983.spark.Assertion.doesNotThrow;
import static me.sparky983.spark.Assertion.isFalse;
import static me.sparky983.spark.Assertion.isTrue;
import static me.sparky983.spark.Assertion.throwsException;
import static me.sparky983.spark.Given.given;

import org.junit.jupiter.api.Test;

class BooleanTest {

    @Test
    void testIsTrue() {

        given(isTrue())
                .whenDo((isTrue) -> isTrue.doAssertion(() -> false))
                .then(throwsException(AssertionError.class));

        given(isTrue())
                .whenDo((isTrue) -> isTrue.doAssertion(() -> true))
                .then(doesNotThrow());
    }

    @Test
    void testIsFalse() {

        given(isFalse())
                .whenDo((isFalse) -> isFalse.doAssertion(() -> true))
                .then(throwsException(AssertionError.class));

        given(isFalse())
                .whenDo((isFalse) -> isFalse.doAssertion(() -> false))
                .then(doesNotThrow());
    }
}
