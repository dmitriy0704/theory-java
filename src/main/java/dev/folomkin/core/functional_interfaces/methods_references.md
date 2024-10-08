# Ссылки на методы

С лямбда-выражениями связана одна важная возможность, которая называется ссылкой
на метод. Ссылка на метод предлагает способ обращения к методу, не инициируя его
выполнение. Она имеет отношение к лямбда-выражениям, поскольку тоже требует
контекста целевого типа, состоящего из совместимого функционального интерфейса.
При вычислении ссылки на метод также создается экземпляр функционального
интерфейса. Существует четыре типа ссылок на методы:

- Ссылка на статический метод
- Ссылка на метод экземпляра
- Ссылка на метод экземпляра конкретного объекта
- Ссылка на конструктор
___
Более короткая запись лямбда-выражения возможна в случае, если реализации
функционального интерфейса необходимо передать уже существующий метод без всяких
изменений.

Оператор видимости «::» отделяет метод от объекта или класса. Существуют три
варианта записи:

- **ContainingClass::staticMethodName** — ссылка на статический метод;
- **ContainingObject::instanceMethodName** — ссылка на нестатический метод
  конкретного объекта;
- **ContainingType::methodName** — ссылка на нестатический метод любого объекта
  конкретного типа.

Первые два варианта эквивалентны лямбда-выражению с параметрами методами.
В третьем варианте первый параметр становится целевым для метода:

    Comparator<Long> comparator = (l1, l2) -> l1.compareTo(l2);
    comparator = Long::compareTo;

В качестве объекта можно использовать ссылки this и super.
Кроме ссылки на метод, существуют также и ссылки на конструктор, где в качестве
имени метода указывается оператор new.

    Supplier<StringBuilder> supplier1 = StringBuilder::new;


## Ссылка на статические методы

Для ссылки на статический метод применяется следующий общий синтаксис,
предусматривающий указание имени класса перед именем метода:

    имя-класса::имя-метода

Имя класса отделяется от имени метода двойным двоеточием. Разделитель :: был
добавлен к языку в версии JDK 8 специально для этой цели. Ссылка на метод может
использоваться везде, где она совместима со своим целевым типом.

В приведенном коде сначала объявляется функциональный интерфейс
IntPredicate с методом test(), который принимает параметр типа int и
возвращает результат типа boolean. Таким образом, его можно применять для
проверки целочисленного значения на предмет соответствия заданному условию.
Затем создается класс по имени MyIntPredicates, где определяются три
статических метода, каждый из которых проверяет, удовлетворяет ли значение
заданному условию: isPrime(), isEven() и isPositive(). Метод isPrime()
проверяет, является ли число простым, метод isEven() является ли число четным, а
метод isPositive() — является ли число положительным.

Далее в классе Code создается метод numTest(), в первом параметре которого
передается ссылка на intPredicate. Во втором параметре указывается проверяемое
целое число. Внутри main() производятся три различных теста за счет
вызова numTest() и передачи ссылки на метод выполняемого теста.

```java
import java.io.IOException;
import java.io.Reader;

// Функциональный интерфейс для предикатов,
// работающими с целочисленными значениями
interface IntPredicate {
    boolean test(int n);
}

// В этом классе определены три статических метода,
// которые выполняют проверку целого числа на предмет
// соответствию условию.
class MyIntPredicates {

    // Статический метод, который возвращает true,
    // если число является простым
    static boolean isPrime(int n) {
        if (n < 2) return false;
        for (int i = 2; i <= n / i; i++) {
            if (n % i == 0) return false;
        }
        return true;
    }

    // Статический метод, который возвращает true,
    // если число является четным.
    static boolean isEven(int n) {
        return n % 2 == 0;
    }

    // Статический метод, который возвращает true,
    // если число является положительным.
    static boolean isPositive(int n) {
        return n > 0;
    }
}

class Code {
    // Типом первого параметра этого метода является
    // функциональный интерфейс. Таким образом, ему можно передавать
    // ссылку на любой экземпляр данного интерфейса, включая
    // созданный ссылкой на метод.
    static boolean numTest(IntPredicate p, int v) {
        return p.test(v);
    }

    public static void main(String[] args) throws IOException {
        boolean result;

        // Передать numTest() ссылку на метод isPrime.
        result = numTest(MyIntPredicates::isPrime, 17);
        if (result) System.out.println("17 is prime");

        // Передать numTestO ссылку на метод isEven.
        result = numTest(MyIntPredicates::isEven, 12);
        if (result) System.out.println("12 является четным.");

        // Передать numTestO ссылку на метод isPositive.
        result = numTest(MyIntPredicates::isPositive, 11);
        if (result) System.out.println("11 является положительным.");
    }
}

```

## Ссылки на методы экземпляра

Ссылка на метод конкретного экземпляра создается с помощью следующего базового
синтаксиса:

    объектная-ссылка : : имя-метода

Синтаксис ссылки на метод экземпляра подобен синтаксису, применяемому для ссылки
на статический метод, но вместо имени класса используется объектная ссылка.
Таким образом, метод, на который указывает ссылка, работает по отношению к
конструкции _объектная ссылка_.

Данный аспект иллюстрируется в приведенной ниже программе. В ней используется
тот же интерфейс IntPredicate и метод test(), что и в предыдущей программе, но
создается класс по имени MyIntNum, где хранится значение int и определяется
метод isFactor(), который выясняет, является ли переданное значение делителем
значения, сохраненного в экземпляре MyIntNum. Затем в методе main() создаются
два экземпляра MyIntNum, после чего вызывается numTest() с передачей ссылки на
метод isFactorO и проверяемое значение. В каждом случае ссылка на метод работает
относительно конкретного объекта.

