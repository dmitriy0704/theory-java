package dev.folomkin.core.lambda;


// Функциональный интерфейс
interface MyValue {
    double getValue();
}

// Функциональный интерфейс
interface MyParamValue {
    double getValue(double value);
}


public class Code {
    public static void main(String[] args) {
        MyValue myVal; // Ссылка на функциональный интерфейс

        // Здесь лямбда-выражение представляет собой константное выражение.
        // Когда оно присваивается myVal, конструируется экземпляр класса,
        // где лямбда-выражение реализует метод getValue() из MyValue.

        // Интерфейс реализуется с помощью анонимного класса
//        myVal = new MyValue() {
//            public double getValue() {
//                return 42.0;
//            }
//        };

        // Который можно записать как лямбда выражение:
        myVal = () -> 42.0; // <- Простое лямбда-выражение

        // Вызвать метод getValue(), предоставляемый ранее присвоенным лямбда-выражением
        System.out.println(myVal.getValue());

        // Создать параметризованное лямбда-выражение и присвоить
        // его ссылке MyParamValue. Это лямбда-выражение возвращает
        // обратную величину переданного ему аргумента.
        MyParamValue myParamVal = (value) -> 1 / value; // Лямбда-выражение имеющее параметры

        // Вызываем getValue через ссылку myPval
        System.out.println(myParamVal.getValue(4.0));
        System.out.println(myParamVal.getValue(8.0));

        // Лямбда-выражение должно быть совместимым с методом, определенным
        // в функциональном интерфейсе. Таким образом, следующий код недопустим:
        // myVal = О -> "three"; // Ошибка! Типы String и double не совместимы !
        // myPval = () > Math.random(); // Ошибка! Требуется параметр!
    }
}
