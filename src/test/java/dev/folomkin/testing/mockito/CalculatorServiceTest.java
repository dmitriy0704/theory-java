package dev.folomkin.testing.mockito;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class CalculatorServiceTest {

    @Test
    public void whenThenReturnExample() {
        CalculatorService calculatorService = mock(CalculatorService.class);

        doNothing().when(calculatorService).printSum(10.0, 20.0);
        calculatorService.printSum(10.0, 20.0);

        verify(calculatorService).printSum(10.0, 20.0);

    }

}