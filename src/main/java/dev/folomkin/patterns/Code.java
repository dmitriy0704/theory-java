package dev.folomkin.patterns;

abstract class Car {
    abstract void build();
}

abstract class CarModifications extends Car {
    Car car;

    public CarModifications(Car car) {
        this.car = car;
    }
}

class Spoiler extends CarModifications {
    public Spoiler(Car car) {
        super(car);
    }

    public void build() {
        car.build();
        addSpoiler();
    }

    void addSpoiler() {
        System.out.println("Spoiler built");
    }
}

class Ford extends Car {
    public void build() {
        System.out.println("Ford built");
    }
}

class Audi extends Car {
    public void build() {
        System.out.println("Audi built");
    }
}

public class Code {
    public static void main(String[] args) {
        Car audi = new Audi();
        Car audiWithSpoiler = new Spoiler(audi);
        audiWithSpoiler.build();
    }
}