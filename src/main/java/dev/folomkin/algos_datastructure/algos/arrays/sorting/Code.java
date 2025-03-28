package dev.folomkin.algos_datastructure.algos.arrays.sorting;

import java.util.Arrays;

public class Code {
    public static int[] bubbleSort(int[] array) {
        int n = array.length;
        boolean swapped;

        for (int i = 0; i < n - 1; i++) {
            swapped = false;
            for (int j = 0; j < n - 1 - i; j++) {
                if (array[j] > array[j + 1]) {
                    swapped = true;
                    int temp = array[j];
                    array[j] = array[j + 1];
                    array[j + 1] = temp;
                }
            }
            if (!swapped) {
                break;
            }
        }
        return array;
    }

    public static void main(String[] args) {
        int[] array = {64, 34, 25, 12, 22, 11, 90};
        System.out.println("Исходный массив:");
        System.out.println(Arrays.toString(array));
        bubbleSort(array);
        System.out.println("Отсортированный массив:");
        int[] arr = bubbleSort(array);
        System.out.println(Arrays.toString(arr));
    }
}
