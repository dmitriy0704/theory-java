package dev.folomkin.core.collections_streamapi.sream_api;


class Car {
    private String model;
    private int maxSpeed;
    private int yearOfManufacture;

    public Car(String model, int maxSpeed, int yearOfManufacture) {
        this.model = model;
        this.maxSpeed = maxSpeed;
        this.yearOfManufacture = yearOfManufacture;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public int getMaxSpeed() {
        return maxSpeed;
    }

    public void setMaxSpeed(int maxSpeed) {
        this.maxSpeed = maxSpeed;
    }

    public int getYearOfManufacture() {
        return yearOfManufacture;
    }

    public void setYearOfManufacture(int yearOfManufacture) {
        this.yearOfManufacture = yearOfManufacture;
    }

    public void gas(String gas) {
        System.out.println(gas);
    }

    public void brake() {
        System.out.println("Brake");
    }
}

class Truck extends Car {
    private String engine;
    private String color;

    public Truck(String model, int maxSpeed, int yearOfManufacture) {
        super(model, maxSpeed, yearOfManufacture);
    }

    public Truck(String model, int maxSpeed, int yearOfManufacture, String engine, String color) {
        super(model, maxSpeed, yearOfManufacture);
        this.engine = engine;
        this.color = color;
    }

    public String getEngine() {
        return engine;
    }

    public void setEngine(String engine) {
        this.engine = engine;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }
}

class Sedan extends Car {
    public Sedan(String model, int maxSpeed, int yearOfManufacture) {
        super(model, maxSpeed, yearOfManufacture);
    }
}

class F1Car extends Car {
    public void pitStop() {
    }

    public F1Car(String model, int maxSpeed, int yearOfManufacture) {
        super(model, maxSpeed, yearOfManufacture);
    }
}

public class Example {
    public static void main(String[] args) {
        F1Car f1Car = new F1Car("F1Car", 10, 20);
        f1Car.pitStop();
        f1Car.gas("f1 gas");
    }
}