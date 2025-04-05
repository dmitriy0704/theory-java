package dev.folomkin.algos_datastructure.algos.arrays.sorting;


public class Code {
    //-> Метод для сортировки массива с использованием сортировки вставками
    public static void insertionSort(int[] array) {
        // Проходим по всем элементам массива начиная со второго
        for (int i = 1; i < array.length; i++) {
            int key = array[i]; // Текущий элемент для вставки
            int j = i - 1;

            // Сдвигаем элементы массива, которые больше ключа, на одну позицию вправо
            while (j >= 0 && array[j] > key) {
                array[j + 1] = array[j];
                j--;
            }
            // Вставляем ключ на его правильное место
            array[j + 1] = key;
        }
    }

    //-> Метод для вывода массива на экран
    public static void printArray(int[] array) {
        for (int value : array) {
            System.out.print(value + " ");
        }
        System.out.println();
    }

    //-> Главный метод для тестирования сортировки вставками
    public static void main(String[] args) {
        int[] array = {12, 11, 13, 5, 6, 4, 10, 7, 8};
        System.out.println("Исходный массив:");
        printArray(array);

        insertionSort(array);

        System.out.println("Отсортированный массив:");
        printArray(array);
    }
}