package dev.folomkin.testing.mockito;

public interface OrderService {
    void placeOrder(Order order);

    int getOrderCount();
}

class Order {
    private String name;
    private int price;

    public Order(String name, int price) {
        this.name = name;
        this.price = price;
    }
}