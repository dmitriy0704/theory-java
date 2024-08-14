package dev.folomkin.algos;

public class Code {
    public static void main(String[] args) {
        int[] testArr = new int[]{6, 3, 8, 8, 6, 9, 4, 11, 1};
        sortBySelect(testArr);
        for (int i : testArr) {
            System.out.println(i);
        }
    }

    public static void sortBySelect(int[] array) {
        int length = array.length;
        int step = length / 2;
        while (step > 0) {
            for (int numberOfGroup = 0; numberOfGroup < length - step; numberOfGroup++) {// проходим по всем нашим группам
                int j = numberOfGroup;
                while (j >= 0 && array[j] > array[j + step]) {//сортировка вставкой внутри группы
                    int temp = array[j];
                    array[j] = array[j + step];
                    array[j + step] = temp;
                    j--; // j-=step
                }
            }
            step = step / 2; // уменьшаем наш шаг
        }
    }
}
