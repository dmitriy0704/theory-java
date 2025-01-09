package dev.folomkin.core.oop.principles.code.inheritance;

public class BoxWeight extends Box {
    double weight; // вес коробки

    // Конструктор для клонирования объекта
    BoxWeight(BoxWeight ob) {
        super(ob);
        this.weight = ob.weight;
    }

    // Конструктор для всех параметров
    BoxWeight(double w, double h, double d, double m) {
        super(w, h, d);
        weight = m;
    }

    // Стандартный конструктор
    BoxWeight(){
        super();
        weight = -1;
    }

    // Для кубической коробки
    BoxWeight(double len, double m) {
        super(len);
        weight = m;
    }
}
