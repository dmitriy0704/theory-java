package dev.folomkin.pro.design_patterns.gof.creational.abstract_factory;



// Интерфейсы продуктов
interface Chair {
    void sitOn();
}

interface Table {
    void use();
}

// Конкретные продукты для стиля "Современный"
class ModernChair implements Chair {
    @Override
    public void sitOn() {
        System.out.println("Сидим на современном стуле");
    }
}

class ModernTable implements Table {
    @Override
    public void use() {
        System.out.println("Используем современный стол");
    }
}

// Конкретные продукты для стиля "Классический"
class ClassicChair implements Chair {
    @Override
    public void sitOn() {
        System.out.println("Сидим на классическом стуле");
    }
}

class ClassicTable implements Table {
    @Override
    public void use() {
        System.out.println("Используем классический стол");
    }
}

// Абстрактная фабрика
interface FurnitureFactory {
    Chair createChair();
    Table createTable();
}

// Конкретная фабрика для "Современной" мебели
class ModernFurnitureFactory implements FurnitureFactory {
    @Override
    public Chair createChair() {
        return new ModernChair();
    }

    @Override
    public Table createTable() {
        return new ModernTable();
    }
}

// Конкретная фабрика для "Классической" мебели
class ClassicFurnitureFactory implements FurnitureFactory {
    @Override
    public Chair createChair() {
        return new ClassicChair();
    }

    @Override
    public Table createTable() {
        return new ClassicTable();
    }
}

// Клиентский код
public class AbstractFactoryExample {
    public static void main(String[] args) {
        // Выбор фабрики в зависимости от стиля
        FurnitureFactory factory = new ModernFurnitureFactory();

        Chair chair = factory.createChair();
        Table table = factory.createTable();

        chair.sitOn(); // Сидим на современной стуле
        table.use();   // Используем современный стол

        // Поменяем фабрику на классическую
        factory = new ClassicFurnitureFactory();

        chair = factory.createChair();
        table = factory.createTable();

        chair.sitOn(); // Сидим на классическом стуле
        table.use();   // Используем классический стол
    }
}