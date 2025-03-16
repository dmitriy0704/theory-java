# Поиск

## Линейный поиск

```java
public class Main {
    public static void main(String[] args) {
        int[] integerArray = {-183, 12, 15, 40, 234, 345, 800, 977800, 345, 977};
        int elementToFind = 977800;
        System.out.println(target);
    }

    public static int linearSearch(int[] arr, int target) {
        for (int i = 0; i < arr.length; i++) {
            if (arr[i] == target) {
                return i;
            }
        }
        return -1;
    }
}
```

Сложности:

- Чтобы получить позицию искомого компонента, будет перебираться набор из N
  элементов.
- При худшем сценарии для соответствующего алгоритма искомый компонент –
  последний в массиве.
- Для последней описанной ситуации потребуется N итераций.

Сложность линейного варианта – O(N).

**Применение**

Линейный поиск можно использовать для малого, несортированного набора данных,
который не увеличивается в размерах.
Несмотря на простоту, алгоритм не находит применения в проектах из-за линейного
увеличения временной сложности.

## Бинарный поиск(Логарифмический/"разделяй и властвуй")

В О-нотации сложность алгоритма равна O(log(N)).

Этот вид поиска использует подход «Разделяй и властвуй», требует предварительной
сортировки набора данных.
Алгоритм делит входную коллекцию на равные половины, и с каждой итерацией
сравнивает целевой элемент с элементом в середине. Поиск заканчивается при
нахождении элемента. Иначе продолжаем искать элемент, разделяя и выбирая
соответствующий раздел массива. Целевой элемент сравнивается со средним.
Вот почему важно иметь отсортированную коллекцию при использовании двоичного
поиска.
Поиск заканчивается, когда firstIndex (указатель) достигает lastIndex (
последнего элемента). Значит мы проверили весь массив Java и не нашли элемента.
Есть два способа реализации этого алгоритма: итеративный и рекурсивный.
Временная и пространственная сложности одинаковы для обоих способов в реализации
на Java.

**Временная сложность**

Временная сложность алгоритма двоичного поиска равна O(log (N)) из-за деления
массива пополам. Она превосходит O(N) линейного алгоритма.

**Пространственная сложность**

Одна единица пространства требуется для хранения искомого элемента.
Следовательно, пространственная сложность равна O(1).
Рекурсивный двоичный поиск хранит вызов метода в стеке. В худшем случае
пространственная сложность потребует O(log (N)).

**Применение**

Этот алгоритм используется в большинстве библиотек и используется с
отсортированными структурами данных.
Двоичный поиск реализован в методе Arrays.binarySearch Java API.

Работает так:

1. Входная коллекция делится на равные половины.
2. С каждой итерацией происходит сравнение целевого элемента с тем, что в
   середине.
3. Поиск заканчивается при нахождении компонента.
4. Если не получилось обнаружить компонент, его поиск происходит далее путем
   разделения и выбора соответствующего раздела массива.

### Итеративная реализация

```java
public class Main {
    public static void main(String[] args) {
        int[] values = {11, 12, 18, 72, 82, 84, 86, 90, 102, 122, 123, 124, 130};
        int valueToFind = 72;

        System.out.printf("Index = %d%n", binarySearch(values, valueToFind));
    }

    private static int binarySearch(int[] sortedArray, int valueToFind) {
        int index = -1;

        //-> 1. Сортируем массив:
        Arrays.sort(sortedArray);   //  int[] values = {11, 12, 18, 72, 82, 84, 86, 90, 102, 122, 123, 124, 130};

        var low = 0; // 0
        var high = sortedArray.length - 1; // 8
        while (low <= high) {

            //-> 2. Делим пополам и находим середину
            var middle = low + (high - low) / 2;
            var current = sortedArray[middle]; //-> current - текущий элемент

            // 3. Сравниваем средний элемент с искомым:

            // 4. Если искомое больше среднего - продолжаем
            // поиск в правой части массива: делим ее пополам, повторяя пункт 3.
            // Если заданное число меньше - возвращаемся к пункту 3.

            if (valueToFind > current) {
                // текущий элемент меньше искомого - сдвигаем левую границу
                low = middle + 1;
            } else if (valueToFind < current) {
                // иначе сдвигаем правую границу
                high = middle - 1;

            } else {
                index = middle;
                break;
            }
        }
        // проверили весь массив, но не нашли элемент
        return index;

    }
}
```

