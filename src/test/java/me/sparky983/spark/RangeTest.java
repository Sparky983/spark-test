package me.sparky983.spark;

import static me.sparky983.spark.Assertion.*;
import static me.sparky983.spark.Given.given;
import static me.sparky983.spark.When.when;

import org.junit.jupiter.api.Test;

class RangeTest {

    @Test
    void testIsInRangeWhenMinIsGreaterThanMax() {

        when(() -> isInRange(1, 0))
                .then(throwsException(IllegalArgumentException.class));

        when(() -> isInRange(0, -1))
                .then(throwsException(IllegalArgumentException.class));
    }

    @Test
    void testIsInRange() {

        given(isInRange(50, 100))
                .whenDo((isInRange) -> isInRange.doAssertion(() -> 49))
                .then(throwsException(AssertionError.class));

        given(isInRange(50, 100))
                .whenDo((isInRange) -> isInRange.doAssertion(() -> 101))
                .then(throwsException(AssertionError.class));

        given(isInRange(50, 100))
                .whenDo((isInRange) -> isInRange.doAssertion(() -> null))
                .then(throwsException(AssertionError.class));

        given(isInRange(1, 1))
                .whenDo((isInRange) -> isInRange.doAssertion(() -> 1))
                .then(doesNotThrow());

        given(isInRange(50, 100))
                .whenDo((isInRange) -> isInRange.doAssertion(() -> 50))
                .then(doesNotThrow());

        given(isInRange(50, 100))
                .whenDo((isInRange) -> isInRange.doAssertion(() -> 100))
                .then(doesNotThrow());

        given(isInRange(50, 100))
                .whenDo((isInRange) -> isInRange.doAssertion(() -> 75))
                .then(doesNotThrow());
    }

    @Test
    void testIsGreaterThan() {

        given(isGreaterThan(50))
                .whenDo((isGreaterThan) -> isGreaterThan.doAssertion(() -> 49))
                .then(throwsException(AssertionError.class));

        given(isGreaterThan(50))
                .whenDo((isGreaterThan) -> isGreaterThan.doAssertion(() -> 50))
                .then(throwsException(AssertionError.class));

        given(isGreaterThan(50))
                .whenDo((isGreaterThan) -> isGreaterThan.doAssertion(() -> null))
                .then(throwsException(AssertionError.class));

        given(isGreaterThan(50))
                .whenDo((isGreaterThan) -> isGreaterThan.doAssertion(() -> 51))
                .then(doesNotThrow());
    }

    @Test
    void testIsLessThan() {

        given(isLessThan(50))
                .whenDo((isLessThan) -> isLessThan.doAssertion(() -> 51))
                .then(throwsException(AssertionError.class));

        given(isLessThan(50))
                .whenDo((isLessThan) -> isLessThan.doAssertion(() -> 50))
                .then(throwsException(AssertionError.class));

        given(isLessThan(50))
                .whenDo((isLessThan) -> isLessThan.doAssertion(() -> null))
                .then(throwsException(AssertionError.class));

        given(isLessThan(50))
                .whenDo((isLessThan) -> isLessThan.doAssertion(() -> 49))
                .then(doesNotThrow());
    }
}
