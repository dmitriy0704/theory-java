package dev.folomkin.testing.tdd;

import dev.folomkin.testing.tdd.TicketRevenue;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class TicketRevenueTest {
    private TicketRevenue venueRevenue;
    private BigDecimal expectedRevenue;

    @BeforeEach
    public void setUp() {
        venueRevenue = new TicketRevenue();
    }

    @DisplayName("Продано отрицательное количество билетов")
    @Test
    public void failIfLessThanZeroTicketsAreSold() {
        assertThrows(IllegalArgumentException.class, () ->
                venueRevenue.estimateTotalRevenue(-1));
    }

    @DisplayName("Продано 0 билетов")
    @Test
    public void zeroSalesEqualsZeroRevenue() {
        assertEquals(BigDecimal.ZERO,
                venueRevenue.estimateTotalRevenue(0));
    }

    @DisplayName("Продан 1 билет")
    @Test
    public void oneTicketSoldIsThirtyInRevenue() {
        expectedRevenue = new BigDecimal("30");
        assertEquals(expectedRevenue, venueRevenue.estimateTotalRevenue(1));
    }

    @DisplayName("Продано N билетов")
    @Test
    public void tenTicketsSoldIsThreeHundredInRevenue() {
        expectedRevenue = new BigDecimal("300");
        assertEquals(expectedRevenue, venueRevenue.estimateTotalRevenue(10));
    }

    @DisplayName("Продано больше 100 билетов")
    @Test
    public void failIfMoreThanOneHundredTicketsAreSold() {
        assertThrows(IllegalArgumentException.class, () ->
                venueRevenue.estimateTotalRevenue(101));
    }
}
