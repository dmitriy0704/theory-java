package dev.folomkin.algos_datastructure.algos.arrays.sorting;


public class Code {

    // Метод для выполнения быстрой сортировки
    public static void quickSort(int[] arr, int low, int high) {
        if (low < high) {
            // Находим индекс разделителя
            int pivotIndex = partition(arr, low, high);
            // Рекурсивно сортируем элементы до и после разделителя
            quickSort(arr, low, pivotIndex - 1);
            quickSort(arr, pivotIndex + 1, high);
        }
    }

    // Метод для разделения массива и нахождения индекса разделителя
    public static int partition(int[] arr, int low, int high) {
        int pivot = arr[high]; // Выбираем последний элемент в качестве опорного
        int i = (low - 1); // Индекс меньшего элемента
        for (int j = low; j < high; j++) {
            // Если текущий элемент меньше или равен опорному
            if (arr[j] <= pivot) {
                i++;
                // Меняем местами элементы
                swap(arr, i, j);
            }
        }
        // Меняем местами опорный элемент с элементом на позиции i + 1
        swap(arr, i + 1, high);
        return i + 1; // Возвращаем индекс разделителя
    }

    // Метод для обмена элементов массива
    public static void swap(int[] arr, int i, int j) {
        int temp = arr[i];
        arr[i] = arr[j];
        arr[j] = temp;
    }

    public static void main(String[] args) {
        int[] array = {10, 7, 8, 9, 1, 5};
        System.out.println("Исходный массив:");
        printArray(array);

        quickSort(array, 0, array.length - 1);

        System.out.println("Отсортированный массив:");
        printArray(array);
    }

    // Метод для вывода массива на экран
    private static void printArray(int[] array) {
        for (int value : array) {
            System.out.print(value + " ");
        }
        System.out.println();
    }
}