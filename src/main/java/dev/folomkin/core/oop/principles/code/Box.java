package dev.folomkin.core.oop.principles.code;

public class Box {
    private double width;
    private double height;
    private double depth;

    // Конструктор применяемый для клонирования объекта
    Box(Box ob) {
        width = ob.width;
        height = ob.height;
        depth = ob.depth;
    }

    // Конструктор, используемый в случае указания всех размеров
    Box(double w, double h, double d) {
        width = w;
        height = h;
        depth = d;
    }

    // Если размеры не указаны
    public Box() {
        width = -1;
        height = -1;
        depth = -1;
    }

    // Для кубической коробки
    Box(double len) {
        width = height = depth = len;
    }

    // Объем
    double volume() {
        return width * height * depth;
    }
}
