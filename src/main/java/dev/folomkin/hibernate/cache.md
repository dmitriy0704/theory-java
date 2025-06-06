# HIBERNATE

================================================================================
--------------------------------------------------------------------------------

# Проблема n+1

Проблема N+1 в Hibernate — это распространённая проблема производительности при
работе с ORM (Object-Relational Mapping), связанная с чрезмерным количеством
SQL-запросов, выполняемых для получения связанных данных. Давайте разберёмся
подробнее.

### Что такое проблема N+1?

Проблема N+1 возникает, когда для загрузки данных из базы выполняется один
запрос для получения основного набора сущностей (1 запрос) и затем по одному
дополнительному запросу для каждой связанной сущности (N запросов), что в сумме
даёт N+1 запросов. Это происходит из-за ленивой загрузки (lazy loading) или
неправильной настройки выборки данных.

### Пример

Предположим, у нас есть две сущности: `Order` (заказ) и `Customer` (клиент),
связанные отношением One-to-Many. Каждый заказ принадлежит одному клиенту, и мы
хотим получить список всех заказов и их клиентов.

```java

@Entity
public class Order {
    @Id
    private Long id;
    private String orderNumber;

    @ManyToOne
    private Customer customer;
}

@Entity
public class Order {
    @Id
    private Long id;
    private String name;

    @OneToMany(mappedBy = "customer")
    private List<Order> orders;
}
```

Если в коде мы выполняем следующий запрос:

```java
void demo() {
    List<Order> orders = session.createQuery("FROM Order", Order.class).getResultList();
    for (Order order : orders) {
        System.out.println(order.getCustomer().getName());
    }
}

```

Hibernate по умолчанию использует ленивую загрузку (`lazy loading`) для связи
`@ManyToOne`. Это означает, что:

1. Один запрос (`1`) выполняется для получения всех заказов (
   `SELECT * FROM Order`).
2. Для каждого заказа при обращении к `order.getCustomer()` выполняется
   отдельный запрос (`N` запросов) для загрузки связанного клиента (
   `SELECT * FROM Customer WHERE id = ?`).

Если у нас, например, 100 заказов, это приведёт к 1 (для заказов) + 100 (для
клиентов) = 101 запросу к базе данных. Это и есть проблема N+1.

### Почему это проблема?

- **Производительность**: Множество мелких запросов создают большую нагрузку на
  базу данных, увеличивая время выполнения и потребление ресурсов.
- **Задержки**: Каждый запрос имеет накладные расходы (сетевые задержки,
  обработка на стороне базы), что замедляет приложение.
- **Скалируемость**: При увеличении объёма данных проблема становится более
  выраженной.

### Как обнаружить проблему N+1?

1. **Логи SQL-запросов**: Включите логирование SQL в Hibernate (например, через
   `hibernate.show_sql=true` и `hibernate.format_sql=true`) и ищите
   повторяющиеся запросы.
2. **Профилировщики**: Используйте инструменты, такие как Hibernate Statistics,
   DataSource Proxy или внешние профилировщики (например, New Relic, YourKit),
   чтобы отслеживать количество запросов.
3. **Тестирование**: Тестируйте запросы с реальными данными, чтобы увидеть,
   сколько запросов генерируется.

### Решения проблемы N+1

1. **Eager Loading (жадная загрузка)**:
   Установите тип загрузки связи на `EAGER` вместо `LAZY`:

   ```java
   @ManyToOne(fetch = FetchType.EAGER)
   private Customer customer;
   ```

   Это заставит Hibernate загружать связанные сущности сразу в одном запросе с
   использованием `JOIN`. Однако жадная загрузка может привести к загрузке
   лишних данных, если связи сложные.

2. **Fetch Join в запросах**:
   Используйте `JOIN FETCH` в HQL или Criteria API, чтобы указать, что связанные
   данные должны быть загружены в одном запросе:

   ```java
   List<Order> orders = session.createQuery("FROM Order o JOIN FETCH o.customer", Order.class).getResultList();
   ```

   Это создаст один SQL-запрос с `JOIN`, который загрузит заказы и их клиентов
   одновременно.

3. **Batch Fetching**:
   Настройте пакетную загрузку с помощью аннотации `@BatchSize`:

   ```java
   @ManyToOne
   @BatchSize(size = 10)
   private Customer customer;
   ```

   Это позволит Hibernate загружать связанные сущности группами (например, по 10
   клиентов за раз), сокращая количество запросов (вместо N запросов будет
   N/10).

4. **Entity Graph**:
   Используйте аннотацию `@NamedEntityGraph` или `EntityGraph` API для точного
   управления загрузкой связей:

   ```java
   @Entity
   @NamedEntityGraph(name = "Order.customer", attributeNodes = @NamedAttributeNode("customer"))
   public class Order { ... }

   EntityGraph graph = session.getEntityGraph("Order.customer");
   List<Order> orders = session.createQuery("FROM Order", Order.class)
                               .setHint("javax.persistence.fetchgraph", graph)
                               .getResultList();
   ```

   Это позволяет гибко указывать, какие связи загружать.

5. **DTO Projections**:
   Вместо загрузки полных сущностей используйте DTO (Data Transfer Objects) для
   выборки только необходимых данных:

   ```java
   List<Object[]> orders = session.createQuery("SELECT o.orderNumber, c.name FROM Order o JOIN o.customer c").getResultList();
   ```

   Это уменьшает объём данных и количество запросов.

6. **Кэширование**:
   Используйте кэш второго уровня (Second Level Cache) в Hibernate для хранения
   часто запрашиваемых данных, чтобы избежать повторных запросов к базе.

### Рекомендации

- **Анализируйте сценарии**: Проблема N+1 зависит от контекста. Иногда ленивая
  загрузка предпочтительна, если связанные данные нужны не всегда.
- **Тестируйте производительность**: Проверяйте запросы на реальных данных,
  чтобы убедиться, что оптимизация работает.
- **Избегайте EAGER по умолчанию**: Это может привести к загрузке ненужных
  данных и снижению производительности.
- **Используйте инструменты мониторинга**: Следите за производительностью базы
  данных и количеством запросов.

### Итог

Проблема N+1 в Hibernate возникает из-за ленивой загрузки и приводит к
чрезмерному количеству SQL-запросов. Её можно решить с помощью жадной загрузки,
`JOIN FETCH`, пакетной загрузки, Entity Graphs или DTO. Выбор подхода зависит от
конкретного сценария использования и структуры данных. Для предотвращения
проблемы важно анализировать SQL-запросы и тестировать приложение на реальных
данных.

---------------

## Пример проблемы N+!

Вот конкретный пример проблемы N+1 в Hibernate с пояснением.

### Модель данных

Предположим, у нас есть две сущности: `Book` (книга) и `Author` (автор),
связанные отношением Many-to-One. Каждая книга принадлежит одному автору.

```java

@Entity
public class Book {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String title;

    @ManyToOne(fetch = FetchType.LAZY) // Ленивая загрузка по умолчанию
    private Author author;

    // Геттеры и сеттеры
}

@Entity
public class Author {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;

    @OneToMany(mappedBy = "author")
    private List<Book> books;

    // Геттеры и сеттеры
}
```

### Сценарий

Мы хотим получить список всех книг и вывести имена их авторов. Код выглядит так:

```java
void demo() {
    Session session = sessionFactory.openSession();
    List<Book> books = session.createQuery("FROM Book", Book.class).getResultList();
    for (Book book : books) {
        System.out.println("Book: " + book.getTitle() + ", Author: " + book.getAuthor().getName());
    }
    session.close();
}
```

### Проблема N+1

1. **Первый запрос (1)**: Hibernate выполняет запрос для получения всех книг:
   ```sql
   SELECT * FROM Book
   ```
   Допустим, в базе 10 книг.

2. **N запросов**: Поскольку связь `@ManyToOne` настроена на `FetchType.LAZY`,
   при каждом обращении к `book.getAuthor().getName()` Hibernate выполняет
   отдельный запрос для загрузки автора:
   ```sql
   SELECT * FROM Author WHERE id = ?
   ```
   Для 10 книг это будет 10 дополнительных запросов (по одному на каждую книгу).

**Итог**: 1 запрос (для книг) + 10 запросов (для авторов) = 11 запросов. Это и
есть проблема N+1, где N — количество книг.

### Логи SQL (пример)

Если включить логирование SQL (`hibernate.show_sql=true`), вы увидите что-то
вроде:

```sql
-- Запрос для книг
SELECT id, title, author_id
FROM Book

-- Запросы для авторов (по одному на книгу)
SELECT id, name
FROM Author
WHERE id = 1
SELECT id, name
FROM Author
WHERE id = 2 ...
SELECT id, name
FROM Author
WHERE id = 10
```

### Решение проблемы

Чтобы избежать N+1, можно использовать `JOIN FETCH` в запросе:

```java
List<Book> books = session.createQuery("FROM Book b JOIN FETCH b.author", Book.class).getResultList();
for(
Book book :books){
        System.out.

println("Book: "+book.getTitle() +", Author: "+book.

getAuthor().

getName());
        }
```

**Результат**: Один SQL-запрос с объединением:

```sql
SELECT b.id, b.title, b.author_id, a.id, a.name
FROM Book b
         INNER JOIN Author a ON b.author_id = a.id
```

Теперь все данные загружаются одним запросом, и проблема N+1 устранена.

### Альтернативное решение с Entity Graph

```java

@Entity
@NamedEntityGraph(name = "Book.author", attributeNodes = @NamedAttributeNode("author"))
public class Book { ...
}

EntityGraph graph = session.getEntityGraph("Book.author");
List<Book> books = session.createQuery("FROM Book", Book.class)
        .setHint("javax.persistence.fetchgraph", graph)
        .getResultList();
```

**Результат**: Тот же единый запрос с `JOIN`, но с более гибкой настройкой через
Entity Graph.

### Итог

В примере без оптимизации Hibernate выполнил бы 11 запросов (1 + 10). С
использованием `JOIN FETCH` или `Entity Graph` количество запросов сокращается
до 1, что значительно улучшает производительность.


> Два сценария проблемы N+1

Проблема N+1 в Hibernate может возникнуть в обоих сценариях, в зависимости от
того, как вы запрашиваете данные и какие связи загружаются. Давайте разберём оба
случая на примере сущностей `Author` (автор) и `Book` (книга) с отношением
One-to-Many, чтобы показать, когда и как возникает проблема N+1.

### Модель данных

```java

@Entity
public class Author {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;

    @OneToMany(mappedBy = "author", fetch = FetchType.LAZY)
    private List<Book> books;

    // Геттеры и сеттеры
}

@Entity
public class Book {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String title;

    @ManyToOne(fetch = FetchType.LAZY)
    private Author author;

    // Геттеры и сеттеры
}
```

Предположим, в базе данных:

- 2 автора (Author).
- У первого автора 3 книги, у второго — 2 книги (всего 5 книг).

### Сценарий 1: 1 запрос на список авторов + N запросов на книги

**Код**:

```java
Session session = sessionFactory.openSession();
List<Author> authors = session.createQuery("FROM Author", Author.class).getResultList();
for(
Author author :authors){
        for(
Book book :author.

getBooks()){
        System.out.

println("Author: "+author.getName() +", Book: "+book.

getTitle());
        }
        }
        session.

close();
```

**Что происходит**:

1. **1 запрос**: Hibernate выполняет запрос для получения всех авторов:
   ```sql
   SELECT id, name FROM Author
   ```
   Это возвращает 2 авторов.

2. **N запросов**: Поскольку связь `@OneToMany` настроена на `FetchType.LAZY`,
   при обращении к `author.getBooks()` для каждого автора Hibernate выполняет
   отдельный запрос для загрузки его книг:
   ```sql
   SELECT id, title, author_id FROM Book WHERE author_id = ?
   ```
   Для 2 авторов это будет 2 запроса (N = количество авторов).

**Итог**: 1 (для авторов) + 2 (для книг каждого автора) = 3 запроса.

**Когда возникает**:

- Проблема N+1 возникает, если вы сначала запрашиваете авторов, а затем для
  каждого автора лениво загружаете его книги.
- N — это количество авторов.

### Сценарий 2: 1 запрос на список всех книг + N запросов на авторов

**Код**:

```java
Session session = sessionFactory.openSession();
List<Book> books = session.createQuery("FROM Book", Book.class).getResultList();
for(
Book book :books){
        System.out.

println("Book: "+book.getTitle() +", Author: "+book.

getAuthor().

getName());
        }
        session.

close();
```

**Что происходит**:

1. **1 запрос**: Hibernate выполняет запрос для получения всех книг:
   ```sql
   SELECT id, title, author_id FROM Book
   ```
   Это возвращает 5 книг.

2. **N запросов**: Поскольку связь `@ManyToOne` настроена на `FetchType.LAZY`,
   при обращении к `book.getAuthor().getName()` для каждой книги Hibernate
   выполняет отдельный запрос для загрузки автора:
   ```sql
   SELECT id, name FROM Author WHERE id = ?
   ```
   Для 5 книг это будет 5 запросов (N = количество книг). Однако, если несколько
   книг принадлежат одному автору, Hibernate может кэшировать авторов в сессии,
   но без кэша второго уровня это всё равно может быть 5 запросов.

**Итог**: 1 (для книг) + 5 (для авторов каждой книги) = 6 запросов.

**Когда возникает**:

- Проблема N+1 возникает, если вы сначала запрашиваете книги, а затем для каждой
  книги лениво загружаете её автора.
- N — это количество книг.

### Сравнение сценариев

| Сценарий               | 1 запрос   | N запросов               | Общее количество запросов (в примере) |
|------------------------|------------|--------------------------|---------------------------------------|
| Список авторов → книги | Все авторы | Книги для каждого автора | 1 + 2 = 3                             |
| Список книг → авторы   | Все книги  | Автор для каждой книги   | 1 + 5 = 6                             |

### Почему различается количество запросов?

- В **первом сценарии** N меньше, так как авторов обычно меньше, чем книг (2
  автора vs 5 книг).
- Во **втором сценарии** N больше, так как каждая книга вызывает запрос на
  автора, даже если некоторые книги принадлежат одному автору.

### Решение с `JOIN FETCH`

Чтобы избежать N+1 в обоих случаях, используйте `JOIN FETCH`:

1. **Для авторов и их книг**:
   ```java
   List<Author> authors = session.createQuery("FROM Author a JOIN FETCH a.books", Author.class).getResultList();
   ```
   SQL:
   ```sql
   SELECT a.id, a.name, b.id, b.title, b.author_id
   FROM Author a
   LEFT JOIN Book b ON b.author_id = a.id
   ```
   Один запрос загружает всех авторов и их книги.

2. **Для книг и их авторов**:
   ```java
   List<Book> books = session.createQuery("FROM Book b JOIN FETCH b.author", Book.class).getResultList();
   ```
   SQL:
   ```sql
   SELECT b.id, b.title, b.author_id, a.id, a.name
   FROM Book b
   INNER JOIN Author a ON b.author_id = a.id
   ```
   Один запрос загружает все книги и их авторов.

### Итог

Проблема N+1 возникает в обоих случаях:

