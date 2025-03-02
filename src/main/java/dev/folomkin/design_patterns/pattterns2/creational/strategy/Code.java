package dev.folomkin.design_patterns.pattterns2.creational.strategy;

public class Code {
}


interface PaymentStrategy {
    void pay(int amount);
}

class CreditCardStrategy implements PaymentStrategy {
    public void pay(int amout) {
        System.out.println("Pay CreditCard" + amout);
    }
}

class PayPalStrategy implements PaymentStrategy {
    public void pay(int amout) {
        System.out.println("Pay PayPal" + amout);
    }
}

enum Payment {
    PAYPAL("paypal"),
    CREDITCARD("creditcard");

    private final String name;

    Payment(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}