package dev.folomkin.core.oop.type_class.enums;

public enum Transport {
    //  Использование конструктора, переменной экземпляра и метода перечисления
    CAR(100), TRUCK(90), AIRPLANE(950);

    private int speed; // переменная экземпляра;

    // Конструктор
    Transport(int s) { // Конструктор
        speed = s;
    }
    public int getSpeed() { // Метод
        return speed;
    }
}

class EnumDemo {
    public static void main(String[] args) {
        Transport transport = Transport.AIRPLANE;
        String s = switch (transport) {
            case CAR -> "вы выбрали CAR";
            case TRUCK -> "вы выбрали TRUCK";
            case AIRPLANE -> "вы выбрали AIRPLANE";
            default -> "Вы выбрали другое";
        };
    }
}

class EnumDemo2 {
    public static void main(String[] args) {
        Transport transport;
        System.out.println("Все константы Transport");

        // Метод values;
        Transport[] transports = Transport.values();
        for (Transport t : transports) {
            System.out.println(t);
        }
        System.out.println();

        //  Метод valueOf;
        transport = Transport.valueOf("TRUCK");
        System.out.println("transport " + transport);
    }
}

class EnumDemo3 {
    public static void main(String[] args) {
        Transport transport;
        System.out.println("Скорость равна " + Transport.CAR.getSpeed());
        //      Все скорости:
        for (Transport t : Transport.values()) {
            System.out.println(t.getSpeed());
        }
    }
}