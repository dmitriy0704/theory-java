package dev.folomkin.core.functional_interfaces;

import java.io.IOException;
import java.util.Objects;
import java.util.function.Consumer;

// Функциональный интерфейс для предикатов,
// работающими с целочисленными значениями
interface IntPredicate {
    boolean test(int n);
}

// В этом классе определены три статических метода,
// которые выполняют проверку целого числа на предмет
// соответствию условию.
class MyIntPredicates {

    // true если число простое
    static boolean isPrime(int n) {
        if (n < 2) return false;
        for (int i = 2; i <= n / i; i++) {
            if (n % i == 0) return false;
        }
        return true;
    }

    // true если число четное
    static boolean isEven(int n) {
        return n % 2 == 0;
    }

    // true если число положительное
    static boolean isPositive(int n) {
        return n > 0;
    }
}

class Code {
/*    // Типом первого параметра этого метода является
    // функциональный интерфейс. Таким образом, ему можно передавать
    // ссылку на любой экземпляр данного интерфейса, включая
    // созданный ссылкой на метод.
    // ... Далее в классе Code создается метод numTest(), в первом
    // параметре которого передается ссылка на intPredicate.
    // Во втором параметре указывается проверяемое целое число...
    static boolean numTest(IntPredicate p, int v) {
        return p.test(v);
    }*/

    public static void main(String[] args) throws IOException {

        Consumer<Object> consumer = Objects::requireNonNull;
        consumer.accept(null);



        //        boolean result;
/*
        // Передать numTest() ссылку на метод isPrime.
        result = numTest(MyIntPredicates::isPrime, 17);
        if (result) System.out.println("17 is prime");

        // Передать numTestO ссылку на метод isEven.
        result = numTest(MyIntPredicates::isEven, 12);
        if (result) System.out.println("12 является четным.");

        // Передать numTestO ссылку на метод isPositive.
        result = numTest(MyIntPredicates::isPositive, 11);
        if (result) System.out.println("11 является положительным.");*/
    }

    public static class LambdaArgumentDemo {
        interface StringFunc {
            String func(String str);
        }



    }
}