package me.sparky983.spark;

import org.junit.jupiter.api.Test;

import static me.sparky983.spark.Assertion.doesNotThrow;
import static me.sparky983.spark.Assertion.isInstanceOf;
import static me.sparky983.spark.Assertion.throwsException;
import static me.sparky983.spark.Given.given;

class IsInstanceOfTest {

    @Test
    void testIsInstanceOfWhenClassNull() {

        given((Class<?>) null)
                .when(Assertion::isInstanceOf)
                .then(throwsException(NullPointerException.class));
    }

    @Test
    void testIsInstanceOf() {

        given(isInstanceOf(Class.class))
                .whenDo((isInstanceOf) -> isInstanceOf.doAssertion(() -> Object.class))
                .then(doesNotThrow());

        given(isInstanceOf(Class.class))
                .whenDo((isInstanceOf) -> isInstanceOf.doAssertion(
                        () -> "not an instance of class"))
                .then(throwsException(AssertionError.class));
    }
}
