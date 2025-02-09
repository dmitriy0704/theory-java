package dev.folomkin.testing.tdd;

import dev.folomkin.testing.tdd.stub.Price;
import dev.folomkin.testing.tdd.stub.Ticket;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

public class TicketTest {


    @Test
    public void tenPercentDiscount() {
        Price price = mock(Price.class);

        when(price.getInitialPrice())
                .thenReturn(new BigDecimal("10"));
//
//        Ticket ticket = new Ticket(price, new BigDecimal("0.9"));
//        assertEquals(new BigDecimal("9.0"), ticket.getDiscountPrice());
//        verify(price).getInitialPrice();

    }
}
