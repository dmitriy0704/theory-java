package dev.folomkin.core.generics;

// Простой обобщенный класс
// Здесь Т - параметр типа, который будет заменен при создании
// объекта типа Gen

class Gen<T> { // Обобщенный класс, Т - параметр обобщенного типа.
    T ob; // <-- объявлен объект типа Т;

    // Передаем конструктору ссылку на объект типа Т
    Gen(T o) {
        ob = o;
    }

    // Возвратить ob
    T getOb() {
        return ob;
    }

    // Отобразить тип Т
    void showType() {
        System.out.println("Тип T: " + ob.getClass().getName());
    }
}

// Демонстрация использования обобщенного класса


public class Code {
    public static void main(String[] args) {

    }
}

