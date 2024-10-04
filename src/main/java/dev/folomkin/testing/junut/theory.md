# JUNIT

Зависимость:

```
<dependency>
        <groupId>org.junit.jupiter</groupId>
        <artifactId>junit-jupiter-engine</artifactId>
        <version>5.9.1</version>
        <scope>test</scope>
</dependency>
```

Чтобы Maven на этапе сборки (test stage) запустил твои тесты, то в pom.xml нужно
добавить еще один фрагмент:

```
<build>
    <plugins>
        <plugin>
            <artifactId>maven-surefire-plugin</artifactId>
            <version>2.19.1</version>
            <dependencies>
               <dependency>
                   <groupId>org.junit.platform</groupId>
                    <artifactId>junit-platform-surefire-provider</artifactId>
                        <version>1.3.2</version>
               </dependency>
            </dependencies>
        </plugin>
    </plugins>
</build>
```

## Аннотация @Test

Тестируемый класс

```java
class Calculator {
    public int add(int a, int b) {
        return a + b;
    }

    public int sub(int a, int b) {
        return a - b;
    }

    public int multiply(int a, int b) {
        return a * b;
    }

    public int divide(int a, int b) {
        return a / b;
    }
}
```

Для тестирования класса создается такой же, но с суффиксом "Test".
Так же тестируется каждый метод.

```java
class CalculatorTest {
    @Test
    void add() {
        Calculator calc = new Calculator();
        int result = calc.add(2, 3);
        assertEquals(5, result);
    }

    @Test
    void sub() {
        Calculator calc = new Calculator();
        int result = calc.sub(10, 10);
        assertEquals(0, result);
    }

    @Test
    void multiply() {
        Calculator calc = new Calculator();
        int result = calc.multiply(-5, -3);
        assertEquals(15, result);
    }

    @Test
    void divide() {
        Calculator calc = new Calculator();
        int result = calc.divide(2, 3);
        assertEquals(0, result);
        
// assertEquals(): сравнивает переданные ему параметры.

    }
}
```

## Настройка окружения теста в JUnit

