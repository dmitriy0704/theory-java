package dev.folomkin.collection.arrays;

public class Code {
    public static void main(String[] args) {
        int[] arr = new int[]{1, 2, 3, 4, 5, 6, 7, 8, 9};

        for (int i = 0, j = 1; i < arr.length; i++, j++) {
            arr[i] = j;
        }

        // Значения в прямом порядке
        for (int i = 0; i < arr.length; i++) {
            System.out.println(arr[i]);
        }
        System.out.println("----------------------");
        //Значения в обратном порядке
      for(int i = arr.length - 1; i >= 0; i--) {
          System.out.println(arr[i]);
      }
    }
}
