# Адаптер (Adapter)

— это структурный паттерн проектирования, который позволяет объектам с
несовместимыми интерфейсами работать вместе.

Адаптер выступает прослойкой между двумя объектами, превращая вызовы одного в
вызовы понятные другому.

Это объект-переводчик, который трансформирует интерфейс или данные одного
объекта в такой вид, чтобы он стал понятен другому объекту.

При этом адаптер оборачивает один из объектов, так что
другойобъектдаженезнаетоналичиипервого. Например, вы можете обернуть объект,
работающий в метрах, адаптером, который бы конвертировал данные в футы.

Адаптеры могут не только переводить данные из одного формата в другой, но и
помогать объектам с разными интерфейсами работать сообща. Это работает так:

1. Адаптер имеет интерфейс, который совместим с одним из объектов.
2. Поэтому этот объект может свободно вызывать методы адаптера.
3. Адаптер получает эти вызовы и перенаправляет их второму объекту, но уже в том
   формате и последовательности, которые понятны второму объекту.
4. Иногда возможно создать даже двухсторонний адаптер, который работал бы в обе
   стороны.

**Применимость**: Паттерн можно часто встретить в Java-коде, особенно там, где
требуется конвертация разных типов данных или совместная работа классов с
разными интерфейсами.

Примеры Адаптеров в стандартных библиотеках Java:

java.util.Arrays#asList()
java.util.Collections#list()
java.util.Collections#enumeration()
java.io.InputStreamReader(InputStream) (возвращает объект Reader)
java.io.OutputStreamWriter(OutputStream) (возвращает объект Writer)
javax.xml.bind.annotation.adapters.XmlAdapter#marshal() и #unmarshal()

**Признаки применения паттерна**: Адаптер получает конвертируемый объект в
конструкторе или через параметры своих методов. Методы Адаптера обычно
совместимы с интерфейсом одного объекта. Они делегируют вызовы вложенному
объекту, превратив перед этим параметры вызова в формат, поддерживаемый
вложенным объектом.

## Структура

Адаптер объектов

Эта реализация использует агрегацию: объект адаптера
«оборачивает», то есть содержит ссылку на служебный объект. Такой подход
работает во всех языках программирования.

![adapter_structure.png](/img/design_pattern/design_patterns/adapter_object_structure.png)

1. Клиент — это класс, который содержит существующую бизнес-логику программы.
2. Клиентский интерфейс описывает протокол, через который
   клиент может работать с другими классами.
3. Сервис — это какой-то полезный класс, обычно сторонний.
   Клиент не может использовать этот класс напрямую, так как сервис имеет
   непонятный ему интерфейс.
4. Адаптер — это класс, который может одновременно работать и с клиентом, и с
   сервисом. Он реализует клиентский интерфейс и содержит ссылку на объект
   сервиса. Адаптер получает вызовы от клиента через методы клиентского
   интерфейса, а затем переводит их в вызовы методов обёрнутого объекта в
   правильном формате.
5. Работая с адаптером через интерфейс, клиент не привязывается к конкретному
   классу адаптера. Благодаря этому, вы можете добавлять в программу новые виды
   адаптеров, независимо от клиентского кода. Это может пригодиться, если
   интерфейс сервиса вдруг изменwится, например, после выхода новой версии
   сторонней библиотеки.

Адаптер классов

Эта реализация базируется на наследовании: адаптер наследует оба интерфейса
одновременно. Такой подход возможен только в языках, поддерживающих
множественное наследование, например, C++.

![adapter_classes_structure.png](/img/design_pattern/design_patterns/adapter_classes_structure.png)

Адаптер классов не нуждается во вложенном объекте, так
как он может одновременно наследовать и часть существующего класса, и часть
сервиса.

## Примеры кода

В этом примере Адаптер преобразует один интерфейс в другой, позволяя совместить
квадратные колышки и круглые отверстия.

Адаптер вычисляет наименьший радиус окружности, в которую можно вписать
квадратный колышек, и представляет его как круглый колышек с этим радиусом.

![adapter_example.png](/img/design_pattern/design_patterns/adapter_example.png)

