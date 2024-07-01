package dev.folomkin.oop.optionalpkg.example;

import java.util.*;
import java.util.stream.Collectors;

public class OrderAction {
    public Optional<Order> findById(List<Order> orders, long id) {
        Optional<Order> optionalResult = orders.stream()
                .filter(o -> id == o.getOrderId()).findAny();
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

class OrderMain {
    public static void main(String[] args) {
        List<Order> orders = new ArrayList<>();
        orders.add(new Order(1L));
        orders.add(new Order(3L));
        orders.add(new Order(4L));
        OrderAction action = new OrderAction();
        Optional<Order> optionalOrder = action.findById(orders, 1L);

        if (optionalOrder.isPresent()) {
            System.out.println(optionalOrder.get());
        }

        Set<Order> set = new HashSet<>();
        optionalOrder.ifPresent(set::add);
        System.out.println(set);

    }
}