- **1 запрос на авторов + N запросов на книги**: Когда вы запрашиваете авторов,
  а затем лениво загружаете их книги (N = количество авторов).
- **1 запрос на книги + N запросов на авторов**: Когда вы запрашиваете книги, а
  затем лениво загружаете их авторов (N = количество книг).

Второй сценарий чаще создаёт больше запросов, так как книг обычно больше, чем
авторов. Использование `JOIN FETCH` устраняет проблему, загружая все данные в
одном запросе.


> Join fetch для решения проблемы n+1

Чтобы показать, как выглядит результат запроса с использованием `JOIN FETCH` для
решения проблемы N+1 в Hibernate, продолжим с примером из предыдущего ответа,
где у нас есть сущности `Book` и `Author` с отношением Many-to-One. Я опишу, как
`JOIN FETCH` влияет на SQL-запрос и результат, а также приведу пример.

### Исходные сущности

```java

@Entity
public class Book {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String title;

    @ManyToOne(fetch = FetchType.LAZY)
    private Author author;

    // Геттеры и сеттеры
}

@Entity
public class Author {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;

    @OneToMany(mappedBy = "author")
    private List<Book> books;

    // Геттеры и сеттеры
}
```

### Проблема N+1 (без `JOIN FETCH`)

Если мы используем запрос без `JOIN FETCH`:

```java
void demo() {
    List<Book> books = session.createQuery("FROM Book", Book.class).getResultList();
    for (Book book : books) {
        System.out.println(
                "Book: " + book.getTitle() + ", Author: " + book.getAuthor().getName()
        );
    }
}
```

Hibernate выполнит:

1. Один запрос для получения всех книг:
   ```sql
   SELECT id, title, author_id FROM Book
   ```
2. N запросов (по одному на каждую книгу) для загрузки авторов:
   ```sql
   SELECT id, name FROM Author WHERE id = ?
   ```

Для 10 книг это будет 1 + 10 = 11 запросов.

### Использование `JOIN FETCH`

Теперь применим `JOIN FETCH` для решения проблемы N+1:

```java
List<Book> books = session.createQuery("FROM Book b JOIN FETCH b.author", Book.class).getResultList();
for(
Book book :books){
        System.out.

println("Book: "+book.getTitle() +", Author: "+book.

getAuthor().

getName());
        }
```

### Как выглядит SQL-запрос с `JOIN FETCH`?

`JOIN FETCH` заставляет Hibernate загрузить связанные сущности (`Author`) в
одном запросе с использованием SQL `INNER JOIN`. Результирующий SQL-запрос будет
выглядеть примерно так:

```sql
SELECT b.id, b.title, b.author_id, a.id, a.name
FROM Book b
         INNER JOIN Author a ON b.author_id = a.id
```

### Разбор SQL-запроса

- **`b.id, b.title, b.author_id`**: Поля из таблицы `Book`.
- **`a.id, a.name`**: Поля из таблицы `Author`.
- **`INNER JOIN Author a ON b.author_id = a.id`**: Объединение таблиц `Book` и
  `Author` по ключу `author_id` (внешний ключ в таблице `Book`).

Этот запрос возвращает все книги и их авторов в одном результате, исключая
необходимость дополнительных запросов для загрузки авторов.

### Пример результата

Предположим, в базе данных есть следующие данные:

**Таблица Book**:

| id | title             | author_id |
|----|-------------------|-----------|
| 1  | "Java Basics"     | 1         |
| 2  | "Spring Guide"    | 1         |
| 3  | "Hibernate Intro" | 2         |

**Таблица Author**:

| id | name         |
|----|--------------|
| 1  | "John Doe"   |
| 2  | "Jane Smith" |

Результат SQL-запроса с `JOIN FETCH` будет выглядеть так:

```sql
SELECT b.id, b.title, b.author_id, a.id, a.name
FROM Book b
         INNER JOIN Author a ON b.author_id = a.id
```

**Результат в виде таблицы**:

| b.id | b.title         | b.author_id | a.id | a.name     |
|------|-----------------|-------------|------|------------|
| 1    | Java Basics     | 1           | 1    | John Doe   |
| 2    | Spring Guide    | 1           | 1    | John Doe   |
| 3    | Hibernate Intro | 2           | 2    | Jane Smith |

### Что получает Java-код?

Hibernate преобразует результат SQL в объекты `Book`, где каждый объект `Book`
уже содержит инициализированный объект `Author`. В Java это будет выглядеть так:

```java
Book [id=1,title="Java Basics",author=Author [id=1,name="John Doe"]]
Book [id=2,title="Spring Guide",author=Author [id=1,name="John Doe"]]
Book [id=3,title="Hibernate Intro",author=Author [id=2,name="Jane Smith"]]
```

При обращении к `book.getAuthor().getName()` дополнительные запросы не
выполняются, так как данные авторов уже загружены.

### Особенности `JOIN FETCH`

1. **Один запрос**: Вместо N+1 запросов выполняется только один.
2. **Тип JOIN**: `JOIN FETCH` использует `INNER JOIN` по умолчанию, поэтому
   будут возвращены только книги, у которых есть автор. Для включения книг без
   авторов можно использовать `LEFT JOIN FETCH`.
3. **Дублирование данных**: Если автор написал несколько книг, его данные (
   например, `id` и `name`) будут повторяться в результате для каждой книги.
   Hibernate корректно обрабатывает это, создавая один объект `Author` в памяти.
4. **Ограничения**: Если в запросе несколько `JOIN FETCH` для разных связей, это
   может усложнить SQL и привести к большому объёму данных (декартово
   произведение).

### Альтернатива с `LEFT JOIN FETCH`

Если нужно включить книги без авторов:

```java
List<Book> books = session.createQuery("FROM Book b LEFT JOIN FETCH b.author", Book.class).getResultList();
```

SQL будет:

```sql
SELECT b.id, b.title, b.author_id, a.id, a.name
FROM Book b
         LEFT OUTER JOIN Author a ON b.author_id = a.id
```

Это вернёт все книги, даже если у них нет автора (поля `a.id` и `a.name` будут
`NULL`).

### Итог

Использование `JOIN FETCH` решает проблему N+1, заменяя N+1 запросов одним
SQL-запросом с `INNER JOIN` (или `LEFT JOIN`). Результирующий запрос включает
данные из обеих таблиц (`Book` и `Author`), что позволяет Hibernate сразу
создать объекты с заполненными связями, устраняя дополнительные обращения к базе
данных.


================================================================================
--------------------------------------------------------------------------------

# EntityGraph

**EntityGraph** в Hibernate — это механизм, позволяющий явно определять, какие
связанные сущности и их атрибуты должны быть загружены из базы данных при
выполнении запроса. Это инструмент для управления стратегией загрузки данных (
fetching strategy), который помогает избежать проблемы N+1 и оптимизировать
запросы, обеспечивая гибкость в сравнении с `JOIN FETCH` или аннотациями
`FetchType`.

### Основные концепции EntityGraph

EntityGraph позволяет создать "граф" сущностей, указывая, какие связи (
ассоциации) и атрибуты должны быть загружены вместе с основной сущностью. Это
полезно для:

- Устранения проблемы N+1, когда ленивая загрузка (`FetchType.LAZY`) приводит к
  множеству дополнительных запросов.
- Точного контроля над тем, какие данные загружаются, чтобы избежать избыточной
  загрузки данных при жадной загрузке (`FetchType.EAGER`).
- Динамической настройки загрузки в зависимости от сценария, без изменения
  аннотаций в сущностях.

EntityGraph поддерживается в JPA (начиная с версии 2.1) и активно используется в
Hibernate.

### Типы EntityGraph

1. **FetchGraph** (`javax.persistence.fetchgraph`):
    - Указывает, какие атрибуты и связи должны быть загружены (остальные
      считаются `LAZY`).
    - Если атрибут не указан в графе, он не загружается, даже если в сущности он
      помечен как `EAGER`.

2. **LoadGraph** (`javax.persistence.loadgraph`):
    - Указывает, какие атрибуты и связи должны быть загружены, но остальные
      атрибуты, помеченные как `EAGER`, также загружаются.
    - Более мягкий подход, чем `FetchGraph`.

| Тип          | Поведение                                                                |
|--------------|--------------------------------------------------------------------------|
| `fetchgraph` | Загружает **только** то, что указано в графе (остальное остаётся LAZY)   |
| `loadgraph`  | Загружает то, что указано в графе **+ всё, что уже EAGER по аннотациям** |

### Способы определения EntityGraph

EntityGraph можно определить двумя способами:

1. **Статически** — с помощью аннотаций `@NamedEntityGraph` в коде сущности.
2. **Динамически** — с помощью API `EntityGraph` в коде приложения.

### Пример сущностей

Рассмотрим те же сущности `Book` и `Author` с отношением Many-to-One:

```java

@Entity
public class Book {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String title;

    @ManyToOne(fetch = FetchType.LAZY)
    private Author author;

    // Геттеры и сеттеры
}

@Entity
public class Author {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;

    @OneToMany(mappedBy = "author", fetch = FetchType.LAZY)
    private List<Book> books;

    // Геттеры и сеттеры
}
```

### Пример 1: Статический EntityGraph с `@NamedEntityGraph`

Определим EntityGraph для сущности `Book`, чтобы загружать её вместе с автором.

```java

@Entity
@NamedEntityGraph(
        name = "Book.author",
        attributeNodes = @NamedAttributeNode("author")
)
public class Book {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String title;

    @ManyToOne(fetch = FetchType.LAZY)
    private Author author;

    // Геттеры и сеттеры
}
```

Использование в запросе:

```java
Session session = sessionFactory.openSession();
EntityGraph graph = session.getEntityGraph("Book.author");
List<Book> books = session.createQuery("FROM Book", Book.class)
        .setHint("javax.persistence.fetchgraph", graph)
        .getResultList();
for(
Book book :books){
        System.out.

println("Book: "+book.getTitle() +", Author: "+book.

getAuthor().

getName());
        }
        session.

close();
```

**SQL-запрос**:

```sql
SELECT b.id, b.title, b.author_id, a.id, a.name
FROM Book b
         INNER JOIN Author a ON b.author_id = a.id
```

**Результат**:

- Один запрос загружает все книги и их авторов, избегая проблемы N+1.
- Связь `author` загружается сразу, несмотря на `FetchType.LAZY`.

### Пример 2: Динамический EntityGraph

Если нужно создать граф динамически, можно использовать API `EntityGraph`:

```java
Session session = sessionFactory.openSession();
EntityGraph<Book> graph = session.createEntityGraph(Book.class);
graph.

addAttributeNodes("author");

List<Book> books = session.createQuery("FROM Book", Book.class)
        .setHint("javax.persistence.fetchgraph", graph)
        .getResultList();
for(
Book book :books){
        System.out.

println("Book: "+book.getTitle() +", Author: "+book.

getAuthor().

getName());
        }
        session.

close();
```

**SQL-запрос**: Тот же, что и в предыдущем примере (с `INNER JOIN`).

### Пример 3: Загрузка сложных графов

Если нужно загрузить не только автора, но и, например, книги автора (в обратную
сторону), можно расширить граф:

```java

@Entity
@NamedEntityGraph(
        name = "Author.books",
        attributeNodes = @NamedAttributeNode(value = "books")
)
public class Author { ...
}
```

Запрос:

```java
EntityGraph graph = session.getEntityGraph("Author.books");
List<Author> authors = session.createQuery("FROM Author", Author.class)
        .setHint("javax.persistence.fetchgraph", graph)
        .getResultList();
```

**SQL-запрос**:

```sql
SELECT a.id, a.name, b.id, b.title, b.author_id
FROM Author a
         LEFT JOIN Book b ON b.author_id = a.id
```

### Преимущества EntityGraph

1. **Гибкость**: Позволяет определять, какие связи загружать, без изменения
   аннотаций `FetchType`.
2. **Избежание N+1**: Загружает связанные данные в одном запросе, подобно
   `JOIN FETCH`.
3. **Повторное использование**: Статические графы (`@NamedEntityGraph`) можно
   переиспользовать в разных запросах.
4. **Динамическая настройка**: Динамические графы позволяют создавать разные
   стратегии загрузки в зависимости от контекста.
5. **Читаемость**: Код становится более явным в плане того, какие данные
   загружаются.

### Отличия от `JOIN FETCH`

- **Синтаксис**: `JOIN FETCH` используется в HQL/JPQL, тогда как EntityGraph —
  это API или аннотация, работающая с JPA.
- **Гибкость**: EntityGraph позволяет задавать сложные графы с вложенными
  связями и частичной загрузкой атрибутов.
- **Контроль**: EntityGraph даёт больше контроля над тем, какие атрибуты
  загружаются, особенно с `FetchGraph`.
- **Повторяемость**: Статические графы (`@NamedEntityGraph`) легче
  переиспользовать, чем писать `JOIN FETCH` в каждом запросе.

### Ограничения

1. **Сложность**: Для больших графов с множеством связей управление становится
   сложнее.
2. **Производительность**: Неправильная настройка графа может привести к
   загрузке избыточных данных (например, декартово произведение).
3. **Совместимость**: Требуется JPA 2.1 или выше, что может быть проблемой для
   старых проектов.

### Итог

**EntityGraph** — это мощный инструмент в Hibernate/JPA для управления загрузкой
связанных сущностей, который помогает избежать проблемы N+1 и оптимизировать
запросы. Он позволяет явно указывать, какие связи и атрибуты загружать, через
аннотации (`@NamedEntityGraph`) или динамическое API. В отличие от `JOIN FETCH`,
EntityGraph более гибок и удобен для повторного использования, особенно в
сложных сценариях с множеством связей.


================================================================================
--------------------------------------------------------------------------------

# ENTITYMANAGER

**EntityManager** в Hibernate (и в целом в JPA — Java Persistence API) — это
основной интерфейс, используемый для взаимодействия с базой данных в контексте
управления сущностями (entity objects). Он предоставляет методы для выполнения
операций CRUD (Create, Read, Update, Delete) и управления жизненным циклом
сущностей, а также для создания запросов к базе данных.

## Основные функции EntityManager

`EntityManager` отвечает за:

