package dev.folomkin.collection.code;

import java.util.*;

class MyComparator implements Comparator<String> {

    @Override
    public int compare(String o1, String o2) {
        return o1.compareToIgnoreCase(o2);
    }
}

public class Code {
    public static void main(String[] args) {

        MyComparator comparator = new MyComparator();

        List<Integer> list = new LinkedList<>();
        list.add(-8);
        list.add(20);
        list.add(-20);
        list.add(8);

        // Создать компаратор с обратным порядком.
        Comparator<Integer> r = Collections.reverseOrder();

        // Сортировать список с использованием созданного компаратора.
        Collections.sort(list, r);

        System.out.print("Список отсортирован в обратном порядке: ");
        for (int i : list) {
            System.out.print(i + " ");
        }

        // Тасовать список.
        Collections.shuffle(list);

        // Отобразить рандомизированный список.
        System.out.print("Список перетасован: ");
        for (int i : list) {
            System.out.print(i + " ");
        }
        System.out.println();
        System.out.println(" Haимeньшee значение: " + Collections.min(list));
        System.out.println("Haибoльшee значение: " + Collections.max(list));


    }
}
