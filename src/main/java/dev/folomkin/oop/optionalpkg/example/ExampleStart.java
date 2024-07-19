package dev.folomkin.oop.optionalpkg.example;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

class Order {
    long id;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    String name;

    public Order(long id, String name) {
        this.id = id;
        this.name = name;
    }

    public String getName() {
        return name;
    }


    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "Order{" +
                "id=" + id +
                ", name='" + name + '\'' +
                '}';
    }
}

public class ExampleStart {
    public static void main(String[] args) {

        List<Order> orders = new ArrayList<>();
        orders.add(new Order(1L, "Alex"));
        orders.add(new Order(2L, "Bob"));
        orders.add(new Order(3L, "Charles"));

        Optional<Order> o = findById(orders, 4l);
        System.out.println(o);
    }

    public static Optional<Order> findById(List<Order> orders, long id) {
//        Order order = null;
        List<Order> result = orders
                .stream().filter(o -> id == o.getId())
                .toList();
//        if (result.size() != 0) {
//            order = result.get(0);
//        }

        Optional<Order> optionalOrder =
                result.size() != 0
                        ? Optional.of(result.get(0))
                        : Optional.empty();

        return optionalOrder;
    }
}
