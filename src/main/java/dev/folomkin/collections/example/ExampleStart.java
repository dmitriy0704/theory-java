package dev.folomkin.collections.example;

import java.util.*;

public class ExampleStart {
    public static void main(String[] args) {
        // Сортировка и поиск "массива" строк Strings
        String[] array = {"Hello", "hello", "hi", "HI"};

        // Используем Comparable из String
        Arrays.sort(array);
        System.out.println(Arrays.toString(array));

        //Используем бинарный поиск,
        // для этого массив должен быть отсортирован.

        System.out.println(Arrays.binarySearch(array, "Hello"));
        System.out.println(Arrays.binarySearch(array, "HELLO"));

        // Сортировка и поиск в списке List из целых чисел
        List<Integer> list = new ArrayList<>();
        list.add(2);
        list.add(1);
        list.add(4);
        list.add(3);

        Collections.sort(list); //Используем Comparable для класса Integer
        System.out.println(list);



    }
}
