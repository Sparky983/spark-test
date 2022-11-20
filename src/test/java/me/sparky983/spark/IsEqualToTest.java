package me.sparky983.spark;

import org.junit.jupiter.api.Test;

import java.util.function.Function;

import static me.sparky983.spark.Assertion.doesNotThrow;
import static me.sparky983.spark.Assertion.isEqualTo;
import static me.sparky983.spark.Assertion.throwsException;
import static me.sparky983.spark.Given.given;

class IsEqualToTest {

    @Test
    void testIsEqualToWhenMethodReferenceNullThrows() {

        given((Function<Object, ?>) null)
                .when((methodReference) -> isEqualTo(methodReference, new Object()))
                .then(throwsException(NullPointerException.class));
    }

    @Test
    void testIsEqualTo() {

        given(isEqualTo("a string"))
                .whenDo((isEqualTo) -> isEqualTo.doAssertion(() -> "a string"))
                .then(doesNotThrow());

        given(isEqualTo("a string"))
                .whenDo((isEqualTo) -> isEqualTo.doAssertion(() -> "a different string"))
                .then(throwsException(AssertionError.class));

        given(isEqualTo(null))
                .whenDo((isEqualTo) -> isEqualTo.doAssertion(() -> null))
                .then(doesNotThrow());

        given(isEqualTo(null))
                .whenDo((isEqualTo) -> isEqualTo.doAssertion(() -> "not null"))
                .then(throwsException(AssertionError.class));

        given(isEqualTo((String str) -> str.substring(0, 4), "a st"))
                .whenDo((isEqualTo) -> isEqualTo.doAssertion(() -> "a string"))
                .then(doesNotThrow());

        given(isEqualTo((String str) -> str.substring(5, 5), "a string"))
                .whenDo((isEqualTo) -> isEqualTo.doAssertion(() -> "a different string"))
                .then(throwsException(AssertionError.class));

        given(isEqualTo(Object::toString, null))
                .whenDo((isEqualTo) -> isEqualTo.doAssertion(() -> null))
                .then(throwsException(AssertionError.class));

        given(isEqualTo(Object::toString, null))
                .whenDo((isEqualTo) -> isEqualTo.doAssertion(() -> "not null"))
                .then(throwsException(AssertionError.class));
    }
}
