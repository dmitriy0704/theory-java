package dev.folomkin.testing.junit;

import org.junit.jupiter.api.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static org.junit.jupiter.api.Assertions.*;

class CalculatorTest {


    @BeforeAll
    public static void init() {
        System.out.println("Before All init() method called");
    }

    @BeforeEach
    void test() {
        System.out.println("Before Each init() method called");
    }

    @Test
    void sum() {
        System.out.println("Sum of integers in calculator");
        assertEquals(4, Calculator.add(2, 2));
    }

    @Test
    void sum2() {
        System.out.println("Sum2 of integers in calculator");
        assertEquals(5, Calculator.add(2, 3));
    }

    @DisplayName("Add operation test")
    @RepeatedTest(value = 5, name = "{displayName} - repetition {currentRepetition} of {totalRepetitions}")
    void addNumber(TestInfo testInfo, RepetitionInfo repetitionInfo) {
        System.out.println("Running test -> " + repetitionInfo.getCurrentRepetition());
        assertEquals(2, Calculator.add(1, 1), "1 + 1 should equal 2");
    }


    @AfterAll
    public static void cleanUp() {
        System.out.println("After All cleanUp() method called");
    }

    @AfterEach
    public void cleanUpEach() {
        System.out.println("After Each cleanUpEach() method called");
    }

    public boolean isPalindrome(String s) {
        return s != null && !s.isEmpty();
    }

    @ParameterizedTest(name = "{index} - {0} is a palindrome")
    @ValueSource(strings = {"12321", "pop"})
    void testPalindrome(String word) {
        assertTrue(isPalindrome(word));
    }
}