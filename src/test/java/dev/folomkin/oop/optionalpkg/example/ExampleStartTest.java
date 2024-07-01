package dev.folomkin.oop.optionalpkg.example;

import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

class ExampleStartTest {
    @Test()
    public void givenNonNull_whenCreatesNonNullable_thenCorrect() {
        String name = "Wine";
        Optional<String> opt = Optional.of(name);
        assertTrue(opt.isPresent());
    }
}