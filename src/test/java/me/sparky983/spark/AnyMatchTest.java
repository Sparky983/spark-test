package me.sparky983.spark;

import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.Objects;
import java.util.function.Predicate;

import static me.sparky983.spark.Assertion.anyMatch;
import static me.sparky983.spark.Assertion.doesNotThrow;
import static me.sparky983.spark.Assertion.throwsException;
import static me.sparky983.spark.Given.given;

class AnyMatchTest {

    @Test
    void testAnyMatchWhenPredicateNullThrows() {

        given((Predicate<?>) null)
                .when(Assertion::anyMatch)
                .then(throwsException(NullPointerException.class));
    }

    @Test
    void testAnyMatch() {

        given(anyMatch((String s) -> s.length() <= 3))
                .whenDo((anyMatch) -> anyMatch.doAssertion(() -> Arrays.asList("longer than 3 characters", "also longer than 3 characters")))
                .then(throwsException(AssertionError.class));

        given(anyMatch(Objects::nonNull))
                .whenDo((anyMatch) -> anyMatch.doAssertion(() -> Arrays.asList(null, null)))
                .then(throwsException(AssertionError.class));

        given(anyMatch((String s) -> s.length() <= 3))
                .whenDo((anyMatch) -> anyMatch.doAssertion(() -> Arrays.asList("12"/*shorter than 3*/, "1234")))
                .then(doesNotThrow());

        given(anyMatch(Objects::nonNull))
                .whenDo((anyMatch) -> anyMatch.doAssertion(() -> Arrays.asList(null, "not null")))
                .then(doesNotThrow());
    }
}
