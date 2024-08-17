# Обобщения

## Основы

### Обобщенный класс

Класс, интерфейс или метод, оперирующий на параметризованном типе, называется
обобщенным. Принципиальное преимущество обобщенного кода связано с тем, что он
будет автоматически работать с типом данных, переданным в его параметре типа.

```java
// Простой обобщенный класс
// Здесь Т - параметр типа, который будет заменен при создании
// объекта типа Gen
class Gen<T> { // Обобщенный класс, Т - параметр обобщенного типа.
    T ob; // <-- объявлен объект типа Т;

    // Передаем конструктору ссылку на объект типа Т
    Gen(T o) {
        ob = o;
    }

    // Возвратить ob
    T getOb() {
        return ob;
    }

    // Отобразить тип Т
    void showType() {
        System.out.println("Тип T: " + ob.getClass().getName());
    }
}

// Демонстрация использования обобщенного класса
class Code {
    public static void main(String[] args) {
        //Создать ссылку Gen для Integer
        Gen<Integer> iOb;// <-- Создать ссылку на объект типа Gen<Integer>
        Integer i = 88;
        iOb = new Gen<Integer>(i); // Создать объект и присвоить iOb ссылку на него
        // Отобразить тип данных, используемый iOb;
        iOb.showType();
        // Получить значение iOb
        int v = iOb.getOb();
        System.out.println("Значение Integer: " + v);
        System.out.println();
        //Создать объект Gen для String:
        Gen<String> genString = new Gen<String>("Hello");
        genString.showType();
        String s = genString.getOb();
        System.out.println(s);
    }
}

```

Здесь T имя параметра типа. Оно применяется в качестве заполнителя для
фактического типа, который будет передан конструктору Gen при создании объекта.
Таким образом, T используется внутри Gen всякий раз, когда требуется параметр
типа. Обратите внимание, что T содержится внутри угловых скобок <>. Такой
синтаксис можно обобщить. Объявляемый параметр типа всегда указывается в угловых
скобках. Поскольку Gen задействует параметр типа, Gen является обобщенным
классом.

    Gen(T o) {
        ob = o;
    }

Параметр "о" имеет тип T, т.е. фактический тип "о" определяется типом,
переданным в Т, когда создается объект Gen. Поскольку и параметр "о", и
переменная-член ob относятся к типу T, при создании объекта Gen они будут иметь
один и тот же фактический тип.  
При объявлении экземпляра обобщенного типа передаваемый параметру
типа аргумент типа должен быть ссылочным типом.

### Обобщенный класс с двумя параметрами типов

```java
// Простой обобщенный класс с двумя параметрами типов: T и V

class TwoGen<T, V> {
    T ob; // <-- объявлен объект типа Т;
    V ob2;

    // Передаем конструктору ссылку на объект типа Т
    TwoGen(T o, V o2) {
        ob = o;
        ob2 = o2;
    }

    // Возвратить ob
    T getOb() {
        return ob;
    }

    V getOb2() {
        return ob2;
    }

    // Отобразить тип Т
    void showType() {
        System.out.println("Тип T: " + ob.getClass().getName());
        System.out.println("Тип V: " + ob2.getClass().getName());
    }
}

class Code {
    public static void main(String[] args) {
        //Создать ссылку Gen для Integer
        TwoGen<Integer, String> tgObj = new TwoGen<>(88, "Обобщения");

        // Отобразить типы
        tgObj.showType();

        // Получить и отобразить значения:
        int v1 = tgObj.getOb();
        System.out.println("Значение V1: " + v1);
        String v2 = tgObj.getOb2();
        System.out.println("Значение V2: " + v2);

    }
}
```

## Ограниченные типы

```java
// Ограничения типов
class NumericFns<T extends Number> { // Т наследуется от числового типа 
    T num;

    // Передаем конструктору ссылку на числовой объект
    NumericFns(T n) {
        num = n;
    }

    // Возвратить обратную величину
    double reciprocal() {
        return 1 / num.doubleValue();
    }

    // Возвратить дробную часть
    double fraction() {
        return num.doubleValue() - num.intValue();
    }

}

class Code {
    public static void main(String[] args) {
        //Создать ссылку Gen для Integer
        NumericFns<Integer> iOb = new NumericFns<>(5);
        System.out.println("Обратная величина iOb: " + iOb.reciprocal());
        System.out.println("Дробная часть Ob: " + iOb.fraction());
        NumericFns<Double> doubleNumericFns = new NumericFns<>(5.25);
        System.out.println("Обратная величина doubleNumericFns: " + doubleNumericFns.reciprocal());

    }
}

```

### Ограниченные аргументы с подстановочными знаками

