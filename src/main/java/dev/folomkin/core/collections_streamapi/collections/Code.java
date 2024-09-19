package dev.folomkin.core.collections_streamapi.collections;

import java.util.*;


class Order {

}

class Key {
    //  где класс Key содержит информацию об уникальном номере заказа и текущем статусе заказа.
    private int keyUnique;
    private boolean isProcessed;

    public Key(int keyUnique) {
        this.keyUnique = keyUnique;
    }

    public boolean isProcessed() {
        return isProcessed;
    }

    public void setProcessed(boolean processed) {
        isProcessed = processed;
    }
}

class CurrentOrders {
    private WeakHashMap<Key, Order> orders = new WeakHashMap<>();

    public Order put(Key key, Order value) {
        return orders.put(key, value);
    }

    public Order get(Object key) {
        return orders.get(key);
    }

    public int size() {
        return orders.size();
    }
}

public class Code {
    public static void main(String[] args) {
        Hashtable<String, Integer> table = new Hashtable<>();
        table.put("Jeans", 40); // adding a pair
        table.put("T-Shirt", 35);
        table.put("Gloves", 42);
        table.compute("Shoes", (k,v) -> 77); // adding a pair
        System.out.println(table);

        Enumeration<String> keys = table.keys();
        while (keys.hasMoreElements()) {
            System.out.println(keys.nextElement());
        }
        Enumeration<Integer> values = table.elements();
        while (values.hasMoreElements()) {
            System.out.println(values.nextElement());
        }
    }
}