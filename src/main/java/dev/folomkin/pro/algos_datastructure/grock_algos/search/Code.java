package dev.folomkin.pro.algos_datastructure.grock_algos.search;

// Binary search:

import java.util.Arrays;

public class Code {
    public static void main(String[] args) {
        int[] arrays = {2, 4, 3, 5, 56, 32, 54, 34, 23}; //-> [2, 3, 4, 5, 23, 32, 34, 43, 54, 56, 76]
        int item = 32; //-> Искомое число
        System.out.println(Arrays.toString(arrays));

        Arrays.sort(arrays);
        System.out.println(Arrays.toString(arrays));

        //-> Самое низкое и самое высокое значения массива
        int low = 0;
        int high = arrays.length - 1;

        //-> Дальше алгоритм проверяет средний элемент
        int mid = (low + high) / 2; //-> Если предполагаемое число нечетное - оно округляется в меньшую сторону
        int guess = arrays[mid]; //-> Предполагаемое число

        //-> Если предполагаемое число было слишком мало, то переменная low
        // обновляется соответственно:
        if (guess < item) {
            low = mid + 1;
        }

        //-> А если предполагаемое число было слишком велико, то обновляется переменная high.
        if (guess > item) {
            high = mid - 1;
        }

        System.out.println(Arrays.toString(arrays));
    }

    int binarySearch(int[] arrays, int target) {
        //-> В переменных low и high хранятся границы
        // той части списка, в которой выполняется поиск
        int low = 0;
        int high = arrays.length - 1;

        //-> Пока эта часть не сократится до одного элемента...
        while (low <= high) {
            //-> ...проверяем среднее значение
            int mid = (low + high) / 2;
            int guess = arrays[mid];

            if (guess == target) { //-> значение найдено
                return mid;
            }

            if (guess < target) { //-> мало, увеличиваем минимальное на  [среднее+1]
                low = mid + 1;
            }
            if (guess > target) { //-> много, уменьшаем максимальное на [среднее-1]
                high = mid - 1;
            }
        }
        return -1;
    }


}
