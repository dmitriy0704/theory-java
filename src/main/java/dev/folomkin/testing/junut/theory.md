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

// assertEquals(): сравнивает переданные ему параметры.}
    }
```

## Настройка окружения теста в JUnit

### Аннотации @BeforeEach, @AfterEach

**@BeforeEach** - метод, помеченный такой аннотацией вызывается перед
каждым тестовым методом.  
**@AfterEach** - метод, помеченный такой аннотацией вызывается после каждого
тестового метода.

Чтобы каждый раз в тестовом методе не создавать новый объект, его можно вынести
в отдельный метод и поставить ему специальную аннотацию @BeforeEach. Тогда Junit
будет вызывать этот метод перед каждым тестовым методом.

Также можно создать специальный метод, который будет вызываться каждый раз после
очередного тестового метода, и подчищать использованные ресурсы, писать что-то в
лог и т.п. Такой метод нужно пометить аннотацией @AfterEach.

## Аннотации @BeforeAll, @AfterAll

**@BeforeAll** - перед всеми тестовыми методами  
**@AfterAll** - после всех тестовых методов

JUnit также позволяет добавить метод, который будет вызван один раз перед всеми
тестовыми методами. Такой метод нужно пометить аннотацией @BeforeAll. Для нее
так же существует парная аннотация @AfterAll. Метод, помеченный ею, JUnit
вызовет после всех тестовых методов.

```java
class CalculatorTest {
    private Calculator calculator = new Calculator();

    @BeforeAll
    public static void init() {
        System.out.println("BeforeAll init() method called");
    }

    @BeforeEach
    public void initEach() {
        System.out.println("BeforeEach initEach() method called");
    }

    @Test
    public void add() {
        System.out.println("Testing Addition");
    }

    @Test
    public void sub() {
        System.out.println("Testing Subtraction");
    }

    @Test
    public void mul() {
        System.out.println("Testing Multiplication");
    }

    @Test
    public void div() {
        System.out.println("Testing Division");
    }

// ->
//    BeforeAll init() method called
//    BeforeEach initEach() method called
//    Testing Addition
//    BeforeEach initEach() method called
//    Testing Division
//    BeforeEach initEach() method called
//    Testing Multiplication
//    BeforeEach initEach() method called
//    Testing Subtraction
}

// ИЛИ

class CalculatorTest {
    private Calculator calculator;

    @BeforeEach
    void init() {
        calculator = new Calculator();
    }

    @Test
    void add() {
        int result = calculator.add(2, 3);
        assertEquals(5, result);
    }
}
```

## Полезные аннотации в JUnit

### @Disabled

Аннотация позволяет выключить определенный тест, чтобы JUnit его не вызывал.
Она нужна в случаях, если вдруг тест стал работать неверно, или поменялся код
и тест случайно поломается.

Записывается перед методом или классом.

```java
public class AppTest {
    @Disabled("Тест временно отключен")
    @Test
    void testOnDev() {
        System.setProperty("ENV", "DEV");
        Assumptions.assumeFalse("DEV".equals(System.getProperty("ENV")));
    }

    @Test
    void testOnProd() {
        System.setProperty("ENV", "PROD");
        Assumptions.assumeFalse("DEV".equals(System.getProperty("ENV")));
    }
}
```

## @Nested

JUnit позволяет вызывать тестовые методы у вложенных классов. Имеется в виду
вложенные тестовые классы.

Чтобы вызвать методы вложенного класса перед его объявлением, нужно написать
аннотацию @Nested. Пример:

```java
public class AppTest {
    @Nested
    public class DevStagingEnvironment {
        @Test
        void testOnDev() {
            System.setProperty("ENV", "DEV");
            Assumptions.assumeFalse("DEV".equals(System.getProperty("ENV")));
        }
    }

    @Nested
    public class ProductionEnvironment {
        @Test
        void testOnProd() {
            System.setProperty("ENV", "PROD");
            Assumptions.assumeFalse("DEV".equals(System.getProperty("ENV")));
        }
    }
}
```

### @ExtendWith

JUnit — это мощный фреймворк, который позволяет писать различные плагины (
расширения) для гибкой настройки своей работы. Некоторые расширения могут
собирать статистику о тестах, другие — эмулировать файловую систему в памяти,
третьи — эмулировать работу внутри веб-сервера, и так далее.  
Если твой код работает внутри какого-нибудь фреймворка (например Spring), то
почти всегда этот фреймворк управляет созданием и настройкой объектов твоего
кода. Поэтому без специального тестового плагина не обойтись. Примеры:

Пример 1. Расширение WebServerExtension передает в вызываемый тестовый метод URL
для корректной работы.

```java