```java

// -> round
// -> round/RoundHole.java: Круглое отверстие

package refactoring_guru.adapter.example.round;

/**
 * КруглоеОтверстие совместимо с КруглымиКолышками.
 */
public class RoundHole {
    private double radius;

    public RoundHole(double radius) {
        this.radius = radius;
    }

    public double getRadius() {
        return radius;
    }

    public boolean fits(RoundPeg peg) {
        boolean result;
        result = (this.getRadius() >= peg.getRadius());
        return result;
    }
}

// -> round/RoundPeg.java: Круглый колышек
package refactoring_guru.adapter.example.round;

/**
 * КруглыеКолышки совместимы с КруглымиОтверстиями.
 */
public class RoundPeg {
    private double radius;

    public RoundPeg() {
    }

    public RoundPeg(double radius) {
        this.radius = radius;
    }

    public double getRadius() {
        return radius;
    }
}

// -> square
// -> square/SquarePeg.java: Квадратный колышек
package refactoring_guru.adapter.example.square;

/**
 * КвадратныеКолышки несовместимы с КруглымиОтверстиями (они остались в проекте
 * после бывших разработчиков). Но мы должны как-то интегрировать их в нашу
 * систему.
 */
public class SquarePeg {
    private double width;

    public SquarePeg(double width) {
        this.width = width;
    }

    public double getWidth() {
        return width;
    }

    public double getSquare() {
        double result;
        result = Math.pow(this.width, 2);
        return result;
    }
}


// -> adapters
// -> adapters/SquarePegAdapter.java: Адаптер квадратных колышков к круглым отверстиям
package refactoring_guru.adapter.example.adapters;

import refactoring_guru.adapter.example.round.RoundPeg;
import refactoring_guru.adapter.example.square.SquarePeg;

/**
 * Адаптер позволяет использовать КвадратныеКолышки и КруглыеОтверстия вместе.
 */
public class SquarePegAdapter extends RoundPeg {
    private SquarePeg peg;

    public SquarePegAdapter(SquarePeg peg) {
        this.peg = peg;
    }

    @Override
    public double getRadius() {
        double result;
        // Рассчитываем минимальный радиус, в который пролезет этот колышек.
        result = (Math.sqrt(Math.pow((peg.getWidth() / 2), 2) * 2));
        return result;
    }
}

// -> Demo.java: Клиентский код
package refactoring_guru.adapter.example;

import refactoring_guru.adapter.example.adapters.SquarePegAdapter;
import refactoring_guru.adapter.example.round.RoundHole;
import refactoring_guru.adapter.example.round.RoundPeg;
import refactoring_guru.adapter.example.square.SquarePeg;

/**
 * Где-то в клиентском коде...
 */
public class Demo {
    public static void main(String[] args) {
        // Круглое к круглому — всё работает.
        RoundHole hole = new RoundHole(5);
        RoundPeg rpeg = new RoundPeg(5);
        if (hole.fits(rpeg)) {
            System.out.println("Round peg r5 fits round hole r5.");
        }

        SquarePeg smallSqPeg = new SquarePeg(2);
        SquarePeg largeSqPeg = new SquarePeg(20);
        // hole.fits(smallSqPeg); // Не скомпилируется.

        // Адаптер решит проблему.
        SquarePegAdapter smallSqPegAdapter = new SquarePegAdapter(smallSqPeg);
        SquarePegAdapter largeSqPegAdapter = new SquarePegAdapter(largeSqPeg);
        if (hole.fits(smallSqPegAdapter)) {
            System.out.println("Square peg w2 fits round hole r5.");
        }
        if (!hole.fits(largeSqPegAdapter)) {
            System.out.println("Square peg w20 does not fit into round hole r5.");
        }
    }
}

```

## Шаги реализации

1. Убедитесь, что у вас есть два класса с несовместимыми
   интерфейсами:
    - полезный сервис — служебный класс, который вы не можете изменять (он либо
      сторонний, либо от него зависит другой код);
    - один или несколько клиентов — существующих классов приложения,
      несовместимых с сервисом из-за неудобного или несовпадающего интерфейса.
2. Опишите клиентский интерфейс, через который классы приложения смогли бы
   использовать класс сервиса.
3. Создайте класс адаптера, реализовав этот интерфейс.
4. Поместите в адаптер поле, которое будет хранить ссылку на объект сервиса.
   Обычно это поле заполняют объектом, переданным в конструктор адаптера. В
   случае простой адаптации этот объект можно передавать через параметры методов
   адаптера.
5. Реализуйте все методы клиентского интерфейса в адаптере. Адаптер должен
   делегировать основную работу сервису.
6. Приложение должно использовать адаптер только через клиентский интерфейс. Это
   позволит легко изменять и добавлять адаптеры в будущем.