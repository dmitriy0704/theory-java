package dev.folomkin.core.generics;


// Обобщения и массивы
class Gen<T extends Number> {
    T ob;
    T[] vals; // нормально
    Gen(T o, T[] nums) {
        ob = o;
        // Этот оператор не допустим.
        // vals = new T[10]; // Невозможно создать массив элементов типа Т
        // Но следующий оператор законен
        vals = nums; // Присваивать ссылку на существующий массив разрешено
    }
}

class Code {
    public static void main(String[] args) {
        Integer[] n = {1, 2, 3, 4, 5};
        Gen<Integer> iOb = new Gen<>(50, n);

        // невозможно создать массив обобщенных ссылок для конкретного типа
        // Gen<Integer>[] gens = new Gen<Integer>[10]; // Ошибка!
        // Все нормально.
        Gen<?>[] gens = new Gen<?>[10]; // нормально
    }
}
