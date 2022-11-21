package me.sparky983.spark;

import static me.sparky983.spark.Assertion.doesNotThrow;
import static me.sparky983.spark.Assertion.indexEquals;
import static me.sparky983.spark.Assertion.throwsException;
import static me.sparky983.spark.Given.given;

import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.Collections;

class IndexEqualsTest {

    @Test
    void testIndexEqualsWhenIndexIsNegative() {

        given(-1)
                .when((i) -> indexEquals(i, null))
                .then(throwsException(IndexOutOfBoundsException.class));

        given(Integer.MIN_VALUE)
                .when((i) -> indexEquals(i, null))
                .then(throwsException(IndexOutOfBoundsException.class));
    }

    @Test
    void testIndexEqualsWhenShorterThanCollection() {

        given(indexEquals(0, null))
                .whenDo((indexEquals) -> indexEquals.doAssertion(Collections::emptyList))
                .then(throwsException(AssertionError.class));

        given(indexEquals(0, new Object())) // they have different identity, and default equals method compares using identity
                .whenDo((indexEquals) -> indexEquals.doAssertion(() -> Collections.singletonList(new Object())))
                .then(throwsException(AssertionError.class));

        given(indexEquals(0, new Object()))
                .whenDo((indexEquals) -> indexEquals.doAssertion(() -> Collections.singletonList(null)))
                .then(throwsException(AssertionError.class));

        given(indexEquals(0, new Object()))
                .whenDo((indexEquals) -> indexEquals.doAssertion(() -> null))
                .then(throwsException(AssertionError.class));

        final Object o = new Object();

        given(indexEquals(1, o))
                .whenDo((indexEquals) -> indexEquals.doAssertion(() -> Arrays.asList(new Object(), o)))
                .then(doesNotThrow());

        given(indexEquals(0, null))
                .whenDo((indexEquals) -> indexEquals.doAssertion(() -> Arrays.asList(null, null)))
                .then(doesNotThrow());
    }

}
