package dev.folomkin.algos_datastructure.algos.arrays.search;

import java.util.Arrays;

public class Code {

    // Рекурсивный метод для выполнения бинарного поиска
    public static int binarySearch(int[] array, int target, int left, int right) {
        // Базовый случай: если границы пересеклись, элемент не найден
        if (left > right) {
            return -1;
        }

        // Вычисляем средний индекс
        int mid = left + (right - left) / 2;

        // Проверяем, находится ли целевое значение в середине
        if (array[mid] == target) {
            return mid; // Возвращаем индекс найденного элемента
        }

        // Если целевое значение больше, ищем в правой половине
        if (array[mid] < target) {
            return binarySearch(array, target, mid + 1, right);
        } else { // Если целевое значение меньше, ищем в левой половине
            return binarySearch(array, target, left, mid - 1);
        }
    }

    public static void main(String[] args) {
        int[] array = {2, 3, 4, 10, 40, 5, 17, 12};
        int target = 17;
        Arrays.sort(array);

        int result = binarySearch(array, target, 0, array.length - 1);
        if (result == -1) {
            System.out.println("Элемент не найден");
        } else {
            System.out.println("Элемент найден на индексе: " + result);
        }
    }
}
