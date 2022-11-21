package me.sparky983.spark;

import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.util.Arrays;

import static me.sparky983.spark.Assertion.contains;
import static me.sparky983.spark.Assertion.doesNotThrow;
import static me.sparky983.spark.Assertion.throwsException;
import static me.sparky983.spark.Given.given;
import static me.sparky983.spark.Given.givenNull;

class ContainsTest {

    @Nested
    class StringContains {

        @Test
        void testContainsWhenPrefixNullThrows() {

            givenNull(String.class)
                    .when(Assertion::contains)
                    .then(throwsException(NullPointerException.class));
        }

        @Test
        void testContains() {

            given(contains("sub"))
                    .whenDo((contains) -> contains.doAssertion(() -> "does not contain the s_u_b"))
                    .then(throwsException(AssertionError.class));

            given(contains("sub"))
                    .whenDo((contains) -> contains.doAssertion(() -> null))
                    .then(throwsException(AssertionError.class));

            given(contains("sub"))
                    .whenDo((contains) -> contains.doAssertion(() -> "contains the substring"))
                    .then(doesNotThrow());

        }
    }

    @Nested
    class CollectionContains {

        @Test
        void testContainsWhenPrefixNullThrows() {

            givenNull()
                    .when((nil) -> Assertion.contains((Object[]) null))
                    .then(throwsException(NullPointerException.class));
        }

        @Test
        void testContains() {

            given(contains(1))
                    .whenDo((contains) -> contains.doAssertion(() -> Arrays.asList(1, 2, 3)))
                    .then(doesNotThrow());

            given(contains(1))
                    .whenDo((contains) -> contains.doAssertion(() -> null))
                    .then(throwsException(AssertionError.class));

            given(contains(2))
                    .whenDo((contains) -> contains.doAssertion(() -> Arrays.asList(1, 3)))
                    .then(throwsException(AssertionError.class));
        }
    }
}