### С использованием рекурсии

Рекурсивный подход отличается вызовом самого метода при получении нового
раздела. В итеративном подходе всякий раз, когда мы определяли новый раздел, мы
изменяли первый и последний элементы, повторяя процесс в том же цикле.
Другое отличие – рекурсивные вызовы помещаются в стек и занимают одну единицу
пространства за вызов.

```java

public class Main {
    public static void main(String[] args) {
        int[] integerArray = {-183, 12, 15, 40, 234, 345, 800, 977800, 345, 977};
        int elementToFind = 977800;
        Arrays.sort(integerArray); //-> [-183, 12, 15, 40, 234, 345, 345, 800, 977, 977800]
        System.out.println("Element " + elementToFind + " found, index: " +
                binarySearch(integerArray, elementToFind, 0, integerArray.length - 1));
    }

    public static int binarySearch(int[] sortedIntegerArray, int elementToFind, int low, int high) {
        //   В данном примере происходит следующее:
        //
        //  1. В методе binarySearch мы принимаем упорядоченный (отсортированный) массив целых чисел.
        //  2. Проверяем, чтобы low был меньше high (low = 0, high = 9) – получаем false
        if (low > high) {
            return -1;
        }

        //  3. Получаем середину массива – 234
        int mid = low + (high - low) / 2;

        //  4. Сравниваем 234 с искомым значением 977800 – 977800 больше.
        //-> 977800 < 234
        if (elementToFind < sortedIntegerArray[mid]) {
            return binarySearch(sortedIntegerArray, elementToFind, low, mid - 1);

            //  5. Отсекаем левую часть массива и получаем новые low – 5 и high – 9.
        } else if (elementToFind > sortedIntegerArray[mid]) {
            //-> 977800 > 234
            return binarySearch(sortedIntegerArray, elementToFind, mid + 1, high);
        } else {
            return mid;
        }


        //  6. Новая медиана – 977800
        //  7. Попадаем в условие else – возвращаем индекс 7.


    }
}


```

## Алгоритм Кнута – Морриса – Пратта

Алгоритм КМП осуществляет поиск текста по заданному шаблону. Он разработан
Дональдом Кнутом, Воном Праттом и Джеймсом Моррисом: отсюда и название.
В этом поиске сначала компилируется заданный шаблон. Компилируя шаблон, мы
пытаемся найти префикс и суффикс строки шаблона. Это поможет в случае
несоответствия – не придётся искать следующее совпадение с начального индекса.
Вместо этого мы пропускаем часть текстовой строки, которую уже сравнили, и
начинаем сравнивать следующую. Необходимая часть определяется по префиксу и
суффиксу, поэтому известно, какая часть уже прошла проверку и может быть
безопасно пропущена.
КМП работает быстрее алгоритма перебора благодаря пропускам.

**Реализация:**

