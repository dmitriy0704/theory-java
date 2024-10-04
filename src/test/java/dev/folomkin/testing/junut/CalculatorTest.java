package dev.folomkin.testing.junut;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class CalculatorTest {

    @Test
    void add() {
        Calculator calc = new Calculator();
        int result = calc.add(2, 3);
        assertEquals(5, result);
    }

    @Test
    void sub() {
        Calculator calc = new Calculator();
        int result = calc.sub(10, 10);
        assertEquals(0, result);
    }

    @Test
    void multiply() {
        Calculator calc = new Calculator();
        int result = calc.multiply(-5, -3);
        assertEquals(15, result);
    }

    @Test
    void divide() {
        Calculator calc = new Calculator();
        int result = calc.divide(2, 3);
        assertEquals(0, result);
    }
}