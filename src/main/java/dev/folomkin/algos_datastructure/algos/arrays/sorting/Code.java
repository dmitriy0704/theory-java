package dev.folomkin.algos_datastructure.algos.arrays.sorting;


public class Code {
    public static void quickSort(int[] array, int low, int high) {
        if (low < high) {
            int pivotIndex = partition(array, low, high);
            quickSort(array, low, pivotIndex - 1);  // Сортируем левую часть
            quickSort(array, pivotIndex + 1, high); // Сортируем правую часть
        }
    }

    private static int partition(int[] array, int low, int high) {
        // Выбор медианы трех
        int mid = low + (high - low) / 2;
        int pivot = medianOfThree(array[low], array[mid], array[high]);

        // Перемещение опорного элемента в конец
        if (pivot == array[low]) {
            swap(array, low, high);
        } else if (pivot == array[mid]) {
            swap(array, mid, high);
        }

        pivot = array[high]; // Теперь опорный элемент в конце

        int i = low - 1;
        for (int j = low; j < high; j++) {
            if (array[j] < pivot) {
                i++;
                swap(array, i, j);
            }
        }

        // Перемещаем опорный элемент на его правильное место
        swap(array, i + 1, high);
        return i + 1;
    }

    private static int medianOfThree(int a, int b, int c) {
        if ((a > b) ^ (a > c)) return a; // a является медианой
        else if ((b > a) ^ (b > c)) return b; // b является медианой
        else return c; // c является медианой
    }

    private static void swap(int[] array, int i, int j) {
        int temp = array[i];
        array[i] = array[j];
        array[j] = temp;
    }

    public static void main(String[] args) {
        int[] array = {3, 6, 8, 10, 1, 2, 1};

        System.out.println("Исходный массив:");
        for (int num : array) {
            System.out.print(num + " ");
        }

        quickSort(array, 0, array.length - 1);

        System.out.println("\nОтсортированный массив:");
        for (int num : array) {
            System.out.print(num + " ");
        }
    }
}