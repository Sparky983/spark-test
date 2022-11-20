package me.sparky983.spark;

import org.junit.jupiter.api.Test;

import static me.sparky983.spark.Assertion.doesNotThrow;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;

class DoesNotThrowTest {

    @Test
    void testDoesNotThrow() {

        assertDoesNotThrow(() ->
                doesNotThrow()
                        .doAssertion(() -> {
                            // no-op
                            return null;
                        }));

        assertThrows(AssertionError.class, () ->
                doesNotThrow()
                        .doAssertion(() -> {
                            throw new NullPointerException();
                        }));
    }
}