```java
package dev.folomkin.algos_datastructure;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class Code {
    public static void main(String[] args) {

/*
        В текстовом шаблоне AAABAAA наблюдается и кодируется в массив шаблонов следующий шаблон:
        Шаблон A (Одиночная A) повторяется в 1 и 4 индексах.
        Паттерн AA (Двойная A) повторяется во 2 и 5 индексах.
        Шаблон AAA (Тройная A) повторяется в индексе 6.
        Описанный выше шаблон ясно показан в скомпилированном массиве.
        С помощью этого массива КМП ищет заданный шаблон в тексте, не возвращаясь в начало текстового массива.
*/
        String pattern = "AAABAAA";
        String text = "ASBNSAAAAAABAAAAABAAAAAGAHUHDJKDDKSHAAJF";
        List<Integer> foundIndexes = Code.performKMPSearch(text, pattern);

        if (foundIndexes.isEmpty()) {
            System.out.println("Pattern not found in the given text String");
        } else {
            System.out.println("Pattern found in the given text String at positions: " +.stream().map(Object::toString).collect(Collectors.joining(", ")))
            ;
        }
    }

    public static int[] compilePatternArray(String pattern) {

//    Скомпилированный массив Java можно рассматривать как массив, хранящий шаблон
//    символов. Цель – найти префикс и суффикс в шаблоне. Зная эти элементы, можно
//    избежать сравнения с начала текста после несоответствия и приступать к сравнению
//    следующего символа.
//    Скомпилированный массив сохраняет позицию предыдущего местонахождения текущего
//    символа в массив шаблонов.

        int patternLength = pattern.length();
        int len = 0;
        int i = 1;
        int[] compliedPatternArray = new int[patternLength];
        compliedPatternArray[0] = 0;

        while (i < patternLength) {
            if (pattern.charAt(i) == pattern.charAt(len)) {
                len++;
                compliedPatternArray[i] = len;
                i++;
            } else {
                if (len != 0) {
                    len = compliedPatternArray[len - 1];
                } else {
                    compliedPatternArray[i] = len;
                    i++;
                }
            }
        }
        System.out.println("Compiled Pattern Array " + Arrays.toString(compliedPatternArray));
        return compliedPatternArray;
    }

    public static List<Integer> performKMPSearch(String text, String pattern) {

    /*    Здесь мы последовательно сравниваем символы в шаблоне и
        текстовом массиве. Мы продолжаем двигаться вперёд, пока не получим
        совпадение. Достижение конца массива при сопоставлении означает
        нахождение шаблона в тексте.

        Если обнаружено несоответствие при сравнении двух массивов, индекс
        символьного массива перемещается в значение compiledPatternArray().
        Затем мы переходим к следующему символу в текстовом массиве.
        КМП превосходит метод грубой силы однократным сравнением текстовых
        символов при несоответствии.
    */

        int[] compliedPatternArray = compilePatternArray(pattern);

        int textIndex = 0;
        int patternIndex = 0;

        List<Integer> foundIndexes = new ArrayList<>();

        while (textIndex < text.length()) {
            if (pattern.charAt(patternIndex) == text.charAt(textIndex)) {
                patternIndex++;
                textIndex++;
            }
            if (patternIndex == pattern.length()) {
                foundIndexes.add(textIndex - patternIndex);
                patternIndex = compliedPatternArray[patternIndex - 1];
            } else if (textIndex < text.length() && pattern.charAt(patternIndex) != text.charAt(textIndex)) {
                if (patternIndex != 0)
                    patternIndex = compliedPatternArray[patternIndex - 1];
                else
                    textIndex = textIndex + 1;
            }
        }
        return foundIndexes;
    }

}

```

**Временная сложность**     
Для поиска шаблона алгоритму нужно сравнить все элементы в заданном тексте.
Необходимое для этого время составляет O(N). Для составления строки шаблона нам
нужно проверить каждый символ в шаблоне – это еще одна итерация O(M).
O (M + N) – общее время алгоритма.

**Пространственная сложность**  
O(M) пространства необходимо для хранения скомпилированного шаблона для
заданного шаблона размера M.

**Применение**  
Этот алгоритм используется в текстовых инструментах для поиска шаблонов в
текстовых файлах.

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

## Интерполяционный поиск

Интерполяционный поиск используется для поиска элементов в отсортированном
массиве. Он полезен для равномерно распределенных в структуре данных.
При равномерно распределенных данных местонахождение элемента определяется
точнее. Тут и вскрывается отличие алгоритма от бинарного поиска, где мы пытаемся
найти элемент в середине массива.
Для поиска элементов в массиве алгоритм использует формулы интерполяции.
Эффективнее применять эти формула для больших массивов. В противном случае
алгоритм работает как линейный поиск.