@Test
@ExtendWith(WebServerExtension.class)
void getProductList(@WebServerUrl String serverUrl) {
    WebClient webClient = new WebClient();
    // Use WebClient to connect to web server using serverUrl and verify response
    assertEquals(200, webClient.get(serverUrl + "/products").getResponseStatus());
}
```

Вот так обычно начинаются тесты для тестирования кода, который работает с
фреймворком Spring:

```java

@ExtendWith(SpringExtension.class)
@ExtendWith(MockitoExtension.class)
class TestServiceTest {

    @MockBean
    TestService service;

    @Test
    void test() {
        assertNotNull(service); // Test succeeds
    }
}
```

SpringExtension создает тестовый вариант фреймворка Spring, а MockitoExtention
позволяет создавать фейковые объекты. Фейковые объекты — тема очень интересная,
мы обязательно ее затронем, но немного позже.

### @Timeout

Позволяет задать время на выполнение теста. Если выполнение теста заняло больше
времени, чем указанно в аннотации, то он считается проваленным.

```java
class TimeoutDemo {
    @Test
    @Timeout(value = 100, unit = TimeUnit.MILLISECONDS)
    void failsIfExecutionTimeExceeds100Milliseconds() {
        // тест упадет, если займет более 100 миллисекунд
    }
}
```

## Параметризованные тесты в Junit

### Аннотация @ParameterizedTest

Чтобы вызвать тест несколько раз с различными параметрами: разные значения,
разные входные параметры, разные имена пользователей.
Для использования параметризованных тестов надо добавить:

```java
<dependency>
    <groupId>org.junit.jupiter</groupId>
    <artifactId>junit-jupiter-params</artifactId>
    <version>5.8.2</version>
    <scope>test</scope>
</dependency>
```

```java

@ParameterizedTest
@ValueSource(ints = {1, 2, 3})
void testMethod(int argument) {
    //test code
}

@ParameterizedTest
@ValueSource(ints = {1, 2, 3})
void testMethodWithAutoboxing(Integer argument) {
    //test code
}
```

Каждый тестовый метод вызовется по 3 раза и при каждом вызове в него будет
передан очередной параметр. Значения задаются с помощью другой аннотации —
@ValueSource. Но о ней нужно рассказать подробнее.

### Аннотация @ValueSource

Аннотация @ValueSource отлично подходит для работы с примитивами и литералами.
Просто перечисли значения параметра через запятую и тест будет вызван по одному
разу для каждого значения.

```java

@ParameterizedTest
@ValueSource(ints = {1, 2, 3})
void testMethodWithAutoboxing(Integer argument) {
    //test code
}
```

Но есть и минус — эта аннотация не поддерживает null, так что для него нужно
будет использовать специальный костыль — @NullSource. Выглядит это так:

```java

@ParameterizedTest
@NullSource
void testMethodNullSource(Integer argument) {
    assertTrue(argument == null);
}
```

### Аннотация @EnumSource

Также есть специальная аннотация @EnumSource, которая передает в метод все
значения определенного Enum’а. Ее применение выглядит так:

```java
enum Direction {
    EAST, WEST, NORTH, SOUTH
}

@ParameterizedTest
@EnumSource(Direction.class)
void testWithEnumSource(Direction d) {
    assertNotNull(d);
}
```

### Аннотация @MethodSource

А как же передавать в качестве параметров объекты? Особенно, если они имеют
сложный алгоритм построения. Для этого можно просто указать специальный
вспомогательный метод, который будет возвращать список (Stream) из таких
значений.  
Пример:

```java

@ParameterizedTest
@MethodSource("argsProviderFactory")
void testWithMethodSource(String argument) {
    assertNotNull(argument);
}

static Stream<String> argsProviderFactory() {
    return Stream.of("один", "два", "три");
}
```

### Параметризованные тесты с несколькими аргументами

Конечно вы уже задались вопросом, а что делать, если в метод хочется передать
несколько параметров. Для этого есть очень классная аннотация @CSVSource. Она
позволяет перечислить значения параметров метода просто через запятую.  
Пример

```java

