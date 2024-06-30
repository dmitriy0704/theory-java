package dev.folomkin.oop.optionalpkg.example;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class OrderAction {
    public Optional<Order> findById(List<Order> orders, long id) {

        Order order = null;
        List<Order> result = orders.stream()
                .filter(o -> id == o.getOrderId()).toList();

        //        if (result.size() != 0) {
        //            order = orders.get(0);
        //        }

        Optional<Order> optionalResult = result.size() != 0 ? Optional.of(result.get(0)) : Optional.empty();

        return optionalResult;
    }
}

class Order {
    private Long orderId;

    public Order() {
    }

    public Order(Long orderId) {
        this.orderId = orderId;
    }

    public Long getOrderId() {
        return orderId;
    }

    public void setOrderId(Long orderId) {
        this.orderId = orderId;
    }
}