package dev.folomkin.design_patterns.pattterns.gof.creational.factory_method;


// Шаблон продукта
// 1. Это интерфейс, который определяет метод doShift(), который будут реализовывать конкретные продукты.
interface Product {
    void doShift();
}

// Конкретные продукты
// 2. ConcreteProductA и ConcreteProductB — это конкретные реализации интерфейса Product.
class ConcreteProductA implements Product {
    @Override
    public void doShift() {
        System.out.println("Используется продукт A");
    }
}

class ConcreteProductB implements Product {
    @Override
    public void doShift() {
        System.out.println("Используется продукт B");
    }
}

// Создатель
// 3. Creator — это абстрактный класс, который объявляет фабричный метод
// factoryMethod(). Он также содержит метод someOperation(), который использует
// продукт.
abstract class Creator {
    public abstract Product factoryMethod();

    public void someOperation() {
        // Вызываем фабричный метод для создания продукта
        Product product = factoryMethod();
        // Используем продукт
        product.doShift();
    }

}

// Конкретные создатели
// 4. ConcreteCreatorA и ConcreteCreatorB — это конкретные создатели, которые
// реализуют фабричный метод и создают соответствующие продукты.
class ConcreteCreatorA extends Creator {
    @Override
    public Product factoryMethod() {
        return new ConcreteProductA();
    }
}

class ConcreteCreatorB extends Creator {
    @Override
    public Product factoryMethod() {
        return new ConcreteProductB();
    }
}

// Клиентский код
// 5. В классе FactoryMethodExample создаются экземпляры конкретных создателей,
// и вызывается метод someOperation(), который создает и использует продукт.

public class FactoryMethodExample {
    public static void main(String[] args) {
        Creator creatorA = new ConcreteCreatorA();
        creatorA.someOperation(); // Используется продукт A

        Creator creatorB = new ConcreteCreatorB();
        creatorB.someOperation(); // Используется продукт B

    }
}