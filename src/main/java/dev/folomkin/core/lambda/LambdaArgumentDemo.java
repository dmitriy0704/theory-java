package dev.folomkin.core.lambda;


/*
Создаются три строковые функции, которые выполняют обращение строки, обращение
регистра букв в строке и замену пробелов дефисами. Функции реализованы в виде
лямбда-выражений функционального интерфейса StringFunc и передаются в
первом аргументе методу changeStr(). Метод changeStr() применяет строковую
функцию к строке, переданной во втором аргументе методу changeStr(),
и возвращает результат. Таким образом, changeStr() можно использовать для
применения разнообразных строковых функций.
*/

public class LambdaArgumentDemo {
    interface StringFunc {
        String func(String str);
    }



}