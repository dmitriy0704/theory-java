package dev.folomkin.algos;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public class ExampleCode {
    public static void main(String[] args) {
        int[] intArr = {30, 20, 5, 12, 55};

        //Сортировка
        Arrays.sort(intArr);
        System.out.println("Отсортированный массив:");
        for (int number : intArr) {
            System.out.println(number + " ");
        }

        //Значение для поиска:
        int searchVal = 12;
        int retVal = Arrays.binarySearch(intArr, searchVal);
        System.out.println("Индекс элемента: " + retVal);
    }
}
