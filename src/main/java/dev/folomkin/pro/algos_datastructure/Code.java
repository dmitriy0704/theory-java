package dev.folomkin.pro.algos_datastructure;

import java.util.Arrays;

public class Code {
    public static void main(String[] args) {
        int[] arr = {1, 12, 34, 24, 15, 6, 17, 8, 9};
        int[] arrResult = sort(arr);
        System.out.println(Arrays.toString(arrResult));
    }

     static int[] sort(int[] array) {
        //внешний цикл отвечает за номер прохода
        for (int i = 0; i < array.length - 1; i++) {
            //внутренний цикл - за перебор элементов в одном проходе
            for (int j = array.length - 1; j > i; j--) {
                if (array[j - 1] > array[j]) {
                    //переменная temp отвечает за обмен значений
                    int temp = array[j - 1];
                    array[j - 1] = array[j];
                    array[j] = temp;
                }
            }
        }
        return array;
    }
}