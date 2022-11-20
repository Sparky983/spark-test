package me.sparky983.spark;

import org.junit.jupiter.api.Test;

import static me.sparky983.spark.Assertion.contains;
import static me.sparky983.spark.Assertion.doesNotThrow;
import static me.sparky983.spark.Assertion.throwsException;
import static me.sparky983.spark.Given.given;
import static me.sparky983.spark.Given.givenNull;

class ContainsTest {


    @Test
    void testContainsWhenPrefixNullThrows() {

        givenNull(String.class)
                .when(Assertion::contains)
                .then(throwsException(NullPointerException.class));
    }

    @Test
    void testContains() {

        given(contains("sub"))
                .whenDo((contains) -> contains.doAssertion(() -> "contains the substring"))
                .then(doesNotThrow());

        given(contains("sub"))
                .whenDo((contains) -> contains.doAssertion(() -> "does not contain the s_u_b"))
                .then(throwsException(AssertionError.class));

        given(contains("sub"))
                .whenDo((contains) -> contains.doAssertion(() -> null))
                .then(throwsException(AssertionError.class));
    }
}
