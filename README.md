<h1 align="center">Spark Test</h1>

<p align="center">
    <img src="https://img.shields.io/github/v/tag/sparky983/spark-test?display_name=release&label=Release&style=flat-square&color=e672ff&labelColor=000d26" alt="Version">
    <a href="https://app.codecov.io/gh/sparky983/spark-test">
        <img src="https://img.shields.io/codecov/c/github/sparky983/spark-test?label=Coverage&style=flat-square&color=e672ff&labelColor=000d26" alt="Coverage">
    </a>
    <img src="https://img.shields.io/github/commit-activity/m/sparky983/spark-test?label=Commits&style=flat-square&color=e672ff&labelColor=000d26" alt="Commit activity">
    <img src="https://img.shields.io/github/license/sparky983/spark-test?label=License&style=flat-square&color=e672ff&labelColor=000d26" alt="License Apache">
</p>

<p align="center">A fluent testing and assertions library.</p>

## Example Tests

```java
// static imports
given("some epic string")
        .noop()
        .then(startsWith("some"))
        .and(contains("epic"));

// standard
Given.given("some epic string")
        .noop()
        .then(Assertion.startsWith("some"))
        .and(Assertion.contains("epic"));
```

```java
// with static imports
when(() -> Objects.requireNonNull(null))
        .then(throwsException(NullPointerException.class));

// standard
When.when(() -> Objects.requireNonNull(null))
        .then(Assertion.throwsException(NullPointerException.class))
```

## Credit

This project was inspired by [AssertJ](https://assertj.github.io/doc/).
