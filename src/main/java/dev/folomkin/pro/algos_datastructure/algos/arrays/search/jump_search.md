
## Поиск прыжками

От двоичного поиска этот алгоритм отличает движение исключительно
вперёд. Такой поиск требует отсортированной коллекции.  
Мы прыгаем вперёд на интервал sqrt(arraylength), пока не достигнем элемента
большего, чем текущий элемент или конца массива. При каждом прыжке записывается
предыдущий шаг.  
Прыжки прекращаются, когда найден элемент больше искомого. Затем запускаем
линейный поиск между предыдущим и текущим шагами.  
Это уменьшает поле поиска и делает линейный поиск жизнеспособным вариантом.

**Временная сложность**  
Поскольку в каждой итерации мы перепрыгиваем на шаг, равный sqrt(arraylength),
временная сложность этого поиска составляет O(sqrt (N)).

**Пространственная сложность**  
Искомый элемент занимает одну единицу пространства, поэтому пространственная
сложность алгоритма составляет O(1).

**Применение**  
Этот поиск используется поверх бинарного поиска, когда прыжки в обратную сторону
затратны.

```java

public static void main(String[] args) {
    int index = jumpSearch(new int[]{3, 22, 27, 47, 57, 67, 89, 91, 95, 99}, 67);
    print(67, index);
}

public static int jumpSearch(int[] integers, int elementToSearch) {
/*
   Мы начинаем с jumpstep размером с корень квадратный от длины массива и 
   продолжаем прыгать вперёд с тем же размером, пока не найдём элемент, 
   который будет таким же или больше искомого элемента.
   
   Сначала проверяется элемент integers[jumpStep], затем integers[2jumpStep], 
   integers[3jumpStep] и так далее. Проверенный элемент сохраняется в 
   переменной previousStep.
   
   Когда найдено значение, при котором
   integers[previousStep] < elementToSearch < integers[jumpStep], 
   производится линейный поиск между integers[previousStep] и 
   integers[jumpStep] или элементом большим, чем elementToSearch.
 */

    int arrayLength = integers.length;
    int jumpStep = (int) Math.sqrt(integers.length);
    int previousStep = 0;

    while (integers[Math.min(jumpStep, arrayLength) - 1] < elementToSearch) {
        previousStep = jumpStep;
        jumpStep += (int) (Math.sqrt(arrayLength));
        if (previousStep >= arrayLength)
            return -1;
    }
    while (integers[previousStep] < elementToSearch) {
        previousStep++;
        if (previousStep == Math.min(jumpStep, arrayLength))
            return -1;
    }

    if (integers[previousStep] == elementToSearch)
        return previousStep;
    return -1;
}

```
