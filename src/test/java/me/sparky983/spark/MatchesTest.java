package me.sparky983.spark;

import org.junit.jupiter.api.Test;

import java.util.regex.Pattern;

import static me.sparky983.spark.Assertion.doesNotThrow;
import static me.sparky983.spark.Assertion.matches;
import static me.sparky983.spark.Assertion.throwsException;
import static me.sparky983.spark.Given.given;
import static me.sparky983.spark.Given.givenNull;

class MatchesTest {

    @Test
    void testMatchesWhenRegexNull() {

        givenNull(Pattern.class)
                .when(Assertion::matches)
                .then(throwsException(NullPointerException.class));

        givenNull(String.class)
                .when(Assertion::matches)
                .then(throwsException(NullPointerException.class));
    }

    @Test
    void testMatches() {

        given(matches(Pattern.compile("[A-Za-z]+")))
                .whenDo((matches) -> matches.doAssertion(() -> "This string doesn't match"))
                .then(throwsException(AssertionError.class));

        given(matches("[A-Za-z]+"))
                .whenDo((matches) -> matches.doAssertion(() -> "This string doesn't match"))
                .then(throwsException(AssertionError.class));

        given(matches(Pattern.compile("[A-Za-z]+")))
                .whenDo((matches) -> matches.doAssertion(() -> null))
                .then(throwsException(AssertionError.class));

        given(matches("[A-Za-z]+"))
                .whenDo((matches) -> matches.doAssertion(() -> null))
                .then(throwsException(AssertionError.class));

        given(matches(Pattern.compile("[A-Za-z]+")))
                .whenDo((matches) -> matches.doAssertion(() -> "ThisStringMatches"))
                .then(doesNotThrow());

        given(matches("[A-Za-z]+"))
                .whenDo((matches) -> matches.doAssertion(() -> "ThisStringMatches"))
                .then(doesNotThrow());
    }
}
