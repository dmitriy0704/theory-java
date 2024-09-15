package dev.folomkin.core.collections_streamapi.collections;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;


class OrderType implements Iterable<String> {
    private int orderId;
    private List<String> currencyNames = new ArrayList<>();

    public OrderType(int orderId) {
        this.orderId = orderId;
    }

    public List<String> getCurrencyNames() {
        return List.copyOf(currencyNames);
    }

    public boolean add(String e) {
       return currencyNames.add(e);
    }

    @Override
    public Iterator<String> iterator() {
        return currencyNames.iterator();
    }
}

public class Code {
    public static void main(String[] args) {
        List<Integer> list = new ArrayList<>();
        list.add(1);
        list.add(2);
        list.add(3);
        list.add(4);

//        Integer y = 3;
//        Iterator<Integer> iterator = list.iterator();
//        while (iterator.hasNext()) {
//            System.out.println(iterator.next());
//        }
//        // или
//        for (Integer i : list) {
//            System.out.println(i);
//        }
//        System.out.println(list);

        list.removeIf(item -> item == 3);
        ListIterator<Integer> iterator = list.listIterator();


    }
}