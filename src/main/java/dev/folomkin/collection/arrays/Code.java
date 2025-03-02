package dev.folomkin.collection.arrays;

public class Code {
    public static void main(String[] args) {
        int[] arr = new int[]{1, 2, 3, 4, 5, 6, 7, 8, 9};

        for (int i = 0, j = 1; i < arr.length; i++, j++) {
            arr[i] = j;
        }

        for (int i = 0; i < arr.length; i++) {
            System.out.println(arr[i]);
        }
    }
}
