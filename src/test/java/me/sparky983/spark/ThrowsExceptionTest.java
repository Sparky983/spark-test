package me.sparky983.spark;

import org.junit.jupiter.api.Test;

import static me.sparky983.spark.Assertion.throwsException;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;

class ThrowsExceptionTest {

    @Test
    void testThrowsException() {

        assertThrows(AssertionError.class, () ->
                throwsException(NullPointerException.class)
                        .doAssertion(() -> {
                            throw new RuntimeException();
                        }));

        assertThrows(AssertionError.class, () ->
                throwsException(NullPointerException.class)
                        .doAssertion(() -> {
                            // no-op
                            return null;
                        }));

        assertDoesNotThrow(() ->
                throwsException(NullPointerException.class)
                        .doAssertion(() -> {
                            throw new NullPointerException();
                        }));

        assertThrows(AssertionError.class, () ->
            throwsException(AssertionError.class)
                    .doAssertion(() -> null));
    }

    @Test
    void testThrowsExceptionWithMessage() {

        assertThrows(AssertionError.class, () ->
                throwsException(NullPointerException.class, "message")
                        .doAssertion(() -> {
                            throw new RuntimeException("message");
                        }));

        assertThrows(AssertionError.class, () ->
                throwsException(NullPointerException.class, "message")
                        .doAssertion(() -> {
                            throw new NullPointerException();
                        }));

        assertThrows(AssertionError.class, () ->
                throwsException(NullPointerException.class, "message")
                        .doAssertion(() -> {
                            throw new NullPointerException("incorrect message");
                        }));

        assertThrows(AssertionError.class, () ->
                throwsException(NullPointerException.class, "message")
                        .doAssertion(() -> null));

        assertDoesNotThrow(() ->
                throwsException(NullPointerException.class, "message")
                        .doAssertion(() -> {
                            throw new NullPointerException("message");
                        }));

        assertDoesNotThrow(() ->
                throwsException(RuntimeException.class, "message")
                        .doAssertion(() -> {
                            throw new RuntimeException("message", new Exception());
                        }));
    }
}
