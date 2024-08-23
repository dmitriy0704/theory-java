package dev.folomkin.core.lambda;

import java.io.IOException;
import java.io.Reader;

// Демонстрация использования ссылки на метод экземпляра.
// Функциональный интерфейс для предикатов,
// работающих с целочисленными значениями,
interface IntPredicate {
    boolean test(int n);
}

// Этот класс хранит значения типа int. Кроме того, в нем определен
// метод экземпляра isFactor(), который возвращает true, если его аргумент
// является делителем сохраненного числа
class MyIntNum {

}

class Code {

}
