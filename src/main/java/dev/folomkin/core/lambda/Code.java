package dev.folomkin.core.lambda;


// Использование обобщенного функционального интерфейса;

// Обобщенный функциональный интерфейс с двумя параметрами,
// который возвращает результат типа boolean
interface SomeTest<T> {
    boolean test(T n, T m);
}

public class Code {
    public static void main(String[] args) {

        // Это лямбда выражение определяет, является
        // ли одно число типа Integer делителем другого
        SomeTest<Integer> testI = (n, d) -> (n % d) == 0;
        if (testI.test(10, 2))
            System.out.println("2 является делителем 10");

        // Это лямбда-выражение определяет, является ли одно число типа Double
        // делителем другого
        SomeTest<Double> testD = (n, d) -> (n % d) == 0;
        if (testD.test(10.0, 2.0))
            System.out.println("2 является делителем 10");


        // Это лямбда-выражение определяет, является
        // л и одна строка частью другой строки.
        SomeTest<String> isIn = (a, b) -> a.indexOf(b) != -1;
        String str = "Generic Functional Interface";

        System.out.println("Проверяемая строка: " + str);
        if (isIn.test(str, "face"))
            System.out.println("Строка 'face' найдена.");
        else
            System.out.println("Строка 'face' не найдена.");

    }
}
