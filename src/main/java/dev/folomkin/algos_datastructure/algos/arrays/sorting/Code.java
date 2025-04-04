package dev.folomkin.algos_datastructure.algos.arrays.sorting;


public class Code {

    // Метод для выполнения быстрой сортировки
    public static void quickSort(int[] array, int low, int high) {
        if (low < high) {
            // Находим индекс опорного элемента и разбиваем массив
            int pivotIndex = partition(array, low, high);
            // Рекурсивно сортируем подмассивы
            quickSort(array, low, pivotIndex - 1);
            quickSort(array, pivotIndex + 1, high);
        }
    }

    // Метод для разбиения массива и нахождения индекса опорного элемента
    private static int partition(int[] array, int low, int high) {
        // Опорный элемент - последний элемент массива
        int pivot = array[high];
        int i = low - 1; // Индекс меньшего элемента

        for (int j = low; j < high; j++) {
            // Если текущий элемент меньше или равен опорному
            if (array[j] <= pivot) {
                i++; // Увеличиваем индекс меньшего элемента
                swap(array, i, j); // Меняем местами элементы
            }
        }
        // Меняем местами опорный элемент с элементом после последнего меньшего
        swap(array, i + 1, high);
        return i + 1; // Возвращаем индекс опорного элемента
    }

    // Метод для обмена двух элементов массива
    private static void swap(int[] array, int i, int j) {
        int temp = array[i];
        array[i] = array[j];
        array[j] = temp;
    }

    // Главный метод для тестирования сортировки
    public static void main(String[] args) {
        int[] array = {7, 2, 1, 8, 6, 3, 5, 4};
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