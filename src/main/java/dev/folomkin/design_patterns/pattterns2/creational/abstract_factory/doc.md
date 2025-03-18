# Абстрактная фабрика(Abstract Factory)

- это порождающий паттерн проектирования, который позволяет создавать семейства
  связанных объектов, не привязываясь к конкретным классам создаваемых объектов.
  Паттерн "Абстрактная фабрика" (Abstract Factory) предоставляет интерфейс для
  создания семейств связанных или зависимых объектов без указания их конкретных
  классов. Этот паттерн позволяет создавать продукты, которые могут быть
  взаимозаменяемыми, и обеспечивает возможность добавления новых семейств
  продуктов без изменения существующего кода.

Вот пример реализации паттерна "Абстрактная фабрика" на Java от GPT:

```java
// Интерфейсы продуктов
// 1. Интерфейсы продуктов: Chair и Table — это интерфейсы, которые определяют
// методы для конкретных продуктов.
interface Chair {
    void sitOn();
}

interface Table {
    void use();
}


// 2. Конкретные продукты: ModernChair, ModernTable, ClassicChair, и ClassicTable —
// это конкретные реализации интерфейсов для разных стилей мебели.
// Конкретные продукты для стиля "Современный"
class ModernChair implements Chair {
    @Override
    public void sitOn() {
        System.out.println("Сидим на современной стуле");
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
// 3. Абстрактная фабрика: FurnitureFactory — это интерфейс, который объявляет
// методы для создания продуктов (стульев и столов).
interface FurnitureFactory {
    Chair createChair();

    Table createTable();
}

// 4. Конкретные фабрики: ModernFurnitureFactory и ClassicFurnitureFactory — это
// конкретные реализации фабрики, которые создают соответствующие продукты.

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

// 5. Клиентский код: В классе AbstractFactoryExample создается экземпляр фабрики,
// и в зависимости от выбранной фабрики создаются соответствующие продукты.
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
```

## Объяснение кода:

1. Интерфейсы продуктов: Chair и Table — это интерфейсы, которые определяют
   методы для конкретных продуктов.
2. Конкретные продукты: ModernChair, ModernTable, ClassicChair, и ClassicTable —
   это конкретные реализации интерфейсов для разных стилей мебели.
3. Абстрактная фабрика: FurnitureFactory — это интерфейс, который объявляет
   методы для создания продуктов (стульев и столов).
4. Конкретные фабрики: ModernFurnitureFactory и ClassicFurnitureFactory — это
   конкретные реализации фабрики, которые создают соответствующие продукты.
5. Клиентский код: В классе AbstractFactoryExample создается экземпляр фабрики,
   и в зависимости от выбранной фабрики создаются соответствующие продукты.

Этот паттерн позволяет легко добавлять новые семейства продуктов, не изменяя
существующий код, что делает его очень гибким и удобным для расширения.

## Структура

![abstract_factory_2.png](/img/design_pattern/design_patterns/abstract_factory_structure.png)

1. Абстрактные продукты объявляют интерфейсы продуктов, которые связаны друг с
   другом по смыслу, но выполняют разные функции.
2. Конкретные продукты — большой набор классов, которые относятся к различным
   абстрактным продуктам (кресло/столик), но имеют одни и те же вариации (
   Викторианский/Модерн).
3. Абстрактная фабрика объявляет методы создания различных абстрактных
   продуктов (кресло/столик).
4. Конкретные фабрики относятся каждая к своей вариации продуктов (
   Викторианский/Модерн) и реализуют методы абстрактной фабрики, позволяя
   создавать все продукты определённой вариации.
5. Несмотря на то, что конкретные фабрики порождают конкретные продукты,
   сигнатуры их методов должны возвращать соответствующие абстрактные продукты.
   Это позволит клиентскому коду, использующему фабрику, не привязываться к
   конкретным классам продуктов. Клиент сможет работать с любыми вариациями
   продуктов через абстрактные интерфейсы.

**_Пример из книги:_**

В этом примере в роли двух семейств продуктов выступают кнопки и чекбоксы. Оба
семейства продуктов имеют одинаковые вариации: для работы под MacOS и Windows.

Абстрактная фабрика задаёт интерфейс создания продуктов всех семейств.
Конкретные фабрики создают различные продукты одной вариации (MacOS или
Windows).

Клиенты фабрики работают как с фабрикой, так и с продуктами только через
абстрактные интерфейсы. Благодаря этому, один и тот же клиентский код может
работать с различными фабриками и продуктами.