@ParameterizedTest
@CsvSource({
        "alex, 30, Программист, Работает",
        "brian, 35, Тестировщик, Работает",
        "charles, 40, Менеджер, Пинает"
})
void testWithCsvSource(String name, int age, String occupation, String status) {
    //код метода
}
```

## JUnit Assertions

### assert’ы

Ассерты (asserts) — это специальные проверки, которые можно вставить в разные
места кода. Их задача определять, что что-то пошло не так. Вернее, проверять,
что все идет как нужно. Вот это “как нужно” они и позволяют задать различными
способами.  
С некоторыми ассертами ты уже сталкивался в коде выше. Первый из них – проверка
объектов на равенство. Если объекты не равны — кинется исключение и тест будет
провален.  
Тут важен порядок сравнения, ведь JUnit в итоговом отчете напишет что-то типа
“получено значение 1, а ожидалось 3”. Общий формат такой проверки имеет вид:
`assertEquals(эталон, значение)`.  
Пример:

```java

@Test
public void whenAssertingEquality() {
    String expected = "3.1415";
    String actual = "3";
    assertEquals(expected, actual);
}
```

### Методы assertEquals, assertTrue, assertFalse

- assertEquals - Проверяет, что два объекта равны
- assertArrayEquals - Проверяет, что два массива содержат равные значения
- assertNotNull - Проверяет, что аргумент не равен null
- assertNull - Проверяет, что аргумент равен null
- assertNotSame - Проверят, что два аргумента — это не один и тот же объект
- assertSame - Проверят, что два аргумента — это один и тот же объект
- assertTrue - Проверяет, что аргумент равен true
- assertFalse - Проверяет, что аргумент равен false

Некоторые из этих методов кажутся излишними. Зачем использовать assertSame(a,
b), если можно просто написать assertTrue(a == b)?
Все дело в том, что assert — это очень умный метод. Он делает много чего
полезного и, в том числе, пишет в лог информацию об ошибке. В первом случае он
напишет, что ожидался объект А, а получен объект Б. Во втором случае просто
напишет, что ожидалось true.
Когда у тебя сотни тестов, особенно выполняемые на специальном тестовом сервере,
то наличие детальных логов может быть суперполезным. Думаю,ты понимаешь, о чем
я.

Пример сравнения массивов:

```java

@Test
public void whenAssertingArraysEquality() {
    char[] expected = {'J', 'u', 'n', 'i', 't'};
    char[] actual = "Junit".toCharArray();

    assertArrayEquals(expected, actual);
}
```

### Метод assertAll

Как уже говорилось выше, метод assert не просто выполняет проверку, но и пишет в
лог много информации о сравнимых объектах.

Допустим выполняется сравнение:

```
Address address = unitUnderTest.methodUnderTest();
assertEquals("Вашингтон", address.getCity());
assertEquals("Oracle Parkway", address.getStreet());
assertEquals("500", address.getNumber());
```

Но если один из параметров не совпадет, то проверки остальных не произойдет. А
хотелось бы чтобы они все-таки произошли и их результаты записались в лог. Но
при этом, если хотя бы одна проверка не прошла, то тест все-таки был провален.

Для этого есть специальный метод — assertAll(). В качестве первого аргумента он
принимает комментарий, который нужно записать в лог, а дальше — любое количество
функций-ассертов.

Вот как будет переписан наш пример с его помощью:

```
Address address = unitUnderTest.methodUnderTest();
assertAll("Сложный сценарий сравнение адреса",
    () -> assertEquals("Вашингтон", address.getCity()),
    () -> assertEquals("Oracle Parkway", address.getStreet()),
    () -> assertEquals("500", address.getNumber())
);
```

Тогда если адрес будет неправильный, в лог будет написано что-то типа:

	Сложный сценарий сравнение адреса (3 failures)
	expected: <Вашингтон> but was: <Сиэтл>
    expected: <Oracle Parkway> but was: <Main Street>
    expected: <500> but was: <5772>

### Метод assertTimeout

Помнишь аннотацию @Timeout? Она позволяла контролировать время выполнения всего
метода. Но иногда бывает полезно задать ограничения на выполнения некоторой
части кода внутри метода. Для этого можно использовать assertTimeout().

В качестве первого параметра в него передается время, а в качестве второго —
код (функция), который должен выполниться за указанное время. Пример:

```java

