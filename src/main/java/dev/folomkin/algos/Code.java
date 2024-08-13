package dev.folomkin.algos;

import java.util.Arrays;

public class Code {
    public static void main(String[] args) {
        int[] testArr = new int[]{6, 3, 8, 2, 6, 9, 4, 11, 1};
        bubbleSort(testArr);
        System.out.println(Arrays.toString(testArr));

    }

    public static void bubbleSort(int[] arr) {
        int tmp = 0, k = arr.length - 2;
        boolean is_swap = false;
        for (int i = 0; i <= k; i++) {
            is_swap = false;
            for (int j = k; j >= i; j--) {
                if (arr[j] > arr[j + 1]) {
                    tmp = arr[j + 1];
                    arr[j + 1] = arr[j];
                    arr[j] = tmp;
                    is_swap = true;
                }
            }

            // Если перестановок не было, то выходим
            if (is_swap == false) break;
        }
    }
}