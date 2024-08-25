package dev.folomkin.oop.optionalpkg.example;

import org.junit.jupiter.api.Test;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

class CodeTest {
    @Test
    public void givenNonNull_whenCreatesNonNullable_thenCorrect() {
        String name = null;
        String opt = Optional.ofNullable(name).orElse("Dmitriy");
        assertEquals("Dmitriy", opt);

    }
}