```java
// ===== buttons: Первая иерархия продуктов (кнопки) ===== //

// -> buttons/Button.java

/**
 * Паттерн предполагает, что у вас есть несколько семейств продуктов,
 * находящихся в отдельных иерархиях классов (Button/Checkbox). Продукты одного
 * семейства должны иметь общий интерфейс.
 *
 * Это — общий интерфейс для семейства продуктов кнопок.
 */
public interface Button {
    void paint();
}

// -> buttons/MacOSButton.java

/**
 * Все семейства продуктов имеют одни и те же вариации (MacOS/Windows).
 *
 * Это вариант кнопки под MacOS.
 */
public class MacOSButton implements Button {

    @Override
    public void paint() {
        System.out.println("You have created MacOSButton.");
    }
}


// -> buttons/WindowsButton.java

/**
 * Все семейства продуктов имеют одни и те же вариации (MacOS/Windows).
 *
 * Это вариант кнопки под Windows.
 */
public class WindowsButton implements Button {

    @Override
    public void paint() {
        System.out.println("You have created WindowsButton.");
    }
}


// ===== checkboxes: Вторая иерархия продуктов (чекбоксы) ===== //

// -> checkboxes/Checkbox.java

/**
 * Чекбоксы — это второе семейство продуктов. Оно имеет те же вариации, что и
 * кнопки.
 */
public interface Checkbox {
    void paint();
}

// -> checkboxes/MacOSCheckbox.java

/**
 * Все семейства продуктов имеют одинаковые вариации (MacOS/Windows).
 *
 * Вариация чекбокса под MacOS.
 */
public class MacOSCheckbox implements Checkbox {

    @Override
    public void paint() {
        System.out.println("You have created MacOSCheckbox.");
    }
}


// -> checkboxes/WindowsCheckbox.java

/**
 * Все семейства продуктов имеют одинаковые вариации (MacOS/Windows).
 *
 * Вариация чекбокса под Windows.
 */
public class WindowsCheckbox implements Checkbox {

    @Override
    public void paint() {
        System.out.println("You have created WindowsCheckbox.");
    }
}

// -> factories
// -> factories/GUIFactory.java: Абстрактная фабрика

/**
 * Абстрактная фабрика знает обо всех (абстрактных) типах продуктов.
 */
public interface GUIFactory {
    Button createButton();

    Checkbox createCheckbox();
}


// -> factories/MacOSFactory.java: Конкретная фабрика (MacOS)

/**
 * Каждая конкретная фабрика знает и создаёт только продукты своей вариации.
 */
public class MacOSFactory implements GUIFactory {

    @Override
    public Button createButton() {
        return new MacOSButton();
    }

    @Override
    public Checkbox createCheckbox() {
        return new MacOSCheckbox();
    }
}


// -> factories/WindowsFactory.java: Конкретная фабрика (Windows)

/**
 * Каждая конкретная фабрика знает и создаёт только продукты своей вариации.
 */
public class WindowsFactory implements GUIFactory {

    @Override
    public Button createButton() {
        return new WindowsButton();
    }

    @Override
    public Checkbox createCheckbox() {
        return new WindowsCheckbox();
    }
}


// app
// app/Application.java: Клиентский код

/**
 * Код, использующий фабрику, не волнует с какой конкретно фабрикой он работает.
 * Все получатели продуктов работают с продуктами через абстрактный интерфейс.
 */
public class Application {
    private Button button;
    private Checkbox checkbox;

    public Application(GUIFactory factory) {
        button = factory.createButton();
        checkbox = factory.createCheckbox();
    }

    public void paint() {
        button.paint();
        checkbox.paint();
    }
}

// Demo.java: Конфигуратор приложения

/**
 * Демо-класс. Здесь всё сводится воедино.
 */
public class Demo {

    /**
     * Приложение выбирает тип и создаёт конкретные фабрики динамически исходя
     * из конфигурации или окружения.
     */
    private static Application configureApplication() {
        Application app;
        GUIFactory factory;
        String osName = System.getProperty("os.name").toLowerCase();
        if (osName.contains("mac")) {
            factory = new MacOSFactory();
        } else {
            factory = new WindowsFactory();
        }
        app = new Application(factory);
        return app;
    }

    public static void main(String[] args) {
        Application app = configureApplication();
        app.paint();
    }
}
```

## Шаги реализации

1. Создайте таблицу соотношений типов продуктов к вариациям семейств продуктов.
2. Сведите все вариации продуктов к общим интерфейсам.
3. Определите интерфейс абстрактной фабрики. Он должен иметь фабричные методы
   для создания каждого из типов продуктов.
4. Создайте классы конкретных фабрик, реализовав интерфейс абстрактной фабрики.
   Этих классов должно быть столько же, сколько и вариаций семейств продуктов.
5. Измените код инициализации программы так, чтобы она создавала определённую
   фабрику и передавала её в клиентский код.
6. Замените в клиентском коде участки создания продуктов через конструктор
   вызовами соответствующих методов фабрики.