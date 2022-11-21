package me.sparky983.spark;

import static me.sparky983.spark.Assertion.allMatch;
import static me.sparky983.spark.Assertion.doesNotThrow;
import static me.sparky983.spark.Assertion.throwsException;
import static me.sparky983.spark.Given.given;

import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.Collections;
import java.util.function.Predicate;

class AllMatchTest {

    @Test
    void testAllMatchWhenPredicateNullThrows() {

        given((Predicate<?>) null)
                .when(Assertion::allMatch)
                .then(throwsException(NullPointerException.class));
    }

    @Test
    void testAllMatch() {

        given(allMatch((String s) -> s.length() >= 3))
                .whenDo((allMatch) -> allMatch.doAssertion(() -> Arrays.asList("12", "1234")))
                .then(throwsException(AssertionError.class));

        given(allMatch((String s) -> false))
                .whenDo((allMatch) -> allMatch.doAssertion(() -> Arrays.asList("1", "2")))
                .then(throwsException(AssertionError.class));

        given(allMatch((String s) -> s.length() <= 3))
                .whenDo((allMatch) -> allMatch.doAssertion(() -> Arrays.asList("1", "12", "123")))
                .then(doesNotThrow());

        given(allMatch((s) -> true))
                .whenDo((allMatch) -> allMatch.doAssertion(() -> Arrays.asList(null, null)))
                .then(doesNotThrow());

        given(allMatch((s) -> false))
                .whenDo((allMatch) -> allMatch.doAssertion(Collections::emptyList))
                .then(doesNotThrow());
    }
}
