package dev.folomkin.design_patterns.patterns;


// Общий вид класса "Кофе"

class Coffee {
    public void grindCoffee() {
        // перемалываем кофе
    }

    public void makeCoffee() {
        // делаем кофе
    }

    public void pourIntoCup() {
        // наливаем в чашку
    }
}

// Виды Кофе
class Americano extends Coffee {
}

class Cappuccino extends Coffee {
}

class CaffeLatte extends Coffee {
}

class Espresso extends Coffee {
}


// Типы Кофе, на которые принимаются заказы
enum CoffeeType {
    ESPRESSO,
    CAPPUCCINO,
    AMERICANO,
    CAFFE_LATTE
}


//Кофейня
class CoffeeShop {
    private final SimpleCoffeeFactory coffeeFactory;

    public CoffeeShop(SimpleCoffeeFactory coffeeFactory) {
        this.coffeeFactory = coffeeFactory;
    }

    public Coffee orderCoffee(CoffeeType type) {
        Coffee coffee = coffeeFactory.createCoffee(type);
        coffee.grindCoffee();
        coffee.makeCoffee();
        coffee.pourIntoCup();
        System.out.println("Ваш кофе!");
        return coffee;
    }
}


// Фабрика
class SimpleCoffeeFactory {
    public Coffee createCoffee(CoffeeType type) {
        Coffee coffee = null;

        switch (type) {
            case AMERICANO:
                coffee = new Americano();
                break;
            case ESPRESSO:
                coffee = new Espresso();
                break;
            case CAPPUCCINO:
                coffee = new Cappuccino();
                break;
            case CAFFE_LATTE:
                coffee = new CaffeLatte();
                break;
        }

        return coffee;
    }
}


public class Code {
    public static void main(String[] args) {

    }
}
