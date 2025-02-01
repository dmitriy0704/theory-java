package dev.folomkin.testing.tdd.stub;

import java.math.BigDecimal;

public class Ticket {

    public static final int BASIC_TICKET_PRICE = 30; // Цена по умолчанию
    private static final BigDecimal DISCOUNT_RATE = new BigDecimal("0.9"); // Скидка по умолчанию


    private final BigDecimal price;
    private final String clientName;

    public Ticket(String clientName) {
        this.clientName = clientName;
        price = new BigDecimal(BASIC_TICKET_PRICE);
    }

    public Ticket(String clientName, BigDecimal price) {
        this.clientName = clientName;
        this.price = price;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public BigDecimal getDiscountPrice() {
        return price.multiply(DISCOUNT_RATE);
    }

}
