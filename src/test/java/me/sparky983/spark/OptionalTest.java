package me.sparky983.spark;

import static me.sparky983.spark.Assertion.doesNotThrow;
import static me.sparky983.spark.Assertion.isEmpty;
import static me.sparky983.spark.Assertion.isPresent;
import static me.sparky983.spark.Assertion.throwsException;
import static me.sparky983.spark.Given.given;

import org.junit.jupiter.api.Test;

import java.util.Optional;

class OptionalTest {

    @Test
    void testIsPresent() {

        given(isPresent())
                .whenDo((isPresent) -> isPresent.doAssertion(Optional::empty))
                .then(throwsException(AssertionError.class));


        given(isPresent())
                .whenDo((isPresent) -> isPresent.doAssertion(() -> Optional.of(new Object())))
                .then(doesNotThrow());
    }

    @Test
    void testIsEmpty() {

        given(isEmpty())
                .whenDo((isEmpty) -> isEmpty.doAssertion(() -> Optional.of(new Object())))
                .then(throwsException(AssertionError.class));

        given(isEmpty())
                .whenDo((isEmpty) -> isEmpty.doAssertion(Optional::empty))
                .then(doesNotThrow());
    }
}
