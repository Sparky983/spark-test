package me.sparky983.spark;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Collections;

import static me.sparky983.spark.Assertion.doesNotThrow;
import static me.sparky983.spark.Assertion.isModifiable;
import static me.sparky983.spark.Assertion.isUnmodifiable;
import static me.sparky983.spark.Assertion.throwsException;
import static me.sparky983.spark.Given.given;

class ModifiableTest {

    @Test
    void testIsModifiable() {

        given(isModifiable())
                .whenDo((isModifiable) -> isModifiable.doAssertion(Collections::emptyList))
                .then(throwsException(AssertionError.class));

        given(isModifiable())
                .whenDo((isModifiable) -> isModifiable.doAssertion(ArrayList::new))
                .then(doesNotThrow());
    }

    @Test
    void testIsUnmodifiable() {

        given(isUnmodifiable())
                .whenDo((isUnmodifiable) -> isUnmodifiable.doAssertion(ArrayList::new))
                .then(throwsException(AssertionError.class));

        given(isUnmodifiable())
                .whenDo((isUnmodifiable) -> isUnmodifiable.doAssertion(Collections::emptyList))
                .then(doesNotThrow());
    }
}