1. **Управление сущностями**:
    - Регистрация новых сущностей в контексте персистентности (persistence





================================================================================
--------------------------------------------------------------------------------

# Уровни кеширования в Hibernate

В Hibernate существует несколько уровней кэширования, которые помогают
оптимизировать производительность приложения за счёт сокращения количества
запросов к базе данных. Основные уровни кэша в Hibernate:

1. **Кэш первого уровня (First-Level Cache)**
2. **Кэш второго уровня (Second-Level Cache)**
3. **Кэш запросов (Query Cache)**

Ниже подробно разберём каждый уровень.

### 1. Кэш первого уровня (First-Level Cache)

**Описание**:

- Кэш первого уровня встроен в Hibernate и всегда включён по умолчанию.
- Он привязан к контексту персистентности (`EntityManager` или `Session`) и
  существует только в рамках одной сессии Hibernate.
- Хранит сущности, загруженные в текущей сессии, чтобы повторные запросы к одним
  и тем же данным не обращались к базе.

**Особенности**:

- **Область действия**: Одна сессия (`Session`/`EntityManager`). Когда сессия
  закрывается, кэш очищается.
- **Автоматическое управление**: Hibernate автоматически сохраняет в кэше
  сущности, загруженные через `find()`, `get()`, или запросы, и отслеживает их
  изменения (dirty checking).
- **Пример**: Если вы дважды вызываете `session.get(Book.class, 1L)` в одной
  сессии, Hibernate выполнит только один SQL-запрос, а второй раз вернёт объект
  из кэша.
- **Ограничения**: Не работает между разными сессиями, не масштабируется для
  распределённых приложений.

**Пример**:

```java
Session session = sessionFactory.openSession();
Book book1 = session.get(Book.class, 1L); // SQL-запрос к базе
Book book2 = session.get(Book.class, 1L); // Берётся из кэша, без запроса
session.

close(); // Кэш очищается
```

**Управление**:

- `session.evict(Object)` — удаляет конкретную сущность из кэша.
- `session.clear()` — очищает весь кэш сессии.

### 2. Кэш второго уровня (Second-Level Cache)

**Описание**:

- Кэш второго уровня является опциональным и работает на уровне
  `SessionFactory`, а не отдельной сессии.
- Хранит данные между сессиями, что позволяет использовать кэшированные сущности
  в разных сессиях и даже в разных транзакциях.
- Обычно используется для данных, которые редко изменяются (например,
  справочники, конфигурационные данные).

**Особенности**:

- **Область действия**: Общий для всех сессий в пределах одной `SessionFactory`.
  Может быть распределённым в кластере.
- **Настройка**: Требует явной конфигурации и подключения стороннего провайдера
  кэша (например, Ehcache, Hazelcast, Infinispan).
- **Типы кэширования**:
    - **Entity Cache**: Кэширует сами сущности (например, `Book` с `id=1`).
    - **Collection Cache**: Кэширует коллекции связанных данных (например,
      список книг автора).
- **Стратегии кэширования**:
    - `READ_ONLY`: Для данных, которые никогда не изменяются.
    - `NONSTRICT_READ_WRITE`: Для данных, которые редко изменяются, с
      возможностью нестрогого обновления.
    - `READ_WRITE`: Для данных, которые могут изменяться, с обеспечением
      согласованности.
    - `TRANSACTIONAL`: Для строгой транзакционной согласованности (редко
      используется).
- **Пример использования**: Если сущность `Book` помечена как кэшируемая, её
  данные сохраняются в кэше второго уровня, и последующие запросы
  `session.get(Book.class, 1L)` из разных сессий используют кэш вместо базы.

**Конфигурация**:

1. Включение в `persistence.xml` или `hibernate.cfg.xml`:
   ```xml
   <property name="hibernate.cache.use_second_level_cache" value="true"/>
   <property name="hibernate.cache.region.factory_class" value="org.hibernate.cache.ehcache.EhCacheRegionFactory"/>
   ```
2. Аннотация для сущности:
   ```java
   @Entity
   @Cacheable
   @Cache(usage = CacheConcurrencyStrategy.READ_ONLY)
   public class Book {
       @Id
       private Long id;
       private String title;
       // Геттеры и сеттеры
   }
   ```

**Пример**:

```java
Session session1 = sessionFactory.openSession();
Book book1 = session1.get(Book.class, 1L); // SQL-запрос, данные кэшируются
session1.

close();

Session session2 = sessionFactory.openSession();
Book book2 = session2.get(Book.class, 1L); // Берётся из кэша второго уровня
session2.

close();
```

**Ограничения**:

- Требует настройки провайдера кэша.
- Не подходит для часто изменяемых данных, так как может привести к проблемам
  согласованности.
- Дополнительные накладные расходы на управление кэшем.

### 3. Кэш запросов (Query Cache)

**Описание**:

- Кэш запросов хранит результаты JPQL/HQL-запросов, чтобы избежать повторного
  выполнения одинаковых запросов.
- Работает в связке с кэшем второго уровня, так как сами сущности, возвращаемые
  запросом, должны быть кэшируемыми.

**Особенности**:

- **Область действия**: Уровень `SessionFactory`, как и кэш второго уровня.
- **Конфигурация**: Требует явного включения и указания, что запрос кэшируемый.
- **Применение**: Подходит для запросов, которые часто выполняются с одинаковыми
  параметрами и возвращают редко изменяемые данные.
- **Ограничение**: Если данные, возвращаемые запросом, изменяются, кэш
  становится недействительным, что требует механизма инвалидации.

**Конфигурация**:

1. Включение в `persistence.xml` или `hibernate.cfg.xml`:
   ```xml
   <property name="hibernate.cache.use_query_cache" value="true"/>
   ```
2. Указание кэшируемости запроса:
   ```java
   Query query = session.createQuery("FROM Book WHERE title LIKE :title")
                       .setParameter("title", "Java%")
                       .setCacheable(true);
   List<Book> books = query.getResultList();
   ```

**Пример**:

```java
void demo(){
    Session session1 = sessionFactory.openSession();
    Query query1 = session1.createQuery("FROM Book WHERE title LIKE :title")
            .setParameter("title", "Java%")
            .setCacheable(true);
    List<Book> books1 = query1.getResultList(); // SQL-запрос, результат кэшируется
    session1.lose();

    Session session2 = sessionFactory.openSession();
    Query query2 = session2.createQuery("FROM Book WHERE title LIKE :title")
            .setParameter("title", "Java%")
            .setCacheable(true);
    List<Book> books2 = query2.getResultList(); // Берётся из кэша запросов
    session2.close();    
}

```

**Ограничения**:

- Требует, чтобы сущности, возвращаемые запросом, были в кэше второго уровня.
- Может быть неэффективен, если данные часто меняются, так как кэш становится
  недействительным.
- Дополнительная память для хранения результатов запросов.

### Сравнение уровней кэша

| Уровень кэша           | Область действия | Включён по умолчанию | Требует настройки | Применение                                |
|------------------------|------------------|----------------------|-------------------|-------------------------------------------|
| **First-Level Cache**  | Одна сессия      | Да                   | Нет               | Автоматическое кэширование в сессии       |
| **Second-Level Cache** | SessionFactory   | Нет                  | Да                | Кэширование сущностей между сессиями      |
| **Query Cache**        | SessionFactory   | Нет                  | Да                | Кэширование результатов JPQL/HQL-запросов |

### Связь с проблемой N+1

Кэш второго уровня и кэш запросов могут помочь в решении проблемы N+1, так как:

- Если сущности или связи уже находятся в кэше второго уровня, Hibernate не
  будет выполнять дополнительные запросы для их загрузки.
- Например, если в кэше второго уровня хранятся авторы книг, запрос
  `book.getAuthor()` не вызовет SQL-запрос, а возьмёт данные из кэша.

Однако кэширование не заменяет `JOIN FETCH` или `EntityGraph`, так как:

- Кэш работает только для данных, которые уже были загружены ранее.
- Для новых данных всё равно потребуется оптимизация загрузки связей.

### Рекомендации

1. **First-Level Cache**: Используйте всегда, так как он встроен и не требует
   настройки.
2. **Second-Level Cache**: Настраивайте для редко изменяемых данных (например,
   справочников). Выбирайте подходящую стратегию кэширования (`READ_ONLY`,
   `READ_WRITE`).
3. **Query Cache**: Используйте для часто выполняемых запросов с предсказуемыми
   параметрами, но только если сущности уже кэшируются в кэше второго уровня.
4. **Мониторинг**: Отслеживайте попадания в кэш (cache hits) и промахи (cache
   misses) с помощью Hibernate Statistics.
5. **Осторожность**: Неправильная настройка кэша может привести к проблемам
   согласованности данных или избыточному потреблению памяти.

### Итог

Hibernate поддерживает три уровня кэширования:

- **First-Level Cache**: Встроенный, сессионный кэш, работает автоматически.
- **Second-Level Cache**: Опциональный, работает на уровне `SessionFactory`,
  требует настройки провайдера (например, Ehcache).
- **Query Cache**: Кэширует результаты запросов, работает в связке с кэшем
  второго уровня.

Каждый уровень решает разные задачи, и их использование зависит от сценария. Для
борьбы с проблемой N+1 кэш второго уровня и кэш запросов могут быть полезны, но
чаще требуется комбинация с `JOIN FETCH` или `EntityGraph` для полной
оптимизации.


-------------

### 🔹 Первый уровень кэша (Session Cache)

### 📌 Что это?

- Это **встроенный кеш**, работающий **в пределах одной сессии (`Session`) или
  транзакции**.
- **Всегда включён** — ничего дополнительно настраивать не нужно.
- Кэширует сущности, загруженные в текущей `Session`.

Пример:

```java
Session session = sessionFactory.openSession();

User user1 = session.get(User.class, 1L); // SELECT из БД
User user2 = session.get(User.class, 1L); // из кэша, без запроса
```

> Второй вызов не пойдёт в базу, потому что объект уже есть в памяти.

### 🔥 Очистка кэша:

- `session.clear()` — очищает весь кэш
- `session.evict(user)` — удаляет конкретную сущность из кэша
- `session.close()` — кэш уничтожается

---

## 🔸 Второй уровень кэша (2nd level cache)

### 📌 Что это?

- Это **глобальный кеш**, работающий **между сессиями и транзакциями**.
- **Выключен по умолчанию**, его нужно явно включить и настроить.
- Используется для **повторно используемых данных** (справочники, настройки,
  часто читаемые сущности).

### 🔧 Настраивается через конфигурацию и провайдер кеша:

Hibernate сам по себе не кеширует на 2 уровне — он **использует сторонние
библиотеки**, например:

- **Ehcache**
- **Infinispan**
- **Caffeine**
- **Redis (через адаптеры)**

---

## 🔐 Как включить 2-й уровень кеша

### Пример конфигурации (Hibernate + Ehcache):

```properties
hibernate.cache.use_second_level_cache=true
hibernate.cache.region.factory_class=org.hibernate.cache.jcache.JCacheRegionFactory
hibernate.javax.cache.provider=org.ehcache.jsr107.EhcacheCachingProvider
```

И в сущности:

```java

@Entity
@Cacheable
@org.hibernate.annotations.Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class User {
    ...
}
```

## Таблица сравнения

| Характеристика         | 1-й уровень (Session) | 2-й уровень (кеш провайдер) |
|------------------------|-----------------------|-----------------------------|
| Область действия       | В пределах `Session`  | Глобальный между сессиями   |
| Включён по умолчанию   | ✅ Да                  | ❌ Нет                       |
| Тип кеша               | В оперативной памяти  | Зависит от провайдера       |
| Поддержка стратегий    | Нет                   | Да (READ_ONLY, READ_WRITE…) |
| Удаляется при закрытии | Да                    | Нет                         |

---

## 🚀 Когда использовать 2-й уровень?

| Полезно когда                            | Не стоит использовать           |
|------------------------------------------|---------------------------------|
| Часто читаешь данные, редко меняешь      | Частые обновления и удаления    |
| Большой объём данных, стабильных по сути | Сложные транзакции              |
| Нужна производительность                 | Сложные связи, ленивая загрузка |

---

## 🔄 Третий уровень кеша (опционально)

Кэширование запросов в Hibernate — это важная тема для оптимизации
производительности при работе с базой данных. Оно позволяет **уменьшить
количество обращений к базе данных**, сохраняя результаты запросов в памяти.

### 🔒 Кэширование в Hibernate

Hibernate поддерживает **несколько уровней кэширования**, и кэширование
запросов — это один из методов повышения производительности.

---

## 🧠 Основные уровни кэширования в Hibernate:

1. **Первичный кэш (Session Cache)** — кэш на уровне сессии.
2. **Вторичный кэш (Second-level Cache)** — кэш на уровне сессии-фабрики.
3. **Кэш запросов (Query Cache)** — кэширование результатов запросов.

---

## 🔥 Кэш запросов (Query Cache)

### 📌 Что это?

**Кэш запросов** — это **кэширование результатов выполнения запросов**. Это
может быть полезно, если одни и те же запросы выполняются многократно с
одинаковыми параметрами.

### ✅ Преимущества кэширования запросов:

- **Уменьшение нагрузки на БД**: запросы, которые часто выполняются с
  одинаковыми параметрами, не будут отправляться в базу данных.
- **Ускорение выполнения**: результаты запросов можно будет извлекать
  непосредственно из кэша, что намного быстрее, чем делать запрос в базу.

---

## 🛠 Как включить кэш запросов в Hibernate?

1. Включи **вторичный кэш**.
2. Включи **кэш запросов**.

### 1. Включение второго уровня кэширования

Для того чтобы работать с кэшированием запросов, нужно включить **вторичный кэш
**. Это делается через конфигурацию Hibernate.

Пример конфигурации `hibernate.cfg.xml` для включения второго уровня
кэширования:

```xml

<hibernate-configuration>
    <session-factory>
        <!-- Включение второго уровня кэширования -->
        <property name="hibernate.cache.use_second_level_cache">true</property>
        <property name="hibernate.cache.region.factory_class">
            org.hibernate.cache.ehcache.EhCacheRegionFactory
        </property>
        <!-- Указываем фабрику кэширования -->
    </session-factory>
</hibernate-configuration>
```

### 2. Включение кэша запросов

Чтобы включить кэширование запросов, нужно установить свойство
`hibernate.cache.use_query_cache` в `true` и использовать кэш второго уровня.

```xml

<property name="hibernate.cache.use_query_cache">true</property>
```

---

## 🧰 Пример кэширования запросов

```java
Query query = session.createQuery("FROM User WHERE active = :active");
query.

setParameter("active",true);

// Включаем кэш запросов
query.

setCacheable(true);

// Получаем результаты из кэша или выполняем запрос в БД
List<User> users = query.list();
```

- `setCacheable(true)` — указывает Hibernate, что результат запроса можно
  кэшировать.
- Когда такой запрос выполняется повторно с теми же параметрами, Hibernate будет
  сначала искать результаты в кэше, и только если их нет — выполнит запрос в
  базу.

---

## 🔥 Настройка кэширования с EhCache

Кэш запросов работает в связке с **вторичным кэшем** (например, с **EhCache**).
Вот как можно настроить **EhCache** для кэширования запросов.

1. Включи кэширование второго уровня:

```xml

<property name="hibernate.cache.use_second_level_cache">true</property>
<property name="hibernate.cache.region.factory_class">
org.hibernate.cache.ehcache.EhCacheRegionFactory
</property>
<property name="hibernate.cache.use_query_cache">true</property>
```

2. Создай конфигурацию **EhCache** (`ehcache.xml`):

```xml

<ehcache>
    <cache name="org.hibernate.cache.internal.StandardQueryCache"
           maxEntriesLocalHeap="1000" eternal="false" timeToLiveSeconds="600"/>
</ehcache>
```

---

## ⚙️ Кэширование запросов с параметрами

Кэширование работает **по запросу и его параметрам**. Это значит, что если ты
выполняешь запрос с разными параметрами, то результат будет кэшироваться
отдельно для каждого набора параметров.

### Пример:

```java
Query query = session.createQuery("FROM User WHERE name = :name");
query.

setParameter("name","John");

// Кэшируем запрос
query.

setCacheable(true);

// Если запрос повторится с тем же параметром "John", результат будет взят из кэша
List<User> users = query.list();
```

- Запрос с параметром `"name = 'John'"` будет кэшироваться, и при следующем
  выполнении с теми же параметрами результат будет взят из кэша.

---

## 🧩 Интеграция с Spring Data JPA

В **Spring Data JPA** кэширование запросов также поддерживается. Для этого можно
использовать аннотацию `@Query` с параметром `cacheable = true`.

### Пример:

```java

@Query("FROM User u WHERE u.name = :name")
@Cacheable("users")
    // Этот кэш будет использоваться для всех запросов
List<User> findByName(@Param("name") String name);
```

В этом случае, когда метод `findByName()` вызывается с теми же параметрами,
Hibernate будет использовать кэшированное значение.

---

## ⚠️ Важные замечания:

- Кэширование запросов не **обновляет** кэш автоматически. То есть, если данные
  изменяются в базе данных, кэшированные результаты не будут обновлены.
- Нужно помнить, что если запрос включает много данных или параметров,
  кэширование может **занимать много памяти**. Старайся кэшировать только *
  *часто используемые запросы с ограниченным количеством результатов**.
- **Параметризация** запросов критична — кэширование работает по параметрам, и
  если параметры изменяются, запрос будет выполнен заново.

---

## ✅ Преимущества кэширования запросов:

1. **Меньше нагрузки на базу данных**: кэшированные результаты берутся из
   памяти.
2. **Скорость**: результаты могут быть возвращены намного быстрее, чем
   выполнение SQL-запроса.
3. **Меньше блокировок**: кэш позволяет уменьшить количество конкуренции за
   блокировки в базе данных.

---

============

### Кэширования в реальном приложении

Отлично! Давай создадим **реальный пример**, где кэширование запросов в
Hibernate будет использоваться с **сущностями** и их **связями**. Мы настроим
кэш запросов для **вторичного кэширования** с использованием **EhCache**.

### 👩‍💻 Шаг 1: Настройка Hibernate для кэширования

1. **Конфигурация Hibernate** (`hibernate.cfg.xml`):

```xml

<hibernate-configuration>
    <session-factory>
        <!-- Включаем второй уровень кэширования -->
        <property name="hibernate.cache.use_second_level_cache">true</property>
        <property name="hibernate.cache.region.factory_class">
            org.hibernate.cache.ehcache.EhCacheRegionFactory
        </property>

        <!-- Включаем кэширование запросов -->
        <property name="hibernate.cache.use_query_cache">true</property>

        <!-- Прочее: настройка диалекта, драйвера и т.д. -->
    </session-factory>
</hibernate-configuration>
```

2. **Конфигурация EhCache** (`ehcache.xml`):

```xml

<ehcache>
    <!-- Кэш для запросов -->
    <cache name="org.hibernate.cache.internal.StandardQueryCache"
           maxEntriesLocalHeap="1000" eternal="false" timeToLiveSeconds="600"/>
    <!-- Кэш для сущностей -->
    <cache name="com.example.User"
           maxEntriesLocalHeap="1000" eternal="true"/>
</ehcache>
```

---

### 👩‍💻 Шаг 2: Сущности и связи

Теперь создадим несколько сущностей и запросим их с использованием кэширования
запросов.

1. **Сущность `User`**:

```java

@Entity
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE) // Кэшируем сущность
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    private boolean active;

    @OneToMany(mappedBy = "user", fetch = FetchType.LAZY)
    @Cache(usage = CacheConcurrencyStrategy.READ_ONLY) // Кэшируем коллекцию
    private List<Order> orders;

    // Геттеры и сеттеры
}
```

2. **Сущность `Order`**:

```java

@Entity
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE) // Кэшируем сущность
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Double amount;

    @ManyToOne(fetch = FetchType.LAZY)
    private User user;

    // Геттеры и сеттеры
}
```

---

### 👩‍💻 Шаг 3: Использование кэша запросов

Теперь создадим запросы с использованием **кэширования запросов**.

```java
// Запрос, который будет кэшироваться
Session session = sessionFactory.openSession();
Query query = session.createQuery("FROM User u WHERE u.active = :active");
query.

setParameter("active",true);

// Включаем кэширование запроса
query.

setCacheable(true);

// Выполняем запрос и получаем результат
List<User> users = query.list();
```

### 🧠 Что происходит?

- Запрос будет **кэшироваться** в памяти через **EhCache**.
- Когда мы снова запросим те же данные с теми же параметрами (`active = true`),
  Hibernate сначала будет искать результат в кэше, а если его не будет —
  выполнит запрос к базе.

---

### 👩‍💻 Шаг 4: Интеграция с Spring Data JPA (если используешь Spring)

В **Spring Data JPA** можно легко интегрировать кэширование запросов с помощью
аннотации `@Cacheable`.

1. **Репозиторий** для `User`:

```java

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    @Cacheable("users")  // Включаем кэширование
    @Query("SELECT u FROM User u WHERE u.active = :active")
    List<User> findByActive(@Param("active") boolean active);
}
```

Теперь метод `findByActive` будет кэшировать запросы по параметру `active`.

2. **Репозиторий** для `Order`:

```java

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {

    @Cacheable("orders")  // Включаем кэширование
    @Query("SELECT o FROM Order o WHERE o.user.id = :userId")
    List<Order> findByUserId(@Param("userId") Long userId);
}
```

### 🧠 Важное замечание

- **Кэширование** работает **по параметрам запроса**: если ты изменишь параметры
  запроса, то результат будет заново загружен из базы.
- **Единый кэш** для всех запросов с одинаковыми параметрами, если они не
  обновляются или не меняются в базе.

---

### 👩‍💻 Шаг 5: Тестирование кэширования запросов

1. **Создание пользователей и заказов**:

```java
// Создаём пользователей и заказы
session.save(new User("John", true));
        session.

save(new User("Jane", false));

// Создаём заказы для пользователей
        session.

save(new Order(100.0, user1));
        session.

save(new Order(150.0, user2));
```

2. **Запрос с кэшированием**:

```java
// Первый запрос (будет выполнен в базу данных)
List<User> activeUsersFirstCall = userRepository.findByActive(true);

// Второй запрос (результат будет взят из кэша)
List<User> activeUsersSecondCall = userRepository.findByActive(true);
```

- При втором вызове данные будут **получены из кэша**, если они не изменились в
  базе.

---

### 🧩 Резюме

- **Кэширование запросов** позволяет хранить результаты запросов в памяти для
  повторных запросов с теми же параметрами.
- **Вторичный кэш** и **кэширование запросов** эффективно снижают нагрузку на
  базу данных.
- При **интеграции с Spring** можно использовать аннотацию `@Cacheable` для
  удобного включения кэширования на уровне репозиториев.

====

Hibernate предоставляет несколько уровней кеширования, которые помогают
оптимизировать производительность приложения, уменьшая количество обращений к
базе данных.

### 1. Первичный кеш (First-Level Cache)

- **Описание:** Это кэш, который связан с сессией Hibernate. Он хранит объекты,
  загруженные в рамках текущей сессии.
- **Область видимости:** Действует только в пределах одной сессии. Когда сессия
  закрывается, все данные в первичном кэше теряются.
- **Применение:** Если вы запрашиваете одну и ту же сущность несколько раз в
  рамках одной сессии, Hibernate будет возвращать объект из первичного кэша
  вместо выполнения нового запроса к базе данных.

- **Описание**: Первичный кеш является неотъемлемой частью сессии Hibernate. Он
  хранит объекты, загруженные в текущей сессии, и обеспечивает их повторное
  использование без необходимости повторного запроса к базе данных.
- **Область видимости**: Первичный кеш существует только в пределах одной
  сессии. Как только сессия закрывается, все объекты в первичном кеше становятся
  недоступными.
- **Поведение**: Если вы запрашиваете один и тот же объект несколько раз в
  рамках одной сессии, Hibernate будет возвращать его из первичного кеша вместо
  того, чтобы выполнять новый запрос к базе данных.
- **Пример**:
  ```java
  Session session = sessionFactory.openSession();
  Author author1 = session.get(Author.class, 1); // Запрос к БД
  Author author2 = session.get(Author.class, 1); // Возвращается из первичного кеша
  ```

### 2. Вторичный кеш (Second-Level Cache)

- **Описание:** Это опциональный кэш, который может быть настроен для хранения
  объектов между сессиями. Он позволяет делиться данными между разными сессиями.
- **Область видимости:** Действует на уровне сессии-фабрики (SessionFactory).
  Данные остаются в этом кэше даже после закрытия сессий.
- **Применение:** Вторичный кэш может быть использован для хранения часто
  запрашиваемых сущностей или коллекций, что позволяет уменьшить количество
  запросов к базе данных.
- **Описание**: Вторичный кеш является опциональным и может быть настроен для
  хранения объектов между сессиями. Он позволяет кэшировать данные на уровне
  всей сессии фабрики (SessionFactory), что позволяет использовать одни и те же
  данные в разных сессиях.
- **Область видимости**: Вторичный кеш доступен для всех сессий, использующих
  одну и ту же фабрику сессий.
- **Настройка**: Для использования второго уровня кеша необходимо включить его в
  конфигурации Hibernate и выбрать провайдер кеша (например, Ehcache, Infinispan
  и т.д.).
- **Пример настройки**:
  ```xml
  <property name="hibernate.cache.use_second_level_cache">true</property>
  <property name="hibernate.cache.region.factory_class">org.hibernate.cache.ehcache.EhCacheRegionFactory</property>
  ```

#### Конфигурация вторичного кэша:

Для использования вторичного кэша необходимо:

1. Включить его в конфигурации Hibernate.
2. Настроить провайдер кеша (например, Ehcache, Infinispan и т.д.).
3. Аннотировать сущности или коллекции, которые должны использовать вторичный
   кэш.

Пример аннотации для включения вторичного кэша:

```java

@Entity
@Cacheable
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Author {
    @Id
    private Long id;

    // другие поля и методы
}
```

#### Кеширование коллекций

Вторичный кеш также может использоваться для кэширования коллекций. Вы можете
настроить стратегию кэширования для отдельных сущностей или коллекций через
аннотации или XML-конфигурацию.

#### Пример аннотации для вторичного кеша:

```java

@Entity
@Cacheable
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Author {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    @OneToMany(mappedBy = "author")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    private Set<Book> books;
}
```

### Стратегии кэширования

Hibernate поддерживает несколько стратегий кэширования для второго уровня:

1. **READ_ONLY**: Используется для объектов, которые не изменяются после их
   создания. Это наиболее эффективная стратегия.
2. **READ_WRITE**: Используется для объектов, которые могут изменяться.
   Hibernate управляет блокировками при записи.
3. **NONSTRICT_READ_WRITE**: Позволяет более высокую производительность за счет
   менее строгого контроля за изменениями.
4. **TRANSACTIONAL**: Поддерживает транзакционное поведение.

### 3. **Кэш запросов (Query Cache)**

- **Описание:** Это дополнительный уровень кеширования, который позволяет
  кешировать результаты запросов.
- **Область видимости:** Работает совместно со вторичным кэшем и может
  использоваться для кеширования результатов HQL или Criteria запросов.
- **Применение:** Если вы выполняете один и тот же запрос несколько раз,
  результаты могут быть извлечены из кеша вместо выполнения запроса к базе
  данных.

#### Конфигурация кеша запросов:

Чтобы использовать кеш запросов, необходимо:

1. Включить его в конфигурации Hibernate.
2. Аннотировать запросы или использовать методы API для указания использования
   кеша.

Пример использования кеша запросов:

```java
List<Author> authors = session.createQuery("FROM Author")
        .setHint("org.hibernate.cacheable", true)
        .getResultList();
```

### Заключение

Кеширование в Hibernate — это мощный инструмент для повышения производительности
приложений за счет уменьшения количества обращений к базе данных. Понимание
уровней кеширования и их правильная настройка могут значительно улучшить время
отклика вашего приложения и снизить нагрузку на базу данных.

=============

> ## Hibernate. Уровни кеширования в Hibernate лучше изучить

### Основные уровни кэширования в Hibernate:

1. **Первичный кэш (Session Cache)** — кэш на уровне сессии.
2. **Вторичный кэш (Second-level Cache)** — кэш на уровне сессии-фабрики.
3. **Кэш запросов (Query Cache)** — кэширование результатов запросов.

### 1. Первичный кеш (First-Level Cache)

- Это **встроенный кеш**, работающий **в пределах одной сессии (`Session`) или
  транзакции**.
- **Всегда включён** — ничего дополнительно настраивать не нужно.
- Кэширует сущности, загруженные в текущей `Session`.
- Первичный кеш является неотъемлемой частью сессии Hibernate. Он
  хранит объекты, загруженные в текущей сессии, и обеспечивает их повторное
  использование без необходимости повторного запроса к базе данных.

**Пример:**

```java
Session session = sessionFactory.openSession();
User user1 = session.get(User.class, 1L); // SELECT из БД
User user2 = session.get(User.class, 1L); // из кэша, без запроса
```

Второй вызов не пойдёт в базу, потому что объект уже есть в памяти.

#### Очистка кэша:

- `session.clear()` — очищает весь кэш
- `session.evict(user)` — удаляет конкретную сущность из кэша
- `session.close()` — кэш уничтожается

### 2. Вторичный кеш (Second-Level Cache)

Это глобальный кеш, работающий между сессиями и транзакциями. Выключен
по умолчанию, его нужно явно включить и настроить. Используется для повторно
используемых данных (справочники, настройки, часто читаемые сущности).
Действует на уровне сессии-фабрики (SessionFactory). Данные остаются в этом кэше
даже после закрытия сессий. Вторичный кэш может быть использован для хранения
часто запрашиваемых сущностей или коллекций, что позволяет уменьшить количество
запросов к базе данных. Вторичный кеш доступен для всех сессий, использующих
одну и ту же фабрику сессий. Для использования второго уровня кеша необходимо
включить его в конфигурации Hibernate и выбрать провайдер кеша (например,
Ehcache, Infinispan и т.д.).

**Настраивается через конфигурацию и провайдер кеша:**

Hibernate сам по себе не кеширует на 2 уровне — он **использует сторонние
библиотеки**, например:

- **Ehcache**
- **Infinispan**
- **Caffeine**
- **Redis (через адаптеры)**

Как включить 2-й уровень кеша

```properties
hibernate.cache.use_second_level_cache=true
hibernate.cache.region.factory_class=org.hibernate.cache.jcache.JCacheRegionFactory
hibernate.javax.cache.provider=org.ehcache.jsr107.EhcacheCachingProvider
```

И в сущности:

```java

@Entity
@Cacheable
@org.hibernate.annotations.Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class User {
    ...
}
```

#### Стратегии кэширования

Hibernate поддерживает несколько стратегий кэширования для второго уровня:

1. **READ_ONLY**: Используется для объектов, которые не изменяются после их
   создания. Это наиболее эффективная стратегия.
2. **READ_WRITE**: Используется для объектов, которые могут изменяться.
   Hibernate управляет блокировками при записи.
3. **NONSTRICT_READ_WRITE**: Позволяет более высокую производительность за счет
   менее строгого контроля за изменениями.
4. **TRANSACTIONAL**: Поддерживает транзакционное поведение.

### 3. **Кэш запросов (Query Cache)**

- **Описание:** Это дополнительный уровень кеширования, который позволяет
  кешировать результаты запросов.
- **Область видимости:** Работает совместно со вторичным кэшем и может
  использоваться для кеширования результатов HQL или Criteria запросов.
- **Применение:** Если вы выполняете один и тот же запрос несколько раз,
  результаты могут быть извлечены из кеша вместо выполнения запроса к базе
  данных.

### Включение кэша запросов в Hibernate

1. Включить **вторичный кэш**.
2. Включить **кэш запросов**.

#### Кэширование запросов с параметрами

Кэширование работает **по запросу и его параметрам**. Это значит, что если ты
выполняешь запрос с разными параметрами, то результат будет кэшироваться
отдельно для каждого набора параметров. Но если запрос повторится с тем же
параметром "John", результат будет взят из кэша

#### Интеграция с Spring Data JPA

В **Spring Data JPA** кэширование запросов также поддерживается. Для этого можно
использовать аннотацию `@Query` с параметром `cacheable = true`.

#### Важные замечания:

- Кэширование запросов не **обновляет** кэш автоматически. То есть, если данные
  изменяются в базе данных, кэшированные результаты не будут обновлены.
- Нужно помнить, что если запрос включает много данных или параметров,
  кэширование может **занимать много памяти**. Старайся кэшировать только *
  *часто используемые запросы с ограниченным количеством результатов**.
- **Параметризация** запросов критична — кэширование работает по параметрам, и
  если параметры изменяются, запрос будет выполнен заново.




================================================================================
--------------------------------------------------------------------------------

# Сессия Hibernate


В Hibernate **сессия** (`Session`) — это основной интерфейс, предоставляемый
Hibernate для взаимодействия приложения с базой данных. Это своего рода "рабочая
область", которая управляет операциями с сущностями (entity objects) в рамках
одной единицы работы, связанной с базой данных. Сессия играет ключевую роль в
управлении контекстом персистентности (persistence context) и является аналогом
`EntityManager` в JPA, но с дополнительными Hibernate-специфичными
возможностями.

### Основные характеристики сессии

1. **Контекст персистентности**:
    - Сессия хранит **кэш первого уровня** (First-Level Cache), где находятся
      все сущности, загруженные или созданные в рамках текущей сессии.
    - Она отслеживает изменения в сущностях (dirty checking) и синхронизирует их
      с базой данных при выполнении `flush()` или `commit()`.

2. **Интерфейс для операций**:
    - Сессия предоставляет методы для выполнения операций CRUD (Create, Read,
      Update, Delete), а также для выполнения HQL-запросов и управления
      транзакциями.
    - Она действует как посредник между Java-объектами (сущностями) и базой
      данных.

3. **Жизненный цикл**:
    - Сессия создаётся из `SessionFactory` и существует в рамках определённого
      контекста (например, одной транзакции или запроса).
    - После завершения работы сессия закрывается, и её кэш очищается.

4. **Однопоточность**:
    - Сессия не является потокобезопасной (thread-safe). Каждая сессия должна
      использоваться только в одном потоке.
    - Обычно сессия создаётся для обработки одного запроса или одной транзакции
      и затем закрывается.

### Основные функции сессии

Сессия предоставляет следующие возможности:

1. **CRUD-операции**:
    - `save(Object)` или `persist(Object)`: Сохранение новой сущности в базе.
    - `get(Class, id)` или `load(Class, id)`: Загрузка сущности по
      идентификатору.
    - `update(Object)` или `merge(Object)`: Обновление сущности.
    - `delete(Object)`: Удаление сущности.

2. **Запросы**:
    - Выполнение HQL-запросов через `createQuery()`.
    - Поддержка Criteria API и нативных SQL-запросов.

3. **Управление транзакциями**:
    - Сессия интегрируется с транзакциями через `beginTransaction()`,
      `commit()`, `rollback()`.
    - Без транзакции большинство операций (например, `save`, `update`) не будут
      применены к базе данных.

4. **Кэширование**:
    - Сессия автоматически использует кэш первого уровня для хранения сущностей
      в рамках одной сессии.
    - При повторном запросе одной и той же сущности (например, через
      `get(Book.class, 1L)`) Hibernate возвращает объект из кэша, избегая
      запроса к базе.

5. **Управление ленивой загрузкой**:
    - Сессия управляет ленивой загрузкой (`FetchType.LAZY`) связей, загружая
      данные только при необходимости.
    - Если сессия закрыта, попытка доступа к лениво загружаемым данным вызовет
      исключение `LazyInitializationException`.

### Пример использования сессии

Предположим, у нас есть сущность `Book`:

```java

@Entity
public class Book {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String title;

    @ManyToOne(fetch = FetchType.LAZY)
    private Author author;

    // Геттеры и сеттеры
}
```

Пример работы с сессией:

```java

void demo() {
    // Создание SessionFactory (обычно один раз на приложение)
    SessionFactory sessionFactory = new Configuration().configure().buildSessionFactory();

    // Открытие сессии
    Session session = sessionFactory.openSession();

    // Начало транзакции
    Transaction tx = session.beginTransaction();

    // Сохранение новой книги
    Book book = new Book();
    book.setTitle("Java Basics");
    session.save(book);

    // Поиск книги
    Book foundBook = session.get(Book.class, 1L);
    System.out.println("Found book: " + foundBook.getTitle());

    // Обновление
    foundBook.setTitle("Java Advanced");
    ession.update(foundBook);

    // Запрос HQL
    List<Book> books = session.createQuery("FROM Book WHERE title LIKE :title", Book.class)
            .setParameter("title", "Java%")
            .getResultList();

    // Подтверждение транзакции
    tx.commit();
    // Закрытие сессии
    session.close();
}
```

### Сессия и проблема N+1

Сессия может способствовать возникновению проблемы N+1, если связи настроены на
`FetchType.LAZY` и загружаются по отдельности. Например:

```java
List<Book> books = session.createQuery("FROM Book", Book.class).getResultList();
for(
Book book :books){
        System.out.

println(book.getAuthor().

getName()); // Вызовет отдельный запрос для каждого автора
        }
```

**Решение**:

- Использовать `JOIN FETCH` в HQL:
  ```java
  List<Book> books = session.createQuery("FROM Book b JOIN FETCH b.author", Book.class).getResultList();
  ```
- Применить `EntityGraph`:
  ```java
  EntityGraph graph = session.getEntityGraph("Book.author");
  List<Book> books = session.createQuery("FROM Book", Book.class)
                           .setHint("javax.persistence.fetchgraph", graph)
                           .getResultList();
  ```

### Сессия vs EntityManager

- **Session** — это Hibernate-специфичный интерфейс, предоставляющий
  дополнительные методы, недоступные в стандартном JPA.
- **EntityManager** — стандартный интерфейс JPA, который Hibernate реализует.
  `Session` можно получить из `EntityManager` через `unwrap`:
  ```java
  Session session = entityManager.unwrap(Session.class);
  ```
- Если вы используете Hibernate в контексте JPA, `EntityManager` чаще
  используется для стандартизации кода, но `Session` даёт больше гибкости для
  специфичных возможностей Hibernate.

### Жизненный цикл сессии

1. **Создание**: Сессия создаётся через `SessionFactory` с помощью
   `openSession()` или `getCurrentSession()` (в зависимости от конфигурации
   транзакций).
2. **Использование**: Выполняются операции с сущностями, запросы и управление
   транзакциями.
3. **Закрытие**: Сессия закрывается через `close()`, что очищает кэш первого
   уровня и освобождает ресурсы.
4. **Транзакции**: Обычно сессия работает в рамках одной транзакции, хотя можно
   использовать несколько транзакций в одной сессии.

### Контексты управления сессией

1. **Transaction-scoped session**:
    - Сессия создаётся и закрывается в рамках одной транзакции.
    - Используется в простых приложениях или с `openSession()`.

2. **Session-per-request**:
    - Одна сессия на HTTP-запрос (типично для веб-приложений).
    - Часто используется с `getCurrentSession()` в связке с фреймворками, такими
      как Spring.

3. **Long-lived session**:
    - Сессия, которая живёт дольше одной транзакции (например, для длительных
      операций).
    - Требует осторожности из-за накопления объектов в кэше первого уровня.

### Итог

**Сессия** в Hibernate — это интерфейс для управления сущностями, выполнения
запросов и взаимодействия с базой данных. Она управляет контекстом
персистентности, кэшем первого уровня и транзакциями, обеспечивая связь между
Java-объектами и базой данных. Сессия не является потокобезопасной и обычно
используется в рамках одной транзакции или запроса. Для решения проблем, таких
как N+1, можно использовать `JOIN FETCH` или `EntityGraph` в рамках сессии,
чтобы оптимизировать загрузку связанных данных.


-----------


Сессия в Hibernate — это основной интерфейс для взаимодействия с базой данных.
Она представляет собой единицу работы с данными и обеспечивает механизм для
выполнения операций с сущностями, таких как создание, чтение, обновление и
удаление (CRUD). Сессия управляет состоянием объектов и их жизненным циклом, а
также обеспечивает кэширование на уровне первого уровня.

Сессия отслеживает изменения в сущностях, которые она загружает. Когда вы
загружаете сущность из базы данных, она становится "управляемой" (managed)и
находится в состоянии "приложения" (persistent). Если вы изменяете управляемую
сущность, Hibernate автоматически отслеживает эти изменения и может
синхронизировать их с базой данных при вызове метода `session.flush()` или при
закрытии сессии.

Сессия имеет свой собственный первичный кэш (first-level cache), который
хранит загруженные объекты. Если вы запрашиваете одну и ту же сущность
несколько раз в рамках одной сессии, Hibernate будет возвращать объект из
этого кэша вместо выполнения нового запроса к базе данных.

### Основные характеристики сессии Hibernate:

1. **Управление состоянием объектов:**
    - Сессия отслеживает изменения в сущностях, которые она загружает. Когда вы
      загружаете сущность из базы данных, она становится "управляемой" (managed)
      и находится в состоянии "приложения" (persistent).
    - Если вы изменяете управляемую сущность, Hibernate автоматически
      отслеживает эти изменения и может синхронизировать их с базой данных при
      вызове метода `session.flush()` или при закрытии сессии.

2. **Первичный кэш:**
    - Сессия имеет свой собственный первичный кэш (first-level cache), который
      хранит загруженные объекты. Если вы запрашиваете одну и ту же сущность
      несколько раз в рамках одной сессии, Hibernate будет возвращать объект из
      этого кэша вместо выполнения нового запроса к базе данных.

3. **Жизненный цикл:**
    - Сессия создается через `SessionFactory` и должна быть закрыта после
      завершения работы. Это можно сделать вручную или с помощью блока
      `try-with-resources`, чтобы гарантировать закрытие.
    - Сессия может быть открыта в начале транзакции и закрыта после завершения
      всех операций.

4. **Транзакции:**
    - Сессия обычно используется в контексте транзакций. Вы можете начать
      транзакцию, выполнять операции с сущностями и затем зафиксировать или
      откатить изменения.
    - Пример использования транзакций:

```java
      Session session = sessionFactory.openSession();
Transaction transaction = null;
 
      try{
transaction =session.

beginTransaction();
// операции с сущностями
          transaction.

commit();
      }catch(
Exception e){
        if(transaction !=null){
        transaction.

rollback();
          }
                  e.

printStackTrace();
      }finally{
              session.

close();
      }
```

5. **Потокобезопасность:**
    - Сессии не являются потокобезопасными. Каждая сессия должна использоваться
      только одним потоком одновременно. Для многопоточных приложений
      рекомендуется использовать `SessionFactory` для создания новых экземпляров
      `Session`.

### Заключение

Сессия в Hibernate является ключевым компонентом для работы с базой данных,
обеспечивая управление состоянием объектов, кэширование и поддержку транзакций.
Правильное использование сессий позволяет эффективно взаимодействовать с базой
данных и оптимизировать производительность приложения.

================================================================================
--------------------------------------------------------------------------------


## SessionFactory

Фабрика сессий (SessionFactory) в Hibernate — это интерфейс, который отвечает за
создание и управление сессиями (Session). Она является основным компонентом,
который настраивается один раз при запуске приложения и используется для
получения экземпляров сессий. В контексте второго уровня кэширования (Second
Level Cache) фабрика сессий играет важную роль в управлении кэшированием данных
между различными сессиями.

### Основные аспекты фабрики сессий в контексте второго уровня кэширования:

1. **Управление вторичным кэшем:**
    - Фабрика сессий отвечает за настройку и управление вторичным кэшем. Она
      может быть сконфигурирована для использования различных провайдеров кэша,
      таких как Ehcache, Infinispan, Hazelcast и другие.
    - Вторичный кэш позволяет хранить данные между сессиями, что уменьшает
      количество обращений к базе данных и повышает производительность
      приложения.

2. **Конфигурация:**
    - При создании `SessionFactory` вы можете указать параметры конфигурации для
      включения второго уровня кэширования. Это включает в себя указание
      провайдера кэша, настройки его параметров и определение того, какие
      сущности или коллекции должны использовать второй уровень кэша.
    - Пример конфигурации в `hibernate.cfg.xml`:

      ```xml
      <property name="hibernate.cache.use_second_level_cache">true</property>
      <property name="hibernate.cache.region.factory_class">
      org.hibernate.cache.ehcache.EhCacheRegionFactory</property>
      ```

3. **Кэширование сущностей и коллекций:**
    - Вы можете аннотировать ваши сущности или коллекции для использования
      второго уровня кэша. Это позволяет Hibernate сохранять их состояние в
      кэше, что ускоряет доступ к данным.
    - Пример аннотации для включения второго уровня кэша:

```java

@Entity
@Cacheable
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Author {
    @Id
    private Long id;
    // другие поля и методы
}
```

4. **Производительность:**
    - Использование второго уровня кэширования может значительно улучшить
      производительность приложения, особенно если у вас есть часто
      запрашиваемые данные или данные, которые редко изменяются.
    - Однако важно правильно настроить стратегию кеширования (например,
      `READ_ONLY`, `READ_WRITE`, `NONSTRICT_READ_WRITE`, `TRANSACTIONAL`) в
      зависимости от требований вашего приложения.

5. **Создание и закрытие:**
    - Фабрика сессий создается один раз при запуске приложения и обычно
      закрывается при завершении работы приложения. Это позволяет избежать
      накладных расходов на создание новых экземпляров фабрики при каждом
      запросе.

### Заключение

Фабрика сессий в Hibernate является ключевым компонентом для управления сессиями
и вторичным кэшированием. Она обеспечивает возможность настройки и использования
различных стратегий кеширования, что позволяет оптимизировать производительность
приложений за счет уменьшения количества обращений к базе данных. Правильная
конфигурация фабрики сессий и использование второго уровня кэширования могут
существенно повысить эффективность работы вашего приложения.

================================================================================
--------------------------------------------------------------------------------

# HQL, native, JPQL, Criteria запросы

### HQL

HQL (Hibernate Query Language) — это язык запросов, очень похожий на SQL, но с
одной ключевой разницей:

> **HQL работает с сущностями и их полями, а не с таблицами и колонками.**

---

## 🧩 Что это значит?

Если в SQL ты пишешь:

```sql
SELECT *
FROM users
WHERE name = 'Ivan';
```

То в HQL это будет:

```java
SELECT u
FROM User
u WHERE
u.name ='Ivan'
```

🧠 Здесь:

- `User` — это **Java-класс сущности**
- `u.name` — это **Java-поле**, а не колонка базы

---

## 🔧 Основы синтаксиса HQL

| Операция    | Пример                                              |
|-------------|-----------------------------------------------------|
| SELECT      | `SELECT u FROM User u`                              |
| WHERE       | `WHERE u.name = :name`                              |
| JOIN        | `JOIN u.orders o`                                   |
| ORDER BY    | `ORDER BY u.createdAt DESC`                         |
| COUNT / AVG | `SELECT COUNT(u) FROM User u`                       |
| UPDATE      | `UPDATE User u SET u.name = :name WHERE u.id = :id` |
| DELETE      | `DELETE FROM User u WHERE u.id = :id`               |

---

## 🔍 Пример запроса с параметрами

```java
Query query = session.createQuery("FROM User u WHERE u.age > :minAge");
query.

setParameter("minAge",18);

List<User> users = query.getResultList();
```

---

## 🔄 JOIN и FETCH

```java
SELECT u
FROM User
u JOIN
FETCH u.orders
```

- `JOIN` — объединение сущностей
- `FETCH` — говорит Hibernate сразу загрузить связанные данные (решает проблему
  N+1)

---

## 🧱 Отличие от SQL

|                        | SQL                                    | HQL                            |
|------------------------|----------------------------------------|--------------------------------|
| Работает с             | Таблицами и колонками                  | Сущностями и их полями         |
| Автоматическое маппинг | ❌ Нет                                  | ✅ Да                           |
| Названия               | Таблица: `users`, колонка: `user_name` | Сущность: `User`, поле: `name` |
| Результат              | `Object[]`, `Map`, строки              | Объекты Java (`User`, `Order`) |

---

## 📦 Где используется HQL?

- В методах DAO/репозиториев
- В `@Query` в Spring Data JPA:

```java

@Query("SELECT u FROM User u WHERE u.name = :name")
List<User> findByName(@Param("name") String name);
```

---

## ❗ Важно: HQL ≠ SQL

- В HQL нельзя использовать `SELECT *`
- Нужно всегда обращаться к **именам классов и полей**, не к названиям таблиц

---

## ✨ Альтернатива: Criteria API или Native SQL

| Вариант          | Когда использовать                     |
|------------------|----------------------------------------|
| **HQL**          | Большинство запросов, удобно и читаемо |
| **Criteria API** | Сложные и динамические фильтры         |
| **Native SQL**   | Максимальная гибкость, сложные джоины  |

---

### Native

Отличный вопрос! В Hibernate **native запросы** — это **SQL-запросы**,
написанные напрямую, **без использования HQL** или `Criteria API`.

---

## 🔍 Что такое native запрос?

> Это **"сырой" SQL-запрос**, который выполняется прямо в базу данных, **в обход
Hibernate Query Language** (HQL).

Например:

```java
Query query = entityManager.createNativeQuery("SELECT * FROM users WHERE name = ?", User.class);
query.

setParameter(1,"Ivan");

List<User> result = query.getResultList();
```

Здесь ты используешь реальное SQL и реальные **имена таблиц и колонок** из базы.

---

## 🤔 Зачем использовать native-запросы?

### ✅ Когда HQL или Criteria API не хватает:

- Сложные `JOIN`-ы, `GROUP BY`, подзапросы, оконные функции
- Запросы к **представлениям (VIEW)** или **хранимым процедурам**
- Использование **оптимизированных индексов, кастомных функций** БД
- Использование **базы, не полностью совместимой с JPQL**

---

## 🛠 Пример с маппингом сущностей

```java

@Query(
        value = "SELECT * FROM users WHERE name = :name",
        nativeQuery = true)
List<User> findByName(@Param("name") String name);
```

Spring Data JPA позволяет использовать `nativeQuery = true`, и возвращать
готовые сущности.

---

## ⚠️ Особенности:

| Особенность                | Описание                                                     |
|----------------------------|--------------------------------------------------------------|
| 🎯 Работает с SQL          | Использует **имена таблиц и колонок**, а не классы и поля    |
| 🧠 Требует явного маппинга | Нужно точно указать, какие колонки соответствуют каким полям |
| 🚫 Игнорирует кэш          | Hibernate-кэш не используется (1st и 2nd level)              |
| ✅ Можно использовать DTO   | Да, можно маппить в произвольные объекты, даже не сущности   |

---

## 📦 Пример: маппинг в DTO (не в Entity)

```java
List<Object[]> rows = entityManager.createNativeQuery(
                "SELECT u.name, COUNT(o.id) FROM users u JOIN orders o ON o.user_id = u.id GROUP BY u.name")
        .getResultList();

for(
Object[] row :rows){
String name = (String) row[0];
Long count = ((Number) row[1]).longValue();
}
```

---

## 🔥 Альтернатива: `@SqlResultSetMapping`

Можно создать кастомную маппинг-схему:

```java
@SqlResultSetMapping(
        name = "UserStatsMapping",
        classes = @ConstructorResult(
                targetClass = UserStatsDto.class,
                columns = {
                        @ColumnResult(name = "name", type = String.class),
                        @ColumnResult(name = "total", type = Long.class)
                }
        )
)
```

И использовать:

```java
List<UserStatsDto> stats = em.createNativeQuery(
        "SELECT name, COUNT(*) as total FROM users GROUP BY name",
        "UserStatsMapping"
).getResultList();
```

---

## ✅ Когда стоит использовать native SQL?

| Используй если...                                |
|--------------------------------------------------|
| ...нужны сложные запросы, которые HQL не потянет |
| ...работаешь с нестандартной БД или VIEW         |
| ...нужна максимальная производительность         |
| ...используешь сторонние хранимые процедуры      |

---

Хочешь, покажу, как писать нативные запросы с `JOIN`, `IN`, `LIKE`, или как
маппить их сразу в DTO через Spring Data?

--------------------------------------------------------------------------------

### JPQL

## 🧠 Что такое JPQL?

**JPQL (Java Persistence Query Language)** — это **стандартизированный язык
запросов**, который входит в JPA (Java Persistence API). Он **похож на SQL**, но
работает **с Java-сущностями и их полями**, а **не с таблицами и колонками**
базы данных.

> То есть: **JPQL = SQL для объектов**, а не для таблиц.

---

## 🔍 Пример: отличие SQL и JPQL

### 📌 SQL:

```sql
SELECT *
FROM users
WHERE name = 'Ivan';
```

### 📌 JPQL:

```java
SELECT u
FROM User
u WHERE
u.name ='Ivan'
```

- `User` — это Java-класс (Entity)
- `u.name` — это поле Java-класса, а не колонка в таблице

---

## ✅ Особенности JPQL

| Характеристика              | JPQL                                                                  |
|-----------------------------|-----------------------------------------------------------------------|
| Работает с                  | Java-классами (Entity)                                                |
| Тип результата              | Java-объекты                                                          |
| Поддерживает                | `SELECT`, `JOIN`, `WHERE`, `GROUP BY`, `ORDER BY`, `UPDATE`, `DELETE` |
| Не поддерживает             | SQL-специфичные функции, `SELECT *`, `JOIN ON` и т.п.                 |
| Совместим с Hibernate и JPA | Да, полностью                                                         |

---

## 🛠 Примеры JPQL-запросов

### 🔹 Простое выборка:

```sql
SELECT u
FROM User u
WHERE u.age > 18
```

### 🔹 JOIN:

```sql
SELECT o
FROM Order o
         JOIN
     o.user u
WHERE u.name = 'Ivan'
```

### 🔹 Группировка:

```java
SELECT u.name,

COUNT(o) FROM User u JOIN u.
orders o
GROUP BY
u.name
```

### 🔹 Обновление:

```java
UPDATE User
u SET
u.active =false
WHERE u.lastLogin< :date
```

### 🔹 Удаление:

```java
DELETE FROM
User u
WHERE u.status ='INACTIVE'
```

---

## 📦 Использование в JPA / Hibernate

```java
TypedQuery<User> query = entityManager.createQuery(
        "SELECT u FROM User u WHERE u.name = :name", User.class);
query.

setParameter("name","Ivan");

List<User> users = query.getResultList();
```

---

## 🔗 Использование в Spring Data JPA

```java

@Query("SELECT u FROM User u WHERE u.email = :email")
Optional<User> findByEmail(@Param("email") String email);
```

> Если не указать `nativeQuery = true`, то по умолчанию это **JPQL**, а не SQL!

---

## 🔄 Сравнение JPQL, HQL и SQL

| Характеристика | JPQL                | HQL (Hibernate)     | SQL                   |
|----------------|---------------------|---------------------|-----------------------|
| Стандарт       | ✅ Да (JPA)          | ❌ Только Hibernate  | ✅ Базовый для всех БД |
| Работает с     | Сущностями и полями | Сущностями и полями | Таблицами и колонками |
| Кросс-движок   | ✅ Да                | ❌ Только Hibernate  | ✅ Да                  |
| Кэширование    | ✅ Да                | ✅ Да                | ❌ Нет (по умолчанию)  |

---

Хочешь, покажу реальный пример запроса с `JOIN FETCH`, `IN`, `LIKE`, `ORDER BY`,
или как комбинировать JPQL с DTO-проекциями?

### Отличие HQL от JPQL

HQL и JPQL часто используются **как синонимы**, но между ними
есть **некоторые отличия**, и важно понимать, в чём суть.

|                   | **HQL** (Hibernate Query Language) | **JPQL** (Java Persistence Query Language)                           |
|-------------------|------------------------------------|----------------------------------------------------------------------|
| Стандартизован    | ❌ Нет, специфичен для Hibernate    | ✅ Да, часть спецификации JPA                                         |
| Реализация        | Только Hibernate                   | Поддерживается всеми JPA-провайдерами (Hibernate, EclipseLink и др.) |
| Расширения и фичи | Имеет **расширения** сверх JPA     | Только стандарт JPA                                                  |
| Совместимость     | Работает только в Hibernate        | Работает в любой реализации JPA                                      |
| Использование     | `session.createQuery(...)`         | `entityManager.createQuery(...)`                                     |

_**Пример: одинаковый синтаксис**_

```java
SELECT u
FROM User
u WHERE
u.age >18
```

Этот запрос абсолютно одинаков в HQL и JPQL.

Главное отличие — в **возможностях**

HQL — мощнее, но привязан к Hibernate:

- Поддерживает:
    - `insert into ... select`
    - `join fetch` даже без явных связей
    - кастомные функции (`function('yourFunc', ...)`)
    - `@Filter`, `@Subselect`, и другие специфичные для Hibernate фишки

JPQL — кросс-стандарт, но ограничен:

- Более строгий
- Не поддерживает `insert into ... select`
- Нельзя использовать произвольные SQL-функции без расширений

#### Примеры различий

_**JPQL не поддерживает:**_

```java
INSERT INTO

Entity(field) SELECT ...
```

-> Это доступно **только в HQL**.

_**JPQL поддерживает стандартные функции:**_

```java
SELECT LENGTH(u.name) FROM User u
```

Но если ты хочешь что-то кастомное вроде:

```java
SELECT function('my_custom_sql_func',u.name) FROM User u
```

→ Это **только для HQL/Hibernate**.

_**В Spring Data JPA**_

Когда ты пишешь:

```java
@Query("SELECT u FROM User u WHERE u.email = :email")
```

Это **JPQL** по умолчанию.

#### Вывод

| Когда использовать?                      | Что выбрать?          |
|------------------------------------------|-----------------------|
| Максимальная совместимость               | **JPQL**              |
| Используешь только Hibernate             | **HQL** (больше фич)  |
| Работаешь с Spring Data                  | **JPQL по умолчанию** |
| Нужен `insert-select`, кастомные функции | **HQL**               |

=========

### Пример где HQL даёт больше гибкости, чем JPQL — или наоборот

Круто! Вот тебе **наглядное сравнение** на конкретных примерах, где **HQL мощнее
JPQL**, и наоборот 💡
---

## ⚙️ Пример 1: `INSERT INTO ... SELECT ...` (✅ HQL, ❌ JPQL)

### ✅ **HQL**:

```java
session.createQuery(
  "INSERT INTO ArchivedUser (id, name) "+
          "SELECT u.id, u.name FROM User u WHERE u.active = false")
  .

executeUpdate();
```

→ **Позволяет вставить** данные из одной сущности в другую (insert-select).

### ❌ **JPQL**:

> Такой конструкции в JPQL **нет по спецификации**. Только `UPDATE` и `DELETE`
> разрешены.

---

## 🔄 Пример 2: Кастомная SQL-функция (✅ HQL, ❌ JPQL)

### ✅ **HQL**:

```java
Query q = session.createQuery(
        "SELECT function('my_custom_db_function', u.name) FROM User u");
```

→ HQL позволяет использовать нативные функции базы через `function(...)`.

### ❌ **JPQL**:

> Такое **не работает без расширений**. Только стандартные функции (`LENGTH`,
`UPPER`, `LOWER`, `TRIM`, `CONCAT` и т.д.)

---

## ✅ Пример 3: Стандартизированные функции (✅ JPQL, ✅ HQL)

```java
SELECT u
FROM User
u WHERE

LENGTH(u.name) >5
```

→ Это работает **и в JPQL, и в HQL**, потому что это **часть JPA-стандарта**.

---

## 🔗 Пример 4: JOIN FETCH (✅ JPQL, ✅ HQL)

```java
SELECT u
FROM User
u JOIN
FETCH u.roles
```

→ Работает и там, и там. Это способ **решить проблему N+1** (жадная загрузка
связанных сущностей).

---

## 💣 Пример 5: @Filter и @Subselect (✅ HQL, ❌ JPQL)

Если у тебя есть, например, такой фильтр:

```java
@Filter(name = "activeUsers", condition = "active = true")
```

→ Ты можешь его применить **только в HQL**, потому что это *
*Hibernate-специфично**.

---

## 🔧 Пример 6: Использование DTO (✅ JPQL, ✅ HQL)

```java
SELECT new com.example.dto.

UserDto(u.name, u.age)

FROM User
u
WHERE u.active =true
```

→ Это работает и в **JPQL**, и в **HQL** — просто должен быть соответствующий *
*конструктор**.

---

## 🧾 Резюме:

| Возможность                              | JPQL  | HQL   |
|------------------------------------------|-------|-------|
| `INSERT INTO ... SELECT ...`             | ❌ Нет | ✅ Да  |
| Кастомные SQL-функции через `function()` | ❌ Нет | ✅ Да  |
| JOIN FETCH                               | ✅ Да  | ✅ Да  |
| DTO-проекции (`new Dto(...)`)            | ✅ Да  | ✅ Да  |
| Поддержка Hibernate @Filter/@Subselect   | ❌ Нет | ✅ Да  |
| Совместимость с другими JPA-провайдерами | ✅ Да  | ❌ Нет |

=======

Отличный вопрос! 🔍 Аннотации `@Filter` и `@Subselect` — это **расширения
Hibernate**, которые позволяют более гибко управлять выборкой данных. Они **не
входят в стандарт JPA**, но дают крутую мощность при работе с сущностями.

Разберём по порядку:

---

## 🔹 `@Filter` — Фильтрация данных на уровне Hibernate

### 📌 Что это?

`@Filter` позволяет **добавлять условие к запросам автоматически**, без явного
указания `WHERE` каждый раз.

> Это похоже на "динамическое условие", которое можно **включать и выключать
вручную**.

---

### 🛠 Пример использования `@Filter`

```java

@Entity
@FilterDef(name = "activeFilter", parameters = @ParamDef(name = "isActive", type = "boolean"))
@Filters({
        @Filter(name = "activeFilter", condition = "active = :isActive")
})
public class User {
    @Id
    private Long id;

    private String name;

    private boolean active;
}
```

Теперь можно применить фильтр при загрузке:

```java
Session session = entityManager.unwrap(Session.class);
session.

enableFilter("activeFilter").

setParameter("isActive",true);

List<User> users = session.createQuery("from User", User.class).getResultList();
```

🔍 Здесь Hibernate **сам подставит `WHERE active = true`** в запрос, даже если ты
его явно не писал.

---

### ✅ Когда использовать `@Filter`?

- Фильтрация **по soft delete** (`deleted = false`)
- Фильтрация по `tenantId` (multi-tenant архитектура)
- Отображение только активных сущностей без лишнего дублирования логики

---

## 🔹 `@Subselect` — Сущность на основе SQL-подзапроса

### 📌 Что это?

`@Subselect` позволяет **создать сущность на основе SQL-запроса**, а не таблицы.
Это удобно, если ты хочешь отобразить **view, агрегаты или подзапросы**.

---

### 🛠 Пример `@Subselect`

```java

@Entity
@Immutable
@Subselect(
        "SELECT u.id AS id, u.name AS name, COUNT(o.id) AS ordersCount " +
                "FROM users u LEFT JOIN orders o ON o.user_id = u.id " +
                "GROUP BY u.id, u.name"
)
public class UserSummary {

    @Id
    private Long id;

    private String name;

    private Long ordersCount;
}
```

> Здесь `UserSummary` — это сущность, **не связанная напрямую с таблицей**, а
> создаваемая из подзапроса.

---

### 🧩 Особенности `@Subselect`

| Особенность          | Описание                                |
|----------------------|-----------------------------------------|
| 📌 Не обновляемая    | Обычно используется с `@Immutable`      |
| 🔒 Только для чтения | Hibernate не знает, как её обновить     |
| 🧠 Работает как view | Можно использовать как DTO с SQL внутри |

---

## 🧾 Вывод

| Аннотация    | Что делает                                        | Где полезно                                   |
|--------------|---------------------------------------------------|-----------------------------------------------|
| `@Filter`    | Добавляет **динамические WHERE-условия**          | Soft delete, tenant filtering, флаги активных |
| `@Subselect` | Создаёт **view-like сущности** из SQL подзапросов | Агрегации, отчёты, представления              |

---

====
Отлично! 😎 Давай разберём, что делает аннотация **`@Immutable`** в Hibernate,
когда её применять, и зачем она нужна.

---

## 🔒 Что такое `@Immutable`?

**`@Immutable`** — это аннотация из Hibernate, которая говорит:

> "Эта сущность **никогда не изменяется** после загрузки, поэтому Hibernate не
> должен пытаться её обновлять или отслеживать изменения."

---

## 📦 Где находится?

```java
import org.hibernate.annotations.Immutable;
```

---

## 📌 Пример использования

```java

@Entity
@Immutable
public class Country {

    @Id
    private Long id;

    private String name;
}
```

Теперь Hibernate будет:

✅ Загружать `Country` из базы  
❌ **Не будет** отслеживать или сохранять изменения в `Country`  
❌ `session.save()`, `session.update()` — вызовут исключение или будут
проигнорированы

---

## 🧠 Где `@Immutable` особенно полезен?

| Где?                        | Почему?                                                            |
|-----------------------------|--------------------------------------------------------------------|
| **Справочники (enum-like)** | Например, `Country`, `Currency`, `Role`, которые не меняются       |
| **Агрегаты/представления**  | В сочетании с `@Subselect` или `View` — т.к. это только для чтения |
| **Исторические данные**     | Таблицы, в которые пишутся логи, но не изменяются                  |

---

## 🚫 Что произойдёт, если изменить `@Immutable`-сущность?

Hibernate **не будет** сохранять изменения в БД. Даже если ты что-то поменяешь и
вызовешь `flush()` — всё проигнорируется (или выбросится `PersistenceException`,
если явно вызвать `merge()`/`update()`).

```java
country.setName("Новое имя");
entityManager.

flush(); // Hibernate проигнорирует изменение
```

---

## ✅ Полезно в сочетании с:

- `@Subselect` — сущность из SQL-запроса (она не обновляется)
- `@JoinFormula` — вычисляемые поля
- `@NaturalId` — при кэшировании справочных данных
- `@Cache(usage = CacheConcurrencyStrategy.READ_ONLY)` — кэш только для чтения

---

## ⚠️ Важно:

- `@Immutable` работает **на уровне класса**, а не поля
- **Не влияет** на SQL-уровень — просто говорит Hibernate **не трогать** объект
- Можно использовать с аннотацией `@Entity`, `@Subselect`, `@View`

---

## 💡 Пример с `@Subselect` и `@Immutable`

```java

@Entity
@Immutable
@Subselect("SELECT id, name, count(*) AS count FROM some_view GROUP BY id, name")
public class ReportItem {

    @Id
    private Long id;

    private String name;

    private Long count;
}
```

→ Отличный кейс: отчёт только для чтения. Hibernate не должен его менять.

---

Хочешь, покажу, как сочетать `@Immutable` с
`@Cache` для максимально эффективной загрузки справочников или с
`@Filter` для "только читаемых, но отфильтрованных" коллекций?
===

### Criteria запросы

Отличный вопрос! 🧱 **Criteria-запросы** в JPA (или Hibernate) — это способ *
*построения запросов через Java-код**, а не через строки. Это особенно полезно,
когда запрос должен быть **динамическим**, например, фильтры меняются во время
выполнения.

---

## 🔍 Что такое Criteria API?

> Это **типобезопасный**, **программный способ** построения JPQL-запросов.

Вместо строки:

```java
"SELECT u FROM User u WHERE u.age > 18"
```

Ты строишь запрос через Java-объекты:

```java
CriteriaBuilder cb = entityManager.getCriteriaBuilder();
CriteriaQuery<User> query = cb.createQuery(User.class);
Root<User> root = query.from(User.class);
query.

select(root).

where(cb.gt(root.get("age"), 18));
```

---

## 🧠 Когда использовать Criteria?

| Подходит если…                            | Лучше избегать если…                      |
|-------------------------------------------|-------------------------------------------|
| Нужно **динамически строить** фильтры     | Запрос простой и фиксированный            |
| Нужна **типобезопасность** (IDE помогает) | Нужно просто написать SELECT в пару строк |
| Формируешь запрос из разных условий       | Хочешь читаемость и лаконичность          |

---

## ⚙️ Пример: простой Criteria-запрос

```java
CriteriaBuilder cb = entityManager.getCriteriaBuilder();
CriteriaQuery<User> query = cb.createQuery(User.class);
Root<User> user = query.from(User.class);

query.

select(user)
     .

where(cb.equal(user.get("name"), "Ivan"));

List<User> result = entityManager.createQuery(query).getResultList();
```

---

## 🔄 Пример с динамическими фильтрами

```java
CriteriaBuilder cb = entityManager.getCriteriaBuilder();
CriteriaQuery<User> query = cb.createQuery(User.class);
Root<User> user = query.from(User.class);

List<Predicate> predicates = new ArrayList<>();

if(nameFilter !=null){
        predicates.

add(cb.like(user.get("name"), "%"+nameFilter +"%"));
        }
        if(minAge !=null){
        predicates.

add(cb.ge(user.get("age"),minAge));
        }

        query.

select(user).

where(cb.and(predicates.toArray(new Predicate[0])));
```

---

## 🔁 Сортировка

```java
query.orderBy(cb.asc(user.get("name")));
```

---

## 🔁 JOIN в Criteria

```java
Join<User, Order> orders = user.join("orders");
query.

where(cb.equal(orders.get("status"), "PAID"));
```

---

## ✅ Плюсы Criteria API

- ✅ Типобезопасность (ошибки видны на этапе компиляции)
- ✅ Поддержка динамики (фильтры, сортировки и т.п.)
- ✅ Интеграция с JPA и Spring Data JPA

---

## ❌ Минусы

- 😵 Много кода на простые запросы
- 🤯 Меньше читаемости по сравнению с JPQL/HQL
- 💬 Сложнее поддерживать, особенно для длинных JOIN'ов

---

## 💡 Альтернатива: QueryDSL (ещё удобнее, чем Criteria)

Если тебе нравится идея динамических запросов, но хочется писать их удобнее, —
можешь посмотреть на [**QueryDSL**](http://www.querydsl.com/) — он делает то же
самое, но **в стиле fluent API**:

```java
QUser user = QUser.user;
JPAQuery<User> query = new JPAQuery<>(entityManager);
List<User> users = query.select(user)
        .from(user)
        .where(user.age.gt(18))
        .fetch();
```

---

=============

Отличный вопрос! В контексте **Hibernate (и JPA)** термины `eager` и `lazy`
относятся к **типу загрузки (fetching strategy)** связанных сущностей.

---

## 📦 Что такое `EAGER` и `LAZY`?

Они определяют, **когда Hibernate загружает связанные данные** из базы данных:

| Стратегия | Описание                                                       |
|-----------|----------------------------------------------------------------|
| **LAZY**  | Загрузка **по требованию**, только когда реально используется. |
| **EAGER** | Загрузка **сразу**, вместе с родительским объектом.            |

---

## 📘 Пример

Представим, есть сущность `User` и у него есть список `orders`.

```java

@Entity
public class User {
    @OneToMany(mappedBy = "user", fetch = FetchType.LAZY) // или EAGER
    private List<Order> orders;
}
```

---

## 🐢 `LAZY` (ленивая загрузка)

- `orders` не загружается при загрузке `User`.
- Загружается **только при первом доступе к `getOrders()`**.
- Подходит, если тебе **не всегда нужны связанные данные**.

```java
User user = entityManager.find(User.class, 1L); // SELECT * FROM users WHERE id = 1;
List<Order> orders = user.getOrders();         // <-- В этот момент SELECT * FROM orders ...
```

---

## ⚡ `EAGER` (жадная загрузка)

- `orders` загружается **сразу** вместе с `User`.
- Даже если ты **не используешь `orders`**, Hibernate всё равно подгрузит их.

```java
User user = entityManager.find(User.class, 1L);
// Выполнится JOIN-запрос или два отдельных SELECT’а
```

---

## 📊 Сравнение

| Особенность                                               | `LAZY`                             | `EAGER`                         |
|-----------------------------------------------------------|------------------------------------|---------------------------------|
| Производительность                                        | 🔼 Лучше (если не нужна коллекция) | 🔽 Может тянуть ненужные данные |
| Управление запросами                                      | ✅ Гибко                            | ❌ Меньше контроля               |
| Риск `LazyInitializationException`                        | ❌ Есть (если сессия закрыта)       | ✅ Нет                           |
| Используется по умолчанию для `@OneToMany`, `@ManyToMany` | ✅ Да (`LAZY`)                      | ❌ Нет (`EAGER` вручную)         |

---

## ⚠️ Важные нюансы

- Hibernate **по умолчанию делает `LAZY`** для коллекций (`@OneToMany`,
  `@ManyToMany`) и **`EAGER`** для одиночных ссылок (`@ManyToOne`, `@OneToOne`).
- У **`LAZY` загрузки есть риск**: если сессия уже закрыта, доступ к полю
  вызовет `LazyInitializationException`.

---

## 🛠️ Как защититься от `LazyInitializationException`?

- Используй `JOIN FETCH` в HQL/JPQL.
- Используй DTO и `EntityGraph`.
- Загружай данные внутри открытой транзакции (например, в `@Transactional`
  методе).

---

Хочешь пример с `JOIN FETCH` или как правильно подгрузить LAZY-поле в
контроллере?

=============

Отличный вопрос! В **Hibernate / JPA** стратегии связывания сущностей
определяются через аннотации отношений между объектами, и отвечают на вопрос: *
*как одна сущность связана с другой?** И как эта связь хранится в базе данных.

---

## 🔗 Основные типы связей между сущностями

| Тип связи     | Описание         | Примеры                     |
|---------------|------------------|-----------------------------|
| `@OneToOne`   | Один к одному    | У пользователя один профиль |
| `@OneToMany`  | Один ко многим   | Один автор — много книг     |
| `@ManyToOne`  | Многие к одному  | Много заказов — один клиент |
| `@ManyToMany` | Многие ко многим | Пользователи и роли         |

---

### 📘 Примеры аннотаций

#### 1. **@OneToOne**

```java

@Entity
public class User {
    @OneToOne
    @JoinColumn(name = "profile_id")
    private Profile profile;
}
```

> 🧠 В базе: `user.profile_id → profile.id`

---

#### 2. **@OneToMany / @ManyToOne**

```java

@Entity
public class Author {
    @OneToMany(mappedBy = "author")
    private List<Book> books;
}

@Entity
public class Book {
    @ManyToOne
    @JoinColumn(name = "author_id")
    private Author author;
}
```

> В таблице `book` есть колонка `author_id`

---

#### 3. **@ManyToMany**

```java

@Entity
public class Student {
    @ManyToMany
    @JoinTable(
            name = "student_course",
            joinColumns = @JoinColumn(name = "student_id"),
            inverseJoinColumns = @JoinColumn(name = "course_id")
    )
    private List<Course> courses;
}
```

> 🧠 Связь через промежуточную таблицу `student_course(student_id, course_id)`

---

## 🧩 Стратегии каскадирования (`cascade`)

Позволяют управлять действиями над связанными сущностями:

```java
@OneToMany(cascade = CascadeType.ALL)
```

| Cascade Type        | Что делает                   |
|---------------------|------------------------------|
| `ALL`               | Применяет все действия       |
| `PERSIST`           | Сохраняет связанные сущности |
| `MERGE`             | Обновляет их                 |
| `REMOVE`            | Удаляет                      |
| `DETACH`, `REFRESH` | Прочее управление состоянием |

---

## 💾 Дополнительно: `mappedBy` vs `JoinColumn`

- `mappedBy` — на стороне **обратной связи** (не владеющей)
- `@JoinColumn` — **владеющая сторона** (где находится внешний ключ)

---

## 🧠 Советы

- Для `@OneToMany` желательно всегда указывать `mappedBy`, иначе Hibernate может
  создать **лишнюю таблицу связи**.
- При `@ManyToMany` используется **промежуточная таблица**, и `@JoinTable` нужно
  указывать явно.
- Не злоупотребляй `CascadeType.ALL` — это может привести к неожиданным
  удалениям/обновлениям.

====

Отлично, давай подробно разберёмся, что такое **тип загрузки (fetching strategy)
** для связанных сущностей в JPA / Hibernate — это ключевой момент для
производительности и правильной работы с данными.

---

## 📦 Что такое Fetching Strategy?

**Fetching strategy** — это способ, которым **Hibernate загружает связанные
сущности** из базы данных при обращении к основному объекту.

Есть два типа:

| Тип загрузки       | Описание                                                              |
|--------------------|-----------------------------------------------------------------------|
| **LAZY** (ленивая) | Связанная сущность загружается **только при первом обращении** к ней. |
| **EAGER** (жадная) | Связанная сущность загружается **сразу вместе с родительской**.       |

---

## 🔍 Где это используется?

В аннотациях связей:

```java
@OneToMany(fetch = FetchType.LAZY)
@ManyToOne(fetch = FetchType.EAGER)
```

---

## 🧪 Пример

```java

@Entity
public class User {
    @OneToMany(mappedBy = "user", fetch = FetchType.LAZY)
    private List<Order> orders;
}
```

- Когда ты загружаешь `User`, `orders` **не будут загружены**.
- Они загрузятся **только при вызове `user.getOrders()`**.

---

## 📊 Сравнение: LAZY vs EAGER

| Параметр                | LAZY                                               | EAGER                                      |
|-------------------------|----------------------------------------------------|--------------------------------------------|
| Когда загружается?      | При первом доступе к полю                          | Сразу, при загрузке объекта                |
| Производительность      | Лучше, если данные не нужны                        | Может тянуть ненужные данные               |
| Количество SQL-запросов | Больше (при обращении к полю)                      | Меньше (если данные нужны сразу)           |
| Возможные ошибки        | `LazyInitializationException`, если сессия закрыта | Нет ошибок, но можно грузить слишком много |

---

## 🔄 Как подгрузить связанные сущности вручную?

1. **`JOIN FETCH` в JPQL**:

```java
SELECT u
FROM User
u JOIN
FETCH u.
orders WHERE
u.id =:id
```

2. **EntityGraph**:

```java

@EntityGraph(attributePaths = {"orders"})
User user = em.find(User.class, id, hints);
```

3. **Hibernate.initialize()**:

```java
Hibernate.initialize(user.getOrders());
```

(работает только в открытой сессии)

---

## 📘 По умолчанию:

| Связь         | Тип загрузки по умолчанию |
|---------------|---------------------------|
| `@ManyToOne`  | `EAGER`                   |
| `@OneToOne`   | `EAGER`                   |
| `@OneToMany`  | `LAZY`                    |
| `@ManyToMany` | `LAZY`                    |

---

## ✅ Рекомендации

- Всегда **используй LAZY по умолчанию**, и **подгружай явно**, когда нужно.
- Используй **JOIN FETCH** или **DTO**-проекцию, чтобы избежать N+1.
- Не забывай про `@Transactional`, если используешь LAZY в сервисах.

===================================================================
------------------------------------------------------------------

# Стратегии генерирования id

В Hibernate существует несколько **стратегий генерации идентификаторов (ID)**,
которые определяют, как создаются значения для первичных ключей сущностей. Эти
стратегии задаются с помощью аннотации `@GeneratedValue` и параметра `strategy`.

### Основные стратегии генерации ID

1. `GenerationType.AUTO`

- 📌 **Hibernate сам выбирает стратегию** в зависимости от диалекта базы данных.
- Может выбрать `IDENTITY`, `SEQUENCE`, `TABLE`, в зависимости от СУБД.

```java

@Id
@GeneratedValue(strategy = GenerationType.AUTO)
private Long id;
```

2. `GenerationType.IDENTITY`

- Использует **автоинкремент** (`AUTO_INCREMENT`, `IDENTITY`) на уровне базы
  данных.
- Подходит для MySQL, SQL Server.

```java

@Id
@GeneratedValue(strategy = GenerationType.IDENTITY)
private Long id;
```

🟡 **Минусы**:

- ID генерируется **только при insert**, не работает с batch inserts.
- Не кэшируется Hibernate’ом.


3. `GenerationType.SEQUENCE`

- Использует **sequence** из базы данных (обычно в PostgreSQL, Oracle).
- Позволяет Hibernate **предсказывать** следующий ID и кэшировать.

```java

@Id
@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_gen")
@SequenceGenerator(name = "seq_gen", sequenceName = "my_seq", allocationSize = 1)
private Long id;
```

🟢 **Плюсы**:

- Можно настраивать `allocationSize` для пакетной генерации.
- Идеально для PostgreSQL.


4. `GenerationType.TABLE`

- Создаёт **отдельную таблицу** для хранения текущего значения ID.
- Работает в любой СУБД.

```java

@Id
@GeneratedValue(strategy = GenerationType.TABLE, generator = "tbl_gen")
@TableGenerator(name = "tbl_gen", table = "id_table", pkColumnName = "key_name",
        valueColumnName = "next_val", pkColumnValue = "my_entity", allocationSize = 1)
private Long id;
```

🔴 **Минусы**:

- Самая **медленная стратегия**, потому что требует обращения к специальной
  таблице.
- Используется, если нет поддержки `SEQUENCE` или `IDENTITY`.

---

## 🧠 Альтернативы:

- **UUID / GUID**:
  ```java
  @Id
  @GeneratedValue(generator = "uuid2")
  @GenericGenerator(name = "uuid2", strategy = "uuid2")
  private UUID id;
  ```

- **Custom генераторы**:
    - Можно реализовать собственную стратегию генерации через
      `IdentifierGenerator`.

---

## 🔍 Как выбрать стратегию?

| Ситуация                              | Рекомендуемая стратегия      |
|---------------------------------------|------------------------------|
| MySQL / SQL Server                    | `IDENTITY`                   |
| PostgreSQL / Oracle                   | `SEQUENCE`                   |
| Кросс-БД совместимость                | `TABLE` или `AUTO`           |
| Используешь UUID                      | `uuid2` / `uuid`             |
| Нужен контроль / нестандартная логика | `Custom IdentifierGenerator` |

---

================================================================================
--------------------------------------------------------------------------------

# Тип загрузки (fetching strategy)** для связанных сущностей в JPA

Отлично, давай подробно разберёмся, что такое **тип загрузки (fetching strategy)
** для связанных сущностей в JPA / Hibernate — это ключевой момент для
производительности и правильной работы с данными.

---

## 📦 Что такое Fetching Strategy?

**Fetching strategy** — это способ, которым **Hibernate загружает связанные
сущности** из базы данных при обращении к основному объекту.

Есть два типа:

| Тип загрузки       | Описание                                                              |
|--------------------|-----------------------------------------------------------------------|
| **LAZY** (ленивая) | Связанная сущность загружается **только при первом обращении** к ней. |
| **EAGER** (жадная) | Связанная сущность загружается **сразу вместе с родительской**.       |

---

## 🔍 Где это используется?

В аннотациях связей:

```java
@OneToMany(fetch = FetchType.LAZY)
@ManyToOne(fetch = FetchType.EAGER)
```

---

## 🧪 Пример

```java

@Entity
public class User {
    @OneToMany(mappedBy = "user", fetch = FetchType.LAZY)
    private List<Order> orders;
}
```

- Когда ты загружаешь `User`, `orders` **не будут загружены**.
- Они загрузятся **только при вызове `user.getOrders()`**.

---

## 📊 Сравнение: LAZY vs EAGER

| Параметр                | LAZY                                               | EAGER                                      |
|-------------------------|----------------------------------------------------|--------------------------------------------|
| Когда загружается?      | При первом доступе к полю                          | Сразу, при загрузке объекта                |
| Производительность      | Лучше, если данные не нужны                        | Может тянуть ненужные данные               |
| Количество SQL-запросов | Больше (при обращении к полю)                      | Меньше (если данные нужны сразу)           |
| Возможные ошибки        | `LazyInitializationException`, если сессия закрыта | Нет ошибок, но можно грузить слишком много |

---

## 🔄 Как подгрузить связанные сущности вручную?

1. **`JOIN FETCH` в JPQL**:

```java
SELECT u
FROM User
u JOIN
FETCH u.
orders WHERE
u.id =:id
```

2. **EntityGraph**:

```java

@EntityGraph(attributePaths = {"orders"})
User user = em.find(User.class, id, hints);
```

3. **Hibernate.initialize()**:

```java
Hibernate.initialize(user.getOrders());
```

(работает только в открытой сессии)

---

## 📘 По умолчанию:

| Связь         | Тип загрузки по умолчанию |
|---------------|---------------------------|
| `@ManyToOne`  | `EAGER`                   |
| `@OneToOne`   | `EAGER`                   |
| `@OneToMany`  | `LAZY`                    |
| `@ManyToMany` | `LAZY`                    |

---

## ✅ Рекомендации

- Всегда **используй LAZY по умолчанию**, и **подгружай явно**, когда нужно.
- Используй **JOIN FETCH** или **DTO**-проекцию, чтобы избежать N+1.
- Не забывай про `@Transactional`, если используешь LAZY в сервисах.

=========================================================================

## Сравнение EAGER и LAZY

Отличный вопрос! В контексте **Hibernate (и JPA)** термины `eager` и `lazy`
относятся к **типу загрузки (fetching strategy)** связанных сущностей.

---

## 📦 Что такое `EAGER` и `LAZY`?

Они определяют, **когда Hibernate загружает связанные данные** из базы данных:

| Стратегия | Описание                                                       |
|-----------|----------------------------------------------------------------|
| **LAZY**  | Загрузка **по требованию**, только когда реально используется. |
| **EAGER** | Загрузка **сразу**, вместе с родительским объектом.            |

---

## 📘 Пример

Представим, есть сущность `User` и у него есть список `orders`.

```java

@Entity
public class User {
    @OneToMany(mappedBy = "user", fetch = FetchType.LAZY) // или EAGER
    private List<Order> orders;
}
```

---

## 🐢 `LAZY` (ленивая загрузка)

- `orders` не загружается при загрузке `User`.
- Загружается **только при первом доступе к `getOrders()`**.
- Подходит, если тебе **не всегда нужны связанные данные**.

```java
User user = entityManager.find(User.class, 1L); // SELECT * FROM users WHERE id = 1;
List<Order> orders = user.getOrders();         // <-- В этот момент SELECT * FROM orders ...
```

---

## ⚡ `EAGER` (жадная загрузка)

- `orders` загружается **сразу** вместе с `User`.
- Даже если ты **не используешь `orders`**, Hibernate всё равно подгрузит их.

```java
User user = entityManager.find(User.class, 1L);
// Выполнится JOIN-запрос или два отдельных SELECT’а
```

---

## 📊 Сравнение

| Особенность                                               | `LAZY`                             | `EAGER`                         |
|-----------------------------------------------------------|------------------------------------|---------------------------------|
| Производительность                                        | 🔼 Лучше (если не нужна коллекция) | 🔽 Может тянуть ненужные данные |
| Управление запросами                                      | ✅ Гибко                            | ❌ Меньше контроля               |
| Риск `LazyInitializationException`                        | ❌ Есть (если сессия закрыта)       | ✅ Нет                           |
| Используется по умолчанию для `@OneToMany`, `@ManyToMany` | ✅ Да (`LAZY`)                      | ❌ Нет (`EAGER` вручную)         |

---

## ⚠️ Важные нюансы

- Hibernate **по умолчанию делает `LAZY`** для коллекций (`@OneToMany`,
  `@ManyToMany`) и **`EAGER`** для одиночных ссылок (`@ManyToOne`, `@OneToOne`).
- У **`LAZY` загрузки есть риск**: если сессия уже закрыта, доступ к полю
  вызовет `LazyInitializationException`.

---

## 🛠️ Как защититься от `LazyInitializationException`?

- Используй `JOIN FETCH` в HQL/JPQL.
- Используй DTO и `EntityGraph`.
- Загружай данные внутри открытой транзакции (например, в `@Transactional`
  методе).

---

Хочешь пример с
`JOIN FETCH` или как правильно подгрузить LAZY-поле в контроллере?
===============================================================================

> ## Стратегии связывания сущностей

Отличный вопрос! В **Hibernate / JPA** стратегии связывания сущностей
определяются через аннотации отношений между объектами, и отвечают на вопрос: *
*как одна сущность связана с другой?** И как эта связь хранится в базе данных.

---

## 🔗 Основные типы связей между сущностями

| Тип связи     | Описание         | Примеры                     |
|---------------|------------------|-----------------------------|
| `@OneToOne`   | Один к одному    | У пользователя один профиль |
| `@OneToMany`  | Один ко многим   | Один автор — много книг     |
| `@ManyToOne`  | Многие к одному  | Много заказов — один клиент |
| `@ManyToMany` | Многие ко многим | Пользователи и роли         |

---

### Примеры аннотаций

#### 1. **@OneToOne**

```java

@Entity
public class User {
    @OneToOne
    @JoinColumn(name = "profile_id")
    private Profile profile;
}
```

> В базе: `user.profile_id → profile.id`

#### 2. **@OneToMany / @ManyToOne**

```java

@Entity
public class Author {
    @OneToMany(mappedBy = "author")
    private List<Book> books;
}

@Entity
public class Book {
    @ManyToOne
    @JoinColumn(name = "author_id")
    private Author author;
}
```

> В таблице `book` есть колонка `author_id`

---

#### 3. **@ManyToMany**

```java

@Entity
public class Student {
    @ManyToMany
    @JoinTable(
            name = "student_course",
            joinColumns = @JoinColumn(name = "student_id"),
            inverseJoinColumns = @JoinColumn(name = "course_id")
    )
    private List<Course> courses;
}
```

> 🧠 Связь через промежуточную таблицу `student_course(student_id, course_id)`

---

## 🧩 Стратегии каскадирования (`cascade`)

Позволяют управлять действиями над связанными сущностями:

```java
@OneToMany(cascade = CascadeType.ALL)
```

| Cascade Type        | Что делает                   |
|---------------------|------------------------------|
| `ALL`               | Применяет все действия       |
| `PERSIST`           | Сохраняет связанные сущности |
| `MERGE`             | Обновляет их                 |
| `REMOVE`            | Удаляет                      |
| `DETACH`, `REFRESH` | Прочее управление состоянием |

---

## 💾 Дополнительно: `mappedBy` vs `JoinColumn`

- `mappedBy` — на стороне **обратной связи** (не владеющей)
- `@JoinColumn` — **владеющая сторона** (где находится внешний ключ)

---

## 🧠 Советы

- Для `@OneToMany` желательно всегда указывать `mappedBy`, иначе Hibernate может
  создать **лишнюю таблицу связи**.
- При `@ManyToMany` используется **промежуточная таблица**, и `@JoinTable` нужно
  указывать явно.
- Не злоупотребляй `CascadeType.ALL` — это может привести к неожиданным
  удалениям/обновлениям.

=======================================
