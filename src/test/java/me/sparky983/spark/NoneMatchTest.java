package me.sparky983.spark;

import static me.sparky983.spark.Assertion.*;
import static me.sparky983.spark.Given.given;

import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.Objects;
import java.util.function.Predicate;

class NoneMatchTest {

    @Test
    void testNoneMatchWhenPredicateNullThrows() {

        given((Predicate<?>) null)
                .when(Assertion::noneMatch)
                .then(throwsException(NullPointerException.class));
    }

    @Test
    void testNoneMatch() {

        given(noneMatch((String s) -> s.length() >= 3))
                .whenDo((noneMatch) -> noneMatch.doAssertion(() -> Arrays.asList("123", "12345")))
                .then(throwsException(AssertionError.class));

        given(noneMatch(Objects::nonNull))
                .whenDo((noneMatch) -> noneMatch.doAssertion(() -> Arrays.asList("not null", null)))
                .then(throwsException(AssertionError.class));

        given(noneMatch((String s) -> s.length() >= 3))
                .whenDo((noneMatch) -> noneMatch.doAssertion(() -> Arrays.asList("1", "12")))
                .then(doesNotThrow());

        given(noneMatch(Objects::isNull))
                .whenDo((noneMatch) -> noneMatch.doAssertion(() -> Arrays.asList("not null", "also not null")))
                .then(doesNotThrow());
    }
}