@Test
public void whenAssertingTimeout() {
    assertTimeout(
            ofSeconds(2),
            () -> {
                // пауза в одну секунду
                Thread.sleep(1000);
            }
    );
}
```

### Метод assertThrows

Очень часто бывают ситуации, когда тебе нужно убедиться, что в определенной
ситуации код кидает нужное исключение: определил ошибку и кинул нужное
исключение. Это очень распространенная ситуация.

На этот случай есть еще один полезный метод assert — это assertThrows(). Общий
формат его вызова имеет вид:
`assertThrows(исключение, код)`  
По сути, он очень похож на метод assertTimeout(), только он проверяет, чтобы
указанный код выкинул нужное исключение.

```java
@Test
void whenAssertingException() {
    Throwable exception = assertThrows(
            IllegalArgumentException.class,
            () -> {
                throw new IllegalArgumentException("Exception message");
            }
    );
    assertEquals("Exception message", exception.getMessage());
}
```


## Популярные аннотации в JUnit

### Аннотация @Suite
И еще несколько часто используемых аннотаций. Даже если ты не будешь их использовать, то точно увидишь тесты с ними. Поэтому нужно хотя бы в общих чертах понимать, что там написано.

Тесты можно объединять в группы. Для этого есть специальная аннотация @Suite. Пример:

```java

@Suite
@SuiteDisplayName("JUnit Platform Suite Demo")
@SelectPackages("com.javarush.test")
@IncludeClassNamePatterns(".*Tests")
class SuiteDemo {
}
```

В данном случае аннотация @Suite работает в паре с другими аннотациями.

SuiteDisplayName – задает имя группы тестов в логе;
SelectPackages – задает список пакетов, где искать классы-тесты;
IncludeClassNamePatterns – задает шаблон имен классов-тестов.
Зачем такие сложности? Ну представь, что все тесты проекта выполняются, скажем, 50 часов. И выполнять их после каждого коммита очень накладно. В таком случае полезно создать некоторые отдельные сценарии тестирования и настроить тестирование гораздо гибче.

### Аннотация @Order
Еще одна часто встречающаяся аннотация – @TestMethodOrder. Она позволяет задать порядок вызова тестовых методов в тестовом классе. Бывает очень полезна, когда вы знаете, что вызовы методов влияют друг на друга, но при определенном порядке все должно работать, как нужно. Используется довольно часто.

Во-первых, можно задать вызов методов в алфавитном порядке:

```java
@TestMethodOrder(MethodOrderer.MethodName.class)
public class AlphanumericOrderUnitTest {

}
```

Во-вторых, у каждого метода можно расставить специальную аннотацию с его порядковым номером.

```java
@TestMethodOrder(OrderAnnotation.class)
public class OrderAnnotationUnitTest {

    @Test
    @Order(1)
    public void firstTest() {
    }

    @Test
    @Order(2)
    public void secondTest() {
    }
}
```

Или даже создать отдельный класс, который будет указывать порядок вызова тестов:

```java
@TestMethodOrder(CustomOrder.class)
public class CustomOrderUnitTest {
}

//сортируем имена методов по алфавитному порядку, но игнорируя регистр
public class CustomOrder implements MethodOrderer {
public void orderMethods(MethodOrdererContext context) {
    context.getMethodDescriptors().sort(
        (MethodDescriptor m1, MethodDescriptor m2)->
        m1.getMethod().getName().compareToIgnoreCase(m2.getMethod().getName()));
    }
}
```

### Аннотация @DisplayName
И наконец, каждому тесту можно задавать его имя. Бывает удобно, если тестов очень много и вы создаете специальные сценарии (подмножества) тестов. Для этого есть специальная аннотация @DisplayName.

Пример:

```java
@DisplayName("Понятное имя для теста")
public class DisplayNameCustomTest {

    @Test
    @DisplayName("Проверка входных данных")
    void inputData() {
    }

    @DisplayName("Проверка критических ситуаций")
    @Test
    void criticalCases() {
    }
}
```

Как и в случае с заданием порядка тестов, тут можно создать специальный метод, который будет генерировать имена тестов и тестовых методов. Пример:

```java
@DisplayNameGeneration(DisplayNameGeneratorUnitTest.ReplaceCamelCase.class)
class DisplayNameGeneratorUnitTest {

    @Test
    void camelCaseName() {
    }

    static class ReplaceCamelCase extends DisplayNameGenerator.Standard {
        @Override
        public String generateDisplayNameForClass(Class testClass) {
            return super.generateDisplayNameForClass(testClass).toUpperCase();
        }
}
}
```

