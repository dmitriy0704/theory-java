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

    public void gas() {
        System.out.println("Gas");
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

    @Override
    public void gas() {
        System.out.println("Truck Gas");
    }

    @Override
    public void brake() {
        System.out.println("Truck Brake");
    }
}

public class Example {
    public static void main(String[] args) {

    }
}