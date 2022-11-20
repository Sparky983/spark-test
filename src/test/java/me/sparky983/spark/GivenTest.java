package me.sparky983.spark;

import static me.sparky983.spark.Assertion.*;
import static me.sparky983.spark.Given.given;
import static me.sparky983.spark.Given.givenNull;

import org.junit.jupiter.api.Test;

class GivenTest {

    @Test
    void testGivenNullWhenClsNull() {

        given((Class<?>) null)
                .when(Given::givenNull)
                .then(throwsException(NullPointerException.class));
    }

    @Test
    void testWhenWhenWhenFunctionNull() {

        given(given(new Object()))
                .when((given) -> given.when(null))
                .then(throwsException(NullPointerException.class));

        given(givenNull())
                .when((given) -> given.when(null))
                .then(throwsException(NullPointerException.class));

        given(givenNull(Object.class))
                .when((given) -> given.when(null))
                .then(throwsException(NullPointerException.class));
    }

    @Test
    void testWhenDoWhenWhenConsumerNull() {

        given(given(new Object()))
                .when((given) -> given.whenDo(null))
                .then(throwsException(NullPointerException.class));

        given(givenNull())
                .when((given) -> given.whenDo(null))
                .then(throwsException(NullPointerException.class));

        given(givenNull(Object.class))
                .when((given) -> given.whenDo(null))
                .then(throwsException(NullPointerException.class));
    }

}