```java
public static void main(String[] args) {
    int index = interpolationSearch(new int[]{1, 2, 3, 4, 5, 6, 7, 8}, 6);
    print(67, index);
}

public static int interpolationSearch(int[] integers, int elementToSearch) {

    int startIndex = 0;
    int lastIndex = (integers.length - 1);

    while ((startIndex <= lastIndex) && (elementToSearch >= integers[startIndex]) &&
            (elementToSearch <= integers[lastIndex])) {
        // используем формулу интерполяции для поиска возможной лучшей позиции для существующего элемента
        int pos = startIndex + (((lastIndex - startIndex) /
                (integers[lastIndex] - integers[startIndex])) *
                (elementToSearch - integers[startIndex]));

        if (integers[pos] == elementToSearch)
            return pos;

        if (integers[pos] < elementToSearch)
            startIndex = pos + 1;

        else
            lastIndex = pos - 1;
    }
    return -1;
}

```

Смотрите, как работают формулы интерполяции, чтобы найти 6:

    startIndex = 0  
    lastIndex = 7  
    integers[lastIndex] = 8  
    integers[startIndex] = 1  
    elementToSearch = 6

Теперь давайте применим эти значения к формулам для оценки индекса элемента
поиска:

index=0+(7−0)/(8−1)∗(6−1)=5

Элемент integers[5] равен 6 — это элемент, который мы искали. Индекс для
элемента рассчитывается за один шаг из-за равномерной распределенности данных.

**Временная сложность**  
В лучшем случае временная сложность такого алгоритма – O(log log N). При
неравномерном распределении элементов сложность сопоставима с временной
сложностью линейного алгоритма, которая = O(N).

**Пространственная сложность**  
Алгоритм требует одну единицу пространства для хранения элемента для поиска. Его
пространственная сложность = O(1).

**Применение**  
Алгоритм полезно применять для равномерно распределенных данных вроде телефонной
книги.

## Экспоненциальный поиск

Экспоненциальный поиск используется для поиска элементов путём перехода в
экспоненциальные позиции, то есть во вторую степень.
В этом поиске мы пытаемся найти сравнительно меньший диапазон и применяем на нем
двоичный алгоритм для поиска элемента.
Для работы алгоритма коллекция должна быть отсортирована.

```java
public static void main(String[] args) {
    int index = exponentialSearch(new int[]{3, 22, 27, 47, 57, 67, 89, 91, 95, 99}, 67);
    print(67, index);
}

public static int exponentialSearch(int[] integers, int elementToSearch) {
    if (integers[0] == elementToSearch)
        return 0;
    if (integers[integers.length - 1] == elementToSearch)
        return integers.length;
    int range = 1;
    while (range < integers.length && integers[range] <= elementToSearch) {
        range = range * 2;
    }
    return Arrays.binarySearch(integers, range / 2, Math.min(range, integers.length), elementToSearch);
}

```

Мы пытаемся найти элемент больше искомого. Зачем? Для минимизации диапазона
поиска. Увеличиваем диапазон, умножая его на 2, и снова проверяем, достигли ли
мы элемента больше искомого или конца массива. При нахождении элемента мы
выходим из цикла. Затем выполняем бинарный поиск с startIndex в качестве range/2
и lastIndex в качестве range.
В нашем случае значение диапазона достигается в элементе 8, а элемент в
integers[8] равен 95. Таким образом, диапазон, в котором выполняется бинарный
поиск:

    startIndex = range/2 = 4
    lastIndex = range = 8

При этом вызываем бинарный поиск:

    Arrays.binarySearch(integers, 4, 8, 6);

Здесь важно отметить, что можно ускорить умножение на 2, используя оператор
левого сдвига range << 1 вместо *.

**Временная сложность**  
В худшем случае временная сложность этого поиска составит O(log (N)).

**Пространственная сложность**  
Итеративный алгоритм двоичного поиска требует O(1) места для хранения искомого
элемента.
Для рекурсивного двоичного поиска пространственная сложность становится равной
O(log (N)).

**Применение**  
Экспоненциальный поиск используется с большими массивами, когда бинарный поиск
затратен. Экспоненциальный поиск разделяет данные на более доступные для поиска
разделы.

