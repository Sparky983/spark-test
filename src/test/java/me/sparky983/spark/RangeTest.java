package me.sparky983.spark;

import org.junit.jupiter.api.Test;

import static me.sparky983.spark.Assertion.doesNotThrow;
import static me.sparky983.spark.Assertion.isGreaterThan;
import static me.sparky983.spark.Assertion.isInRange;
import static me.sparky983.spark.Assertion.isLessThan;
import static me.sparky983.spark.Assertion.isNegative;
import static me.sparky983.spark.Assertion.isNotNegative;
import static me.sparky983.spark.Assertion.isNotPositive;
import static me.sparky983.spark.Assertion.isPositive;
import static me.sparky983.spark.Assertion.throwsException;
import static me.sparky983.spark.Given.given;
import static me.sparky983.spark.When.when;

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

    @Test
    void testIsPositive() {

        given(isPositive())
                .whenDo((isPositive) -> isPositive.doAssertion(() -> -1))
                .then(throwsException(AssertionError.class));

        given(isPositive())
                .whenDo((isPositive) -> isPositive.doAssertion(() -> 0))
                .then(throwsException(AssertionError.class));

        given(isPositive())
                .whenDo((isPositive) -> isPositive.doAssertion(() -> null))
                .then(throwsException(AssertionError.class));

        given(isPositive())
                .whenDo((isPositive) -> isPositive.doAssertion(() -> 1))
                .then(doesNotThrow());

        given(isPositive())
                .whenDo((isPositive) -> isPositive.doAssertion(() -> Integer.MAX_VALUE))
                .then(doesNotThrow());
    }

    @Test
    void testIsNegative() {

        given(isNegative())
                .whenDo((isNegative) -> isNegative.doAssertion(() -> 1))
                .then(throwsException(AssertionError.class));

        given(isNegative())
                .whenDo((isNegative) -> isNegative.doAssertion(() -> 0))
                .then(throwsException(AssertionError.class));

        given(isNegative())
                .whenDo((isNegative) -> isNegative.doAssertion(() -> null))
                .then(throwsException(AssertionError.class));

        given(isNegative())
                .whenDo((isNegative) -> isNegative.doAssertion(() -> -1))
                .then(doesNotThrow());

        given(isNegative())
                .whenDo((isNegative) -> isNegative.doAssertion(() -> Integer.MIN_VALUE))
                .then(doesNotThrow());
    }

    @Test
    void testIsNotPositive() {

        given(isNotPositive())
                .whenDo((isNotPositive) -> isNotPositive.doAssertion(() -> 1))
                .then(throwsException(AssertionError.class));

        given(isNotPositive())
                .whenDo((isNotPositive) -> isNotPositive.doAssertion(() -> null))
                .then(throwsException(AssertionError.class));

        given(isNotPositive())
                .whenDo((isNotPositive) -> isNotPositive.doAssertion(() -> -1))
                .then(doesNotThrow());

        given(isNotPositive())
                .whenDo((isNotPositive) -> isNotPositive.doAssertion(() -> 0))
                .then(doesNotThrow());

        given(isNotPositive())
                .whenDo((isNotPositive) -> isNotPositive.doAssertion(() -> Integer.MIN_VALUE))
                .then(doesNotThrow());
    }

    @Test
    void testIsNotNegative() {

        given(isNotNegative())
                .whenDo((isNotNegative) -> isNotNegative.doAssertion(() -> -1))
                .then(throwsException(AssertionError.class));

        given(isNotNegative())
                .whenDo((isNotNegative) -> isNotNegative.doAssertion(() -> null))
                .then(throwsException(AssertionError.class));

        given(isNotNegative())
                .whenDo((isNotNegative) -> isNotNegative.doAssertion(() -> 1))
                .then(doesNotThrow());

        given(isNotNegative())
                .whenDo((isNotNegative) -> isNotNegative.doAssertion(() -> 0))
                .then(doesNotThrow());

        given(isNotNegative())
                .whenDo((isNotNegative) -> isNotNegative.doAssertion(() -> Integer.MAX_VALUE))
                .then(doesNotThrow());
    }
}
