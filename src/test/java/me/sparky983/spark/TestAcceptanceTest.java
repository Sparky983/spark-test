package me.sparky983.spark;

import org.junit.jupiter.api.Test;

import java.util.Objects;

import static me.sparky983.spark.Assertion.contains;
import static me.sparky983.spark.Assertion.endsWith;
import static me.sparky983.spark.Assertion.isEqualTo;
import static me.sparky983.spark.Assertion.isNotNull;
import static me.sparky983.spark.Assertion.startsWith;
import static me.sparky983.spark.Assertion.throwsException;
import static me.sparky983.spark.Given.given;
import static me.sparky983.spark.Given.givenNull;
import static me.sparky983.spark.When.when;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;

class TestAcceptanceTest {

    @Test
    void testFailingTestThrows() {

        assertThrows(AssertionError.class, () ->
                given(null)
                        .noop()
                        .then(isNotNull())
        );

        assertThrows(AssertionError.class, () ->
                given("not null")
                        .whenDo(Objects::requireNonNull)
                        .then(throwsException(NullPointerException.class))
        );

        assertThrows(AssertionError.class, () ->
                given("not null")
                        .when(Objects::requireNonNull)
                        .then(throwsException(NullPointerException.class))
        );

        assertThrows(AssertionError.class, () ->
                givenNull()
                        .noop()
                        .then(isNotNull())
        );

        assertThrows(AssertionError.class, () ->
                when(() -> null)
                        .then(isNotNull())
        );

        assertThrows(AssertionError.class, () ->
                givenNull()
                        .noop()
                        .then(isEqualTo(null))
                        .and(isNotNull()) // fails here
        );
    }

    @Test
    void testSucceedingTestDoesNotThrow() {

        assertDoesNotThrow(() ->
                given(null)
                        .noop()
                        .then(isEqualTo(null))
        );

        assertDoesNotThrow(() ->
                given(null)
                        .whenDo(Objects::requireNonNull)
                        .then(throwsException(NullPointerException.class))
        );

        assertDoesNotThrow(() ->
                given(null)
                        .when(Objects::requireNonNull)
                        .then(throwsException(NullPointerException.class))
        );

        assertDoesNotThrow(() ->
                givenNull()
                        .noop()
                        .then(isEqualTo(null))
        );

        assertDoesNotThrow(() ->
                when(() -> null)
                        .then(isEqualTo(null))
        );

        assertDoesNotThrow(() ->
                given("some epic string")
                        .noop()
                        .then(startsWith("some"))
                        .and(contains("epic"))
                        .and(endsWith("string"))
                        .and(isEqualTo(String::length, 16))
        );
    }
}
