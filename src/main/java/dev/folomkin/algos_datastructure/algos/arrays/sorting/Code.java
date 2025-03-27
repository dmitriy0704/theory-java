package dev.folomkin.algos_datastructure.algos.arrays.sorting;

public class Code {
    // Метод для сортировки массива с использованием сортировки слиянием
    public static void mergeSort(int[] array) {
        if (array.length < 2) {
            return; // Если массив состоит из одного элемента, он уже отсортирован
        }

        int mid = array.length / 2; // Находим середину массива
        int[] left = new int[mid]; // Создаем левую половину
        int[] right = new int[array.length - mid]; // Создаем правую половину

        // Заполняем левую и правую половины
        for (int i = 0; i < mid; i++) {
            left[i] = array[i];
        }
        for (int i = mid; i < array.length; i++) {
            right[i - mid] = array[i];
        }

        // Рекурсивно сортируем обе половины
        mergeSort(left);
        mergeSort(right);

        // Сливаем отсортированные половины
        merge(array, left, right);
    }

    // Метод для слияния двух отсортированных массивов
    private static void merge(int[] array, int[] left, int[] right) {
        int i = 0, j = 0, k = 0;

        // Сравниваем элементы левой и правой половин и заполняем основной массив
        while (i < left.length && j < right.length) {
            if (left[i] <= right[j]) {
                array[k++] = left[i++];
            } else {
                array[k++] = right[j++];
            }
        }

        // Копируем оставшиеся элементы левой половины, если есть
        while (i < left.length) {
            array[k++] = left[i++];
        }

        // Копируем оставшиеся элементы правой половины, если есть
        while (j < right.length) {
            array[k++] = right[j++];
        }
    }

    // Метод для вывода массива на экран
    public static void printArray(int[] array) {
        for (int value : array) {
            System.out.print(value + " ");
        }
        System.out.println();
    }

    // Главный метод для тестирования сортировки слиянием
    public static void main(String[] args) {
        int[] array = {38, 27, 43, 3, 9, 82, 10}; // Исходный массив
        System.out.println("Исходный массив:");
        printArray(array);

        mergeSort(array); // Сортируем массив

        System.out.println("Отсортированный массив:");
        printArray(array); // Выводим отсортированный массив
    }
}
