package dev.folomkin.core.generics;


// Обобщения и массивы
class Gen<T> {
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

class Code {
    public static void main(String[] args) {
        Gen<Integer> iOb;
        iOb = new Gen<Integer>(88);
        iOb.showType();
        int ob = iOb.getOb();
        System.out.println(ob);

        Gen<String> sOb = new Gen<>("Текст");
        sOb.showType();
    }
}
