package dev.folomkin.testing.tdd;

import java.math.BigDecimal;

public class TicketRevenue {

    private final static int TICKET_PRICE = 30;

    public BigDecimal estimateTotalRevenue(int numberOfTicketsSold)
            throws IllegalArgumentException {

        if (numberOfTicketsSold < 0 || numberOfTicketsSold > 100) {
            throw new IllegalArgumentException("Должно быть == 1..100");
        }

        // Продано N биллетов
        return new BigDecimal(TICKET_PRICE * numberOfTicketsSold);
    }
}
