package me.sparky983.spark;

import org.junit.jupiter.api.Test;

import static me.sparky983.spark.Assertion.doesNotThrow;
import static me.sparky983.spark.Assertion.endsWith;
import static me.sparky983.spark.Assertion.throwsException;
import static me.sparky983.spark.Given.given;
import static me.sparky983.spark.Given.givenNull;

class EndsWithTest {

    @Test
    void testEndsWithWhenPrefixNullThrows() {

        givenNull(String.class)
                .when(Assertion::endsWith)
                .then(throwsException(NullPointerException.class));
    }

    @Test
    void testEndsWith() {

        given(endsWith("suffix"))
                .whenDo((endsWith) -> endsWith.doAssertion(() -> "ends with the suffix"))
                .then(doesNotThrow());

        given(endsWith("suffix"))
                .whenDo((endsWith) -> endsWith.doAssertion(
                        () -> "does not end with the suffix <not suffix>"))
                .then(throwsException(AssertionError.class));

        given(endsWith("suffix"))
                .whenDo((endsWith) -> endsWith.doAssertion(() -> null))
                .then(throwsException(AssertionError.class));
    }
}
