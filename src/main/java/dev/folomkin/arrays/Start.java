package dev.folomkin.arrays;

import java.util.Arrays;

public class Start {
    public static void main(String[] args) {

        int[] array = {1, 2, 3, 4, 5, 6, 7, 8, 9, 10};

        int i = Arrays.binarySearch(array, 4);
        System.out.println(i);

    }
}
