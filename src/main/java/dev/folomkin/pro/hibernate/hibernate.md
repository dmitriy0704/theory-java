# HIBERNATE 

## Проблема n+1


**Проблема N+1 запросов** возникает, когда при работе с ORM (например,
Hibernate) в приложении для получения данных из базы вместо одного оптимального
запроса выполняется **множество отдельных запросов**. Это приводит к
существенным затратам времени и ресурсов.

**_Как возникает проблема?_**

Допустим, у нас есть две связанные сущности: **`User`** и **`Order`**, где
`User` имеет список `orders`. Например:

```java

@Entity
public class User {
    @Id
    private Long id;

    private String name;

    @OneToMany(mappedBy = "user", fetch = FetchType.LAZY)
    private List<Order> orders;
}

@Entity
public class Order {
    @Id
    private Long id;

    private String description;

    @ManyToOne
    private User user;
}
```

Теперь мы хотим получить список пользователей с их заказами:

```java
class Demo {
    void demo() {

        List<User> users = entityManager
                .createQuery("SELECT u FROM User u", User.class)
                .getResultList();

        for (User user : users) {
            System.out.println("User: " + user.getName());
            System.out.println("Orders: " + user.getOrders());
        }
    }
}

```

### Что происходит под капотом?

1. Первый запрос:
   ```sql
   SELECT * FROM user;
   ```
   Загружаются все пользователи.

2. Для каждого пользователя (N пользователей) выполняется отдельный запрос для
   получения его заказов:
   ```sql
   SELECT * FROM order WHERE user_id = ?;
   ```

Итого:

- **1 запрос для загрузки пользователей.**
- **N запросов для загрузки заказов.**
- Получается `N+1` запросов.

### Решение проблемы N+1

#### 1. **Использование `JOIN FETCH` (жадная загрузка)**

Перепишем запрос, чтобы сразу загрузить заказы:

```java
List<User> users = entityManager
        .createQuery("SELECT u FROM User u JOIN FETCH u.orders", User.class)
        .getResultList();
```

Теперь Hibernate выполнит **один запрос** с `JOIN`:

```sql
SELECT u.*, o.*
FROM user u
         JOIN order o ON u.id = o.user_id;
```

> **Важно:** Это решает проблему, но может вернуть дублирующиеся строки, если у
> одного пользователя несколько заказов. Hibernate автоматически устраняет
> дубликаты, но это тоже имеет накладные расходы.

#### 2. **Использование графов сущностей (Entity Graphs)**

С помощью **графов сущностей** можно явно указать, какие данные подгружать:

```java

@EntityGraph(attributePaths = "orders")
List<User> users = entityManager
        .createQuery("SELECT u FROM User u", User.class)
        .setHint("javax.persistence.fetchgraph", entityGraph)
        .getResultList();
```

Это также позволяет избежать проблемы N+1, загружая связанные сущности в одном
запросе.

---

### 3. **Использование `@BatchSize`**

Hibernate может группировать загрузку данных с помощью аннотации `@BatchSize`:

```java

@Entity
@BatchSize(size = 10)
public class User {
    @Id
    private Long id;

    @OneToMany(mappedBy = "user", fetch = FetchType.LAZY)
    private List<Order> orders;
}
```

Теперь вместо одного запроса на каждый объект, Hibernate будет подгружать данные
пачками:

```sql
SELECT *
FROM order
WHERE user_id IN (?, ?, ?, ?, ?, ?, ?, ?, ?, ?);
```

---

### 4. **Кэширование (`Second-Level Cache`)**

В некоторых случаях проблему можно частично решить с помощью **вторичного кэша
Hibernate**. Он позволяет повторно использовать данные, которые уже были
загружены, чтобы избежать лишних запросов.

---

### Резюме

| Метод         | Преимущества                            | Недостатки                              |
|---------------|-----------------------------------------|-----------------------------------------|
| `JOIN FETCH`  | Быстро, минимальный оверхед             | Риск дубликатов, сложные запросы        |
| Entity Graphs | Гибкий, можно настроить разные сценарии | Требует ручной настройки                |
| `@BatchSize`  | Уменьшает число запросов                | Подходит не всегда, требует настройки   |
| Кэширование   | Избегает повторных запросов             | Не всегда применимо, сложно настраивать |

Если нужно, могу показать более сложный пример, как комбинировать эти подходы
или использовать их с JPA Criteria API. 😊

### ENTITYMANAGER

Отличный вопрос! Давай разберёмся, **что такое `EntityManager`** в контексте
JPA (Java Persistence API) и Hibernate.

---

## 🔹 Что такое `EntityManager`?

`EntityManager` — это **основной интерфейс JPA**, через который осуществляется *
*вся работа с базой данных**:  
создание, чтение, обновление, удаление объектов (CRUD), а также управление
жизненным циклом сущностей.

---

## 🧩 Проще говоря:

Это как "менеджер" между твоим Java-приложением и базой данных. Он отвечает за:

- сохранение объекта в базу
- получение объекта из базы
- обновление и удаление
- управление транзакциями
- кеширование сущностей
- создание запросов (JPQL, SQL, Criteria API)

---

## 🧪 Пример использования `EntityManager`

```java

@PersistenceContext
private EntityManager entityManager;

public User getUserById(Long id) {
    return entityManager.find(User.class, id);
}

public void saveUser(User user) {
    entityManager.persist(user);
}
```

---

## 🔧 Основные методы `EntityManager`

| Метод                    | Что делает                                   |
|--------------------------|----------------------------------------------|
| `persist(Object entity)` | Сохраняет объект в базу данных               |
| `find(Class<T>, id)`     | Находит сущность по id                       |
| `merge(Object entity)`   | Обновляет объект в базе                      |
| `remove(Object entity)`  | Удаляет объект                               |
| `createQuery()`          | Создаёт JPQL-запрос                          |
| `createNativeQuery()`    | SQL-запрос                                   |
| `flush()`                | Принудительно синхронизирует изменения с БД  |
| `clear()`                | Очищает контекст (все отслеживаемые объекты) |

---

## 🔄 Жизненный цикл сущностей

`EntityManager` управляет состоянием объектов:

| Состояние   | Описание                                                                   |
|-------------|----------------------------------------------------------------------------|
| `Transient` | Объект создан, но не привязан к БД                                         |
| `Managed`   | Контролируется `EntityManager`, любые изменения автоматически попадут в БД |
| `Detached`  | Больше не контролируется, но может быть сохранён снова через `merge()`     |
| `Removed`   | Помечен на удаление                                                        |

---

## 📦 Откуда берётся `EntityManager`?

В Spring или Jakarta EE его можно внедрить через:

```java

@PersistenceContext
private EntityManager em;
```

А в обычных Java SE-приложениях — создать вручную:

```java
EntityManagerFactory emf = Persistence.createEntityManagerFactory("myUnit");
EntityManager em = emf.createEntityManager();
```

---

## ☑️ Резюме

`EntityManager` = главный API для работы с базой в JPA.  
Он:

- управляет сущностями
- делает запросы к базе
- отвечает за транзакции
- предоставляет гибкий доступ к данным

Хочешь, могу показать сравнение `EntityManager` и `Spring Data JPA`, или как
сделать транзакции вручную.
========




### Отлично! Давай сравним **`EntityManager` и `Spring Data JPA`

**, а также посмотрим, **как управлять транзакциями вручную**.

---

## 🔄 1. EntityManager vs Spring Data JPA

| Характеристика        | `EntityManager`                         | `Spring Data JPA`                             |
|-----------------------|-----------------------------------------|-----------------------------------------------|
| ✍️ Код                | Нужно писать вручную все запросы и CRUD | CRUD генерируется автоматически               |
| ⚙️ Настройка          | Стандартный JPA API                     | Абстракция над JPA, интеграция с Spring       |
| 📦 DAO (репозитории)  | Самописные классы                       | Интерфейсы `JpaRepository`, `CrudRepository`  |
| 📜 Запросы            | JPQL / SQL / Criteria API               | Метод-имена, JPQL, @Query                     |
| 📈 Уровень абстракции | Низкий (гибкость, но больше кода)       | Высокий (меньше кода, но ограничения)         |
| 💥 Ошибки             | На уровне выполнения (Runtime)          | Могут быть на этапе старта (проверка методов) |
| 🧪 Тестирование       | Чуть сложнее                            | Просто — можно мокать репозитории             |

---

### 🔸 Пример: `EntityManager`

```java
public class UserRepository {
    @PersistenceContext
    private EntityManager em;

    public User findById(Long id) {
        return em.find(User.class, id);
    }

    public void save(User user) {
        em.persist(user);
    }
}
```

---

### 🔹 Пример: `Spring Data JPA`

```java
public interface UserRepository extends JpaRepository<User, Long> {
    List<User> findByName(String name);
}
```

> Не нужно писать реализацию — Spring сам всё сгенерирует.

---

## ⚙️ 2. Управление транзакциями вручную

Обычно Spring сам управляет транзакциями с помощью аннотации:

```java

@Transactional
public void doSomething() {
    // всё внутри — одна транзакция
}
```

---

### Но ты можешь делать это **вручную** через

`EntityTransaction` (если без Spring):

```java
EntityManager em = ...;
EntityTransaction tx = em.getTransaction();

try{
        tx.

begin();

User user = new User();
    em.

persist(user);

    tx.

commit();
}catch(
Exception e){
        if(tx.

isActive()){
        tx.

rollback();
    }
            }
```

---

## 📌 Когда использовать `EntityManager`, а когда `Spring Data JPA`?

| Когда использовать                                             | Что выбрать       |
|----------------------------------------------------------------|-------------------|
| Нужен полный контроль (join fetch, батчи, хинты, нативные SQL) | `EntityManager`   |
| Типовой CRUD + быстрый старт                                   | `Spring Data JPA` |
| Очень сложные запросы                                          | Комбинировать оба |

---

Хочешь, я покажу пример, как в одном репозитории использовать и `Spring Data`, и
`EntityManager` вместе? Это довольно удобно!

Супер! Тогда давай посмотрим, как **объединить `Spring Data JPA`
и `EntityManager` в одном репозитории**, чтобы получить лучшее из двух миров:

- ✅ Удобный `JpaRepository` для базового CRUD
- ✅ Гибкий `EntityManager` для кастомных запросов и оптимизаций

---

## 🔧 Шаг 1: Создай интерфейс репозитория

```java
public interface UserRepository extends JpaRepository<User, Long>, CustomUserRepository {
    List<User> findByName(String name);
}
```

Здесь:

- `JpaRepository<User, Long>` — стандартные методы CRUD
- `CustomUserRepository` — дополнительный интерфейс с кастомными методами (
  которые ты реализуешь вручную)

---

## 🛠️ Шаг 2: Создай интерфейс для кастомной логики

```java
public interface CustomUserRepository {
    List<User> findUsersWithOrders(); // свой метод
}
```

---

## 🔨 Шаг 3: Реализуй интерфейс с использованием `EntityManager`

Класс должен называться: `ИмяРепозитория + Impl`

```java

@Repository
public class CustomUserRepositoryImpl implements CustomUserRepository {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public List<User> findUsersWithOrders() {
        return entityManager.createQuery(
                        "SELECT DISTINCT u FROM User u JOIN FETCH u.orders", User.class)
                .getResultList();
    }
}
```

> Здесь мы использовали `JOIN FETCH`, чтобы избежать проблемы N+1 и сразу
> загрузить заказы пользователя.

---

## 📦 Теперь в `UserRepository` доступно всё:

```java

@Service
public class UserService {
    @Autowired
    private UserRepository userRepository;

    public void test() {
        // стандартный метод Spring Data
        List<User> named = userRepository.findByName("Ivan");

        // кастомный метод через EntityManager
        List<User> usersWithOrders = userRepository.findUsersWithOrders();
    }
}
```

---

## 🧠 Это очень удобно, когда:

- Нужно использовать **стандартные возможности Spring Data**
- Но при этом нужно внедрить **сложные запросы**, которые проще или быстрее
  написать на `JPQL` или `SQL`

---

Если хочешь, могу ещё показать:

- как использовать `@Query` с нативным SQL
- как внедрить `Criteria API` для динамических фильтров
- или как делать пагинацию и сортировку с кастомными запросами

или как делать пагинацию и сортировку с кастомными запросами
Отлично, давай разберёмся, **как делать пагинацию и сортировку с кастомными JPQL
или SQL-запросами** — с использованием `EntityManager` и при этом не терять
совместимость со `Spring Data`.

---

## 📘 Что такое пагинация и сортировка в Spring Data JPA?

Spring предоставляет удобные классы:

- `Pageable` — объект, содержащий номер страницы, размер и сортировку
- `Page<T>` — результат: контент, номер страницы, общее количество и т.д.

---

## ✅ Цель:

Сделать **кастомный запрос** с **пагинацией и сортировкой**, используя
`EntityManager`.

---

### 🔹 Пример задачи:

Получить список пользователей с заказами, но:

- только часть пользователей (по страницам)
- отсортировать их по имени или дате создания

---

## 🛠️ Шаг 1: Создаём кастомный интерфейс

```java
public interface CustomUserRepository {
    Page<User> findUsersWithOrders(Pageable pageable);
}
```

---

## 🛠️ Шаг 2: Реализуем с `EntityManager`

```java

@Repository
public class CustomUserRepositoryImpl implements CustomUserRepository {

    @PersistenceContext
    private EntityManager em;

    @Override
    public Page<User> findUsersWithOrders(Pageable pageable) {
        // 1. Основной запрос
        String jpql = "SELECT DISTINCT u FROM User u LEFT JOIN FETCH u.orders";

        // 2. Запрос без fetch для подсчёта total (fetch нельзя использовать в count)
        String countJpql = "SELECT COUNT(DISTINCT u) FROM User u";

        // 3. Добавляем сортировку
        if (pageable.getSort().isSorted()) {
            String sortClause = pageable.getSort().stream()
                    .map(order -> "u." + order.getProperty() + " " + order.getDirection())
                    .collect(Collectors.joining(", "));
            jpql += " ORDER BY " + sortClause;
        }

        // 4. Выполняем запрос с пагинацией
        List<User> users = em.createQuery(jpql, User.class)
                .setFirstResult((int) pageable.getOffset())
                .setMaxResults(pageable.getPageSize())
                .getResultList();

        // 5. Подсчёт общего количества
        Long total = em.createQuery(countJpql, Long.class)
                .getSingleResult();

        return new PageImpl<>(users, pageable, total);
    }
}
```

---

## 🚀 Как вызывать этот метод

```java
Pageable pageable = PageRequest.of(0, 5, Sort.by("name").ascending());
Page<User> result = userRepository.findUsersWithOrders(pageable);

result.

getContent().

forEach(System.out::println);
System.out.

println("Всего страниц: "+result.getTotalPages());
```

---

## 🧠 Резюме

| Этап               | Что происходит                         |
|--------------------|----------------------------------------|
| `setFirstResult()` | Устанавливает смещение (`OFFSET`)      |
| `setMaxResults()`  | Устанавливает ограничение (`LIMIT`)    |
| `ORDER BY`         | Добавляется из `Pageable.getSort()`    |
| `count(*)`         | Выполняется отдельно, без `fetch join` |
| `PageImpl<T>`      | Собирает всё в одну структуру          |

---

## 💡 Можно ещё:

- Добавить **фильтры** по параметрам (`WHERE u.name LIKE :name`)
- Сделать пагинацию с **нативным SQL** (`createNativeQuery()`)
- Использовать **Criteria API** для динамической генерации условий

## EntityGraph

Крутой вопрос — **`Entity Graph`** в Hibernate (и JPA в целом) — это инструмент,
который помогает **гибко управлять стратегиями загрузки данных**: что подгружать
сразу (жадно), а что позже (лениво), **без изменения аннотаций в сущностях**.

---

## 📌 Что такое Entity Graph?

`Entity Graph` — это способ **явно указать, какие поля сущности должны быть
загружены сразу**, **даже если они помечены как `LAZY`**.  
Это очень удобно для **оптимизации запросов** и **избежания проблемы N+1**.

> То есть: ты говоришь Hibernate — "вот эту часть мне подгрузи сразу, а
> остальное не трогай".

---

## 🤓 Почему это удобно?

Представь, у тебя есть сущность `User` с лениво загружаемыми заказами (
`orders`):

```java

@Entity
public class User {
    @Id
    private Long id;

    private String name;

    @OneToMany(mappedBy = "user", fetch = FetchType.LAZY)
    private List<Order> orders;
}
```

Если ты просто загрузишь пользователей:

```java
List<User> users = em.createQuery("SELECT u FROM User u", User.class).getResultList();
```

А потом обратишься к `user.getOrders()` — получишь **N+1 проблему**.

---

## 🧩 Как помогает Entity Graph?

Ты создаёшь граф:

```java
EntityGraph<User> graph = em.createEntityGraph(User.class);
graph.addAttributeNodes("orders");
```

И передаёшь его в запрос:

```java
List<User> users = em.createQuery("SELECT u FROM User u", User.class)
        .setHint("javax.persistence.fetchgraph", graph)
        .getResultList();
```

👉 Теперь Hibernate **выполнит `JOIN FETCH` автоматически**, и `orders`
загрузятся сразу, **без лишних запросов**.

---

## 🛠️ Пример с аннотациями

Ты можешь описать `Entity Graph` прямо в сущности:

```java

@NamedEntityGraph(
        name = "User.withOrders",
        attributeNodes = @NamedAttributeNode("orders")
)
@Entity
public class User {
    ...
}
```

И использовать его в коде:

```java
EntityGraph<?> graph = em.getEntityGraph("User.withOrders");

List<User> users = em.createQuery("SELECT u FROM User u", User.class)
        .setHint("javax.persistence.fetchgraph", graph)
        .getResultList();
```

---

## 🔍 fetchgraph vs loadgraph

| Тип          | Поведение                                                                |
|--------------|--------------------------------------------------------------------------|
| `fetchgraph` | Загружает **только** то, что указано в графе (остальное остаётся LAZY)   |
| `loadgraph`  | Загружает то, что указано в графе **+ всё, что уже EAGER по аннотациям** |

---

## 📦 Где это полезно?

- Замена `JOIN FETCH`, но **более гибко и декларативно**
- Используется в **Spring Data JPA** (`@EntityGraph` на методах)
- Оптимизация **REST API**, где не всегда нужны все связи
- Когда сущности большие и жадная загрузка по умолчанию — это плохо

---

## ✅ Пример в Spring Data JPA

```java

@EntityGraph(attributePaths = {"orders"})
@Query("SELECT u FROM User u")
List<User> findAllWithOrders();
```

> Это тоже будет использовать `JOIN FETCH` под капотом.


==================================================================
==================================================================

Проблема N+1 в Hibernate (и в других ORM, таких как JPA) — это распространенная
проблема производительности, которая возникает при выполнении запросов к базе
данных. Она связана с тем, как ORM загружает связанные сущности.

## ЧТО ТАКОЕ ПРОБЛЕМА N+1?

Проблема N+1 возникает, когда для загрузки коллекции связанных сущностей
выполняется один запрос для основной сущности и затем отдельный запрос для
каждой из связанных сущностей. Это может привести к значительному увеличению
количества запросов к базе данных, что негативно сказывается на
производительности приложения.

### Пример

Предположим, у вас есть две сущности: `Author` и `Book`, где один автор может
иметь много книг. Если вы хотите получить список всех авторов и их книг, вы
можете написать следующий код:

```java
class Demo {
    public void demo() {
        List<Author> authors = session
                .createQuery("FROM Author", Author.class)
                .getResultList();
        for (Author author : authors) {
            System.out.println(author.getName());
            for (Book book : author.getBooks()) {
                System.out.println(book.getTitle());
            }
        }
    }
}

```

В этом примере происходит следующее:

1. Выполняется один запрос для получения всех авторов (это 1 запрос).
2. Для каждого автора выполняется отдельный запрос для получения его книг (это N
   запросов, где N — количество авторов).

Таким образом, общее количество запросов к базе данных составляет 1 + N, что и
приводит к проблеме N+1.

## ПОЧЕМУ ЭТО ПРОБЛЕМА?

Проблема N+1 может значительно ухудшить производительность приложения по
следующим причинам:

- **Увеличение времени выполнения**: Каждый дополнительный запрос требует
  времени на выполнение и обработку.
- **Нагрузка на базу данных**: Большое количество запросов может привести к
  увеличению нагрузки на базу данных и ухудшению ее производительности.
- **Сложность отладки**: Увеличение количества запросов может затруднить отладку
  и анализ производительности приложения.

## ПРИЧИНЫ ВОЗНИКНОВЕНИЯ

1. **Ленивая загрузка (Lazy Loading) по умолчанию**  
   По умолчанию многие ассоциации в Hibernate (например, `@OneToMany`,
   `@ManyToOne`) настроены на ленивую загрузку (`FetchType.LAZY`). Это значит,
   что связанные объекты не загружаются сразу вместе с основным объектом, а
   подгружаются при первом обращении к ним. Если в коде происходит итерация по
   коллекции связанных объектов, то для каждого из них Hibernate выполнит
   отдельный SQL-запрос.

2. **Отсутствие явного указания жадной загрузки (Eager Fetching)**  
   Если не использовать `FetchType.EAGER` или не применять `JOIN FETCH` в
   JPQL/HQL-запросах, Hibernate не будет загружать связанные сущности одним
   запросом с использованием JOIN.

3. **Использование коллекций с ленивой загрузкой без оптимизации**  
   При работе с коллекциями (`List`, `Set`) связанных сущностей без применения
   специальных техник (например, batch fetching или fetch join) каждый элемент
   коллекции может быть загружен отдельным запросом.

4. **Отсутствие batch fetching**  
   Batch fetching — это механизм Hibernate, который позволяет загружать
   несколько связанных сущностей одним запросом с помощью IN-подобных
   конструкций. Если он не настроен или не используется, то для каждой связанной
   сущности будет отдельный запрос.

5. **Неправильное использование ORM-запросов**  
   Например, если в цикле выполняется обращение к связанным объектам без
   предварительной выборки через JOIN FETCH, то каждый вызов приведёт к
   отдельному SQL-запросу.

### Кратко:

| Причина                          | Описание                                                                                 |
|----------------------------------|------------------------------------------------------------------------------------------|
| Ленивый fetch (`FetchType.LAZY`) | Связанные объекты загружаются по требованию, что приводит к множеству отдельных запросов |
| Отсутствие `JOIN FETCH`          | Запросы без join fetch не объединяют выборку связанных сущностей                         |
| Отсутствие batch fetching        | Нет групповой подгрузки связанных объектов                                               |
| Итерация по ленивым коллекциям   | При обходе коллекций происходит множество отдельных запросов                             |

## Как исправить проблемы N+1?

Проблему **N+1** в Hibernate можно исправить несколькими способами, в
зависимости от конкретного сценария и требований к производительности. Ниже
перечислены основные подходы с примерами.

1. Использовать `JOIN FETCH` в JPQL/HQL-запросах

Это самый распространённый способ загрузить связанные сущности одним запросом с
помощью SQL JOIN.

**Пример:**

```java

class Demo() {
    void demo() {
        // Без JOIN FETCH — возникнет N+1
        List<Author> authors = entityManager.createQuery("SELECT a FROM Author a", Author.class).getResultList();

        for (
                Author author : authors) {
            // При обращении к author.getBooks() будет отдельный запрос для каждой книги
            System.out.

                    println(author.getBooks().

                            size());
        }

// С JOIN FETCH — все книги загружаются вместе с авторами одним запросом
        List<Author> authors = entityManager.createQuery(
                "SELECT a FROM Author a JOIN FETCH a.books", Author.class).getResultList();
    }
}

```

2. Настроить batch fetching (пакетную загрузку)

Batch fetching позволяет Hibernate загружать связанные сущности пакетами, а не
по одной.

**Настройка в `hibernate.cfg.xml` или `application.properties`:**

```properties
hibernate.default_batch_fetch_size=16
```

**Аннотация на коллекции или связях:**

```java

@OneToMany(fetch = FetchType.LAZY)
@BatchSize(size = 16)
private Set<Book> books;
```

Hibernate при обращении к коллекции загрузит сразу 16 связанных объектов одним
запросом с `IN (...)`.

3. Использовать жадную загрузку (`FetchType.EAGER`)

Можно указать, что связанные сущности должны загружаться сразу вместе с основной
сущностью.

```java

@OneToMany(fetch = FetchType.EAGER)
private Set<Book> books;
```

**Однако:**  
Жадная загрузка может привести к избыточной выборке данных и ухудшению
производительности, если связи большие или не всегда нужны.

4. Использовать Entity Graphs (JPA 2.1+)

Entity Graph позволяет динамически указывать, какие связи нужно подгружать.

```java
EntityGraph<Author> graph = entityManager.createEntityGraph(Author.class);
graph.

addAttributeNodes("books");

Map<String, Object> props = new HashMap<>();
props.

put("javax.persistence.fetchgraph",graph);

Author author = entityManager.find(Author.class, authorId, props);
```

5. Оптимизировать структуру запросов и логику приложения

- Избегать обращения к ленивым коллекциям в циклах без предварительной выборки.
- Загружать данные пакетами.
- Использовать DTO и проекции для выборки только нужных данных.

Итог

| Способ                    | Когда использовать                                  | Преимущества                  | Недостатки                          |
|---------------------------|-----------------------------------------------------|-------------------------------|-------------------------------------|
| `JOIN FETCH`              | При необходимости загрузить связанные объекты сразу | Один запрос вместо N+1        | Может привести к дублированию строк |
| Batch fetching            | При работе с большими коллекциями                   | Уменьшает количество запросов | Требует настройки и понимания       |
| Жадная загрузка (`EAGER`) | Когда всегда нужны связанные данные                 | Простота использования        | Может грузить лишние данные         |
| Entity Graphs             | Для динамического управления загрузкой              | Гибкость                      | Сложнее в настройке                 |

> HIBERNATE. ENTITY GRAPHS

Entity Graphs (графы сущностей) — это механизм в JPA (Java Persistence API),
который позволяет разработчикам управлять загрузкой связанных сущностей более
гибко и эффективно. С помощью графов сущностей можно указать, какие связанные
объекты должны быть загружены вместе с основной сущностью, что помогает избежать
проблемы N+1 и оптимизировать производительность запросов.

### Основные характеристики Entity Graphs:

1. **Определение графа:** Граф сущностей определяет, какие атрибуты (сущности)
   должны быть загружены при выполнении запроса. Это позволяет вам
   контролировать стратегию выборки данных.

2. **Динамическое создание:** Графы могут быть созданы динамически в коде или
   определены статически с помощью аннотаций.

3. **Поддержка различных стратегий загрузки:** Вы можете использовать графы для
   указания, какие связанные сущности должны быть загружены с использованием
   `FetchType.EAGER` или `FetchType.LAZY`, в зависимости от ваших потребностей.

## ПРИМЕР

Есть две сущности: `Author` и `Book`. У автора может быть много книг.

```java

@Entity
public class Author {
    @Id
    private Long id;

    private String name;

    @OneToMany(mappedBy = "author", fetch = FetchType.LAZY)
    private List<Book> books;

    // геттеры и сеттеры
}

@Entity
public class Book {
    @Id
    private Long id;

    private String title;

    @ManyToOne(fetch = FetchType.LAZY)
    private Author author;

    // геттеры и сеттеры
}
```

### Проблема N+1

Если вы просто сделаете:

```java
class Demo {
    void demo() {
        List<Author> authors = entityManager
                .createQuery("SELECT a FROM Author a", Author.class)
                .getResultList();
        for (Author author : authors) {
            System.out.println(
                    author.getBooks()
                            .size()
            );  // тут будет отдельный запрос на каждую коллекцию books!
        }
    }
}

```

Hibernate выполнит 1 запрос на авторов + N запросов на книги каждого автора —
это и есть проблема N+1.

### Решение с Entity Graph

Создадим Entity Graph, который укажет JPA подгрузить книги вместе с авторами:

```java
class Demo {
    void demo() {
        EntityGraph<Author> graph = entityManager.createEntityGraph(Author.class);
        graph.addAttributeNodes("books");
    }
}
```

Теперь используем этот граф при выполнении запроса:

```java
class Demo {
    void demo() {
        List<Author> authors = entityManager
                .createQuery("SELECT a FROM Author a", Author.class)
                .setHint("javax.persistence.fetchgraph", graph)
                .getResultList();
        for (Author author : authors) {
            System.out
                    .println(author.getBooks()
                            .size());  // книги уже загружены одним запросом!
        }
    }
}
```

### Что происходит?

- JPA подставляет в SQL `JOIN` для связи `books`.
- Все данные загружаются одним запросом.
- При обращении к `author.getBooks()` дополнительных запросов не будет.


1. Определение графа с помощью аннотаций

Вы можете определить граф сущностей с помощью аннотации `@EntityGraph` на уровне
класса или метода репозитория:

```java

@Entity
@NamedEntityGraph(name = "Author.books",
        attributePaths = {"books"})
public class Author {
    @Id
    private Long id;

    @OneToMany(mappedBy = "author")
    private Set<Book> books;
}
```

2. Использование графа в запросе

Затем вы можете использовать этот граф при выполнении запроса:

```java
class Demo {
    void demo() {
        EntityGraph entityGraph = entityManager.getEntityGraph("Author.books");
        Map<String, Object> properties = new HashMap<>();
        properties.put("javax.persistence.fetchgraph", entityGraph);
        Author author = entityManager.find(Author.class, authorId, properties);
    }
}
```

3. Динамическое создание графа

Вы также можете создавать графы динамически в коде:

```java

class Demo {
    void demo() {
        EntityGraph graph = entityManager.createEntityGraph(Author.class);
        graph.addAttributeNodes("books");
        Map<String, Object> hints = new HashMap<>();
        hints.put("javax.persistence.fetchgraph", graph);
        List<Author> authors = entityManager
                .createQuery("SELECT a FROM Author a", Author.class)
                .setHint("javax.persistence.fetchgraph", graph)
                .getResultList();
    }
}
```

### Преимущества использования Entity Graphs

- **Гибкость:** Позволяют динамически управлять тем, какие данные загружаются.
- **Улучшение производительности:** Помогают избежать проблемы N+1 и избыточных
  запросов к базе данных.
- **Читаемость кода:** Упрощают понимание того, какие данные будут загружены при
  выполнении запроса.

Использование Entity Graphs — это мощный инструмент для оптимизации работы с JPA
и управления загрузкой связанных данных в приложениях на Java.



> ## Жадная загрузка

**Жадная загрузка (Eager Loading)** — это стратегия загрузки связанных данных,
при которой связанные сущности загружаются **сразу вместе с основной сущностью**
в момент выполнения запроса к базе данных.

### Что это значит?

Если у вас есть, например, сущность `Author` и связанная с ней коллекция
`books`, то при жадной загрузке:

- Когда вы загружаете автора из базы, сразу же подгружаются и все его книги.
- Это обычно реализуется через SQL JOIN или дополнительные запросы, которые
  выполняются сразу.

### Пример

```java

@Entity
public class Author {
    @OneToMany(fetch = FetchType.EAGER)
    private List<Book> books;
}
```

При таком объявлении, когда вы получите автора из базы, Hibernate сразу же
загрузит и все связанные книги.

### Плюсы жадной загрузки

- Избегает проблему N+1 запросов, если вам точно нужны связанные данные.
- Удобно, когда вы всегда используете связанные объекты вместе с основной
  сущностью.

### Минусы жадной загрузки

- Может привести к избыточной нагрузке на базу и сети, если связанные данные не
  нужны.
- При большом количестве связанных объектов может сильно замедлить запрос.

### В противоположность — **ленивая загрузка (Lazy Loading)**

- Связанные данные загружаются только при первом обращении к ним.
- Позволяет экономить ресурсы, но может вызвать проблему N+1 запросов.

### Итог

**Жадная загрузка** — это когда связанные объекты подгружаются сразу вместе с
основной сущностью, чтобы избежать дополнительных запросов позже. Это удобно для
часто используемых связей, но может быть неэффективно при больших объемах
данных.


> ## Criteria api

**Criteria API** — это часть спецификации JPA (Java Persistence API), которая
предоставляет типобезопасный, объектно-ориентированный способ построения
динамических запросов к базе данных.

### Основные особенности Criteria API:

- **Типобезопасность**  
  Запросы строятся с помощью Java-кода, а не строк JPQL, что позволяет избежать
  ошибок в синтаксисе и типах на этапе компиляции.

- **Динамическое построение запросов**  
  Удобно создавать запросы с условиями, которые зависят от логики приложения (
  например, если параметры фильтрации могут меняться).

- **Объектно-ориентированный подход**  
  Запросы строятся через объекты `CriteriaBuilder`, `CriteriaQuery`, `Root` и
  т.д., что облегчает чтение и поддержку кода.

### Пример использования Criteria API

```java
CriteriaBuilder cb = entityManager.getCriteriaBuilder();
CriteriaQuery<Author> cq = cb.createQuery(Author.class);
Root<Author> author = cq.from(Author.class);

cq.

select(author)
  .

where(cb.like(author.get("name"), "Иван%"));

List<Author> authors = entityManager.createQuery(cq).getResultList();
```

### Когда использовать Criteria API?

- Когда нужно строить сложные или динамические запросы.
- Когда важна типобезопасность и удобство поддержки кода.
- В случаях, когда использование строковых JPQL-запросов неудобно или
  рискованно.

### Кратко

**Criteria API** — это инструмент для программного построения запросов к базе
данных в JPA, который помогает создавать гибкие, безопасные и удобочитаемые
запросы без использования строковых выражений.

### Пример кода с Criteria API

Допустим, у нас есть сущность `Author` с полями `id`, `name` и связью `books` (
список книг). Мы хотим получить всех авторов с именем, начинающимся на "Иван".

```java
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import javax.persistence.EntityManager;
import java.util.List;

public List<Author> findAuthorsByNamePrefix(EntityManager entityManager, String prefix) {
    // Получаем объект CriteriaBuilder из EntityManager
    CriteriaBuilder cb = entityManager.getCriteriaBuilder();

    // Создаем запрос для сущности Author
    CriteriaQuery<Author> cq = cb.createQuery(Author.class);

    // Определяем корень запроса (FROM Author a)
    Root<Author> authorRoot = cq.from(Author.class);

    // Добавляем условие WHERE a.name LIKE 'prefix%'
    cq.select(authorRoot)
            .where(cb.like(authorRoot.get("name"), prefix + "%"));

    // Выполняем запрос и получаем результат
    List<Author> authors = entityManager.createQuery(cq).getResultList();

    return authors;
}
```

### Объяснение:

- `CriteriaBuilder` — фабрика для создания частей запроса.
- `CriteriaQuery<T>` — сам запрос, где `T` — тип результата.
- `Root<T>` — корень запроса, указывает на основную сущность.
- Метод `cb.like()` создаёт условие LIKE.
- В итоге мы получаем список авторов, чьё имя начинается с заданного префикса.

### Как использовать вместе с Entity Graph?

Можно комбинировать Criteria API и Entity Graph:

```java
EntityGraph<Author> graph = entityManager.createEntityGraph(Author.class);
graph.

addAttributeNodes("books");

List<Author> authors = entityManager.createQuery(cq)
        .setHint("javax.persistence.fetchgraph", graph)
        .getResultList();
```

Так вы получите авторов с подгруженными книгами в одном запросе.


> ## EntityManager

**EntityManager** — это основной интерфейс в JPA (Java Persistence API), который
отвечает за взаимодействие с базой данных и управляет жизненным циклом
сущностей.

### Основные функции EntityManager:

- **Создание, чтение, обновление и удаление (CRUD) сущностей**  
  Например, сохранение нового объекта в базу или получение объекта по его
  идентификатору.

- **Управление состоянием сущностей**  
  Сущности могут быть в разных состояниях: новые, управляемые (attached),
  отсоединённые (detached), удалённые. EntityManager следит за этими
  состояниями.

- **Выполнение запросов к базе данных**  
  Через JPQL (Java Persistence Query Language) или Criteria API.

- **Транзакционное управление**  
  Обычно EntityManager работает внутри транзакций, обеспечивая атомарность
  операций.

### Пример использования EntityManager:

```java

class Demo {
    void demo() {
        // Получение сущности по ID
        Author author = entityManager.find(Author.class, 1L);

        // Создание новой сущности
        Author newAuthor = new Author();
        newAuthor.setName("Иван Иванов").entityManager.persist(newAuthor);

        // Обновление сущности
        author.setName("Пётр Петров");
        entityManager.merge(author);

        // Удаление сущности
        entityManager.remove(author);
    }
}
```

### Где берётся EntityManager?

- В Java EE / Jakarta EE контейнере его обычно внедряют через
  `@PersistenceContext`.
- В Spring Boot можно получить через `@Autowired` или использовать
  `EntityManagerFactory`.

### Кратко

EntityManager — это объект, который позволяет работать с базой данных на уровне
объектов (сущностей), управлять их состояниями и выполнять запросы. Это основной
инструмент для работы с JPA.

=================================================================
-----------------------------------------------------------------

# Уровни кеширования в Hibernate




Кэширование — важная тема в Hibernate, и понимание **уровней
кеширования** помогает **улучшать производительность** приложений и **снижать
нагрузку на базу данных**.

---

## 🧠 В Hibernate есть **два уровня кеша**:

### ✅ 1. Первый уровень кеша (1st level cache)

### ✅ 2. Второй уровень кеша (2nd level cache)

---

## 🔹 Первый уровень кэша (Session Cache)

### 📌 Что это?

- Это **встроенный кеш**, работающий **в пределах одной сессии (`Session`) или
  транзакции**.
- **Всегда включён** — ничего дополнительно настраивать не нужно.
- Кэширует сущности, загруженные в текущей `Session`.

### ⚙️ Пример:

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

S
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

Кэширование запросов в Hibernate — это важная тема для оптимизации производительности при работе с базой данных. Оно позволяет **уменьшить количество обращений к базе данных**, сохраняя результаты запросов в памяти.

### 🔒 Кэширование в Hibernate

Hibernate поддерживает **несколько уровней кэширования**, и кэширование запросов — это один из методов повышения производительности.

---

## 🧠 Основные уровни кэширования в Hibernate:

1. **Первичный кэш (Session Cache)** — кэш на уровне сессии.
2. **Вторичный кэш (Second-level Cache)** — кэш на уровне сессии-фабрики.
3. **Кэш запросов (Query Cache)** — кэширование результатов запросов.

---

## 🔥 Кэш запросов (Query Cache)

### 📌 Что это?

**Кэш запросов** — это **кэширование результатов выполнения запросов**. Это может быть полезно, если одни и те же запросы выполняются многократно с одинаковыми параметрами.

### ✅ Преимущества кэширования запросов:

- **Уменьшение нагрузки на БД**: запросы, которые часто выполняются с одинаковыми параметрами, не будут отправляться в базу данных.
- **Ускорение выполнения**: результаты запросов можно будет извлекать непосредственно из кэша, что намного быстрее, чем делать запрос в базу.

---

## 🛠 Как включить кэш запросов в Hibernate?

1. Включи **вторичный кэш**.
2. Включи **кэш запросов**.

### 1. Включение второго уровня кэширования

Для того чтобы работать с кэшированием запросов, нужно включить **вторичный кэш**. Это делается через конфигурацию Hibernate.

Пример конфигурации `hibernate.cfg.xml` для включения второго уровня кэширования:

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

Чтобы включить кэширование запросов, нужно установить свойство `hibernate.cache.use_query_cache` в `true` и использовать кэш второго уровня.

```xml
<property name="hibernate.cache.use_query_cache">true</property>
```

---

## 🧰 Пример кэширования запросов

```java
Query query = session.createQuery("FROM User WHERE active = :active");
query.setParameter("active", true);

// Включаем кэш запросов
query.setCacheable(true);

// Получаем результаты из кэша или выполняем запрос в БД
List<User> users = query.list();
```

- `setCacheable(true)` — указывает Hibernate, что результат запроса можно кэшировать.
- Когда такой запрос выполняется повторно с теми же параметрами, Hibernate будет сначала искать результаты в кэше, и только если их нет — выполнит запрос в базу.

---

## 🔥 Настройка кэширования с EhCache

Кэш запросов работает в связке с **вторичным кэшем** (например, с **EhCache**). Вот как можно настроить **EhCache** для кэширования запросов.

1. Включи кэширование второго уровня:

```xml
<property name="hibernate.cache.use_second_level_cache">true</property>
<property name="hibernate.cache.region.factory_class">org.hibernate.cache.ehcache.EhCacheRegionFactory</property>
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

Кэширование работает **по запросу и его параметрам**. Это значит, что если ты выполняешь запрос с разными параметрами, то результат будет кэшироваться отдельно для каждого набора параметров.

### Пример:

```java
Query query = session.createQuery("FROM User WHERE name = :name");
query.setParameter("name", "John");

// Кэшируем запрос
query.setCacheable(true);

// Если запрос повторится с тем же параметром "John", результат будет взят из кэша
List<User> users = query.list();
```

- Запрос с параметром `"name = 'John'"` будет кэшироваться, и при следующем выполнении с теми же параметрами результат будет взят из кэша.

---

## 🧩 Интеграция с Spring Data JPA

В **Spring Data JPA** кэширование запросов также поддерживается. Для этого можно использовать аннотацию `@Query` с параметром `cacheable = true`.

### Пример:

```java
@Query("FROM User u WHERE u.name = :name")
@Cacheable("users") // Этот кэш будет использоваться для всех запросов
List<User> findByName(@Param("name") String name);
```

В этом случае, когда метод `findByName()` вызывается с теми же параметрами, Hibernate будет использовать кэшированное значение.

---

## ⚠️ Важные замечания:

- Кэширование запросов не **обновляет** кэш автоматически. То есть, если данные изменяются в базе данных, кэшированные результаты не будут обновлены.
- Нужно помнить, что если запрос включает много данных или параметров, кэширование может **занимать много памяти**. Старайся кэшировать только **часто используемые запросы с ограниченным количеством результатов**.
- **Параметризация** запросов критична — кэширование работает по параметрам, и если параметры изменяются, запрос будет выполнен заново.

---

## ✅ Преимущества кэширования запросов:

1. **Меньше нагрузки на базу данных**: кэшированные результаты берутся из памяти.
2. **Скорость**: результаты могут быть возвращены намного быстрее, чем выполнение SQL-запроса.
3. **Меньше блокировок**: кэш позволяет уменьшить количество конкуренции за блокировки в базе данных.

---

============
### Кэширования в реальном приложении
Отлично! Давай создадим **реальный пример**, где кэширование запросов в Hibernate будет использоваться с **сущностями** и их **связями**. Мы настроим кэш запросов для **вторичного кэширования** с использованием **EhCache**.

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
           maxEntriesLocalHeap="1000" eternal="false" timeToLiveSeconds="600" />
    <!-- Кэш для сущностей -->
    <cache name="com.example.User"
           maxEntriesLocalHeap="1000" eternal="true" />
</ehcache>
```

---

### 👩‍💻 Шаг 2: Сущности и связи

Теперь создадим несколько сущностей и запросим их с использованием кэширования запросов.

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
query.setParameter("active", true);

// Включаем кэширование запроса
query.setCacheable(true);

// Выполняем запрос и получаем результат
List<User> users = query.list();
```

### 🧠 Что происходит?

- Запрос будет **кэшироваться** в памяти через **EhCache**.
- Когда мы снова запросим те же данные с теми же параметрами (`active = true`), Hibernate сначала будет искать результат в кэше, а если его не будет — выполнит запрос к базе.

---

### 👩‍💻 Шаг 4: Интеграция с Spring Data JPA (если используешь Spring)

В **Spring Data JPA** можно легко интегрировать кэширование запросов с помощью аннотации `@Cacheable`.

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

- **Кэширование** работает **по параметрам запроса**: если ты изменишь параметры запроса, то результат будет заново загружен из базы.
- **Единый кэш** для всех запросов с одинаковыми параметрами, если они не обновляются или не меняются в базе.

---

### 👩‍💻 Шаг 5: Тестирование кэширования запросов

1. **Создание пользователей и заказов**:

```java
// Создаём пользователей и заказы
session.save(new User("John", true));
session.save(new User("Jane", false));

// Создаём заказы для пользователей
session.save(new Order(100.0, user1));
session.save(new Order(150.0, user2));
```

2. **Запрос с кэшированием**:

```java
// Первый запрос (будет выполнен в базу данных)
List<User> activeUsersFirstCall = userRepository.findByActive(true);

// Второй запрос (результат будет взят из кэша)
List<User> activeUsersSecondCall = userRepository.findByActive(true);
```

- При втором вызове данные будут **получены из кэша**, если они не изменились в базе.

---

### 🧩 Резюме

- **Кэширование запросов** позволяет хранить результаты запросов в памяти для повторных запросов с теми же параметрами.
- **Вторичный кэш** и **кэширование запросов** эффективно снижают нагрузку на базу данных.
- При **интеграции с Spring** можно использовать аннотацию `@Cacheable` для удобного включения кэширования на уровне репозиториев.


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


============================================================
------------------------------------------------------------






# Сессия Hibernate

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

### Фабрика сессий

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
      <property name="hibernate.cache.region.factory_class">org.hibernate.cache.ehcache.EhCacheRegionFactory</property>
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

> ## HQL, native, JPQL, Criteria запросы

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

### JPQL

Отличный вопрос! 💡 Давай разберёмся.

---

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
FROM User
u WHERE
u.age >18
```

### 🔹 JOIN:

```sql
SELECT o
FROM Order
o JOIN
o.user u
WHERE u.name ='Ivan'
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


Отличный вопрос! В контексте **Hibernate (и JPA)** термины `eager` и `lazy` относятся к **типу загрузки (fetching strategy)** связанных сущностей.

---

## 📦 Что такое `EAGER` и `LAZY`?

Они определяют, **когда Hibernate загружает связанные данные** из базы данных:

| Стратегия     | Описание                                               |
|----------------|--------------------------------------------------------|
| **LAZY**       | Загрузка **по требованию**, только когда реально используется. |
| **EAGER**      | Загрузка **сразу**, вместе с родительским объектом.       |

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

| Особенность                   | `LAZY`                     | `EAGER`                    |
|------------------------------|----------------------------|----------------------------|
| Производительность           | 🔼 Лучше (если не нужна коллекция) | 🔽 Может тянуть ненужные данные |
| Управление запросами         | ✅ Гибко                   | ❌ Меньше контроля         |
| Риск `LazyInitializationException` | ❌ Есть (если сессия закрыта) | ✅ Нет                    |
| Используется по умолчанию для `@OneToMany`, `@ManyToMany` | ✅ Да (`LAZY`)             | ❌ Нет (`EAGER` вручную)  |

---

## ⚠️ Важные нюансы

- Hibernate **по умолчанию делает `LAZY`** для коллекций (`@OneToMany`, `@ManyToMany`) и **`EAGER`** для одиночных ссылок (`@ManyToOne`, `@OneToOne`).
- У **`LAZY` загрузки есть риск**: если сессия уже закрыта, доступ к полю вызовет `LazyInitializationException`.

---

## 🛠️ Как защититься от `LazyInitializationException`?

- Используй `JOIN FETCH` в HQL/JPQL.
- Используй DTO и `EntityGraph`.
- Загружай данные внутри открытой транзакции (например, в `@Transactional` методе).

---

Хочешь пример с `JOIN FETCH` или как правильно подгрузить LAZY-поле в контроллере?


=============

Отличный вопрос! В **Hibernate / JPA** стратегии связывания сущностей определяются через аннотации отношений между объектами, и отвечают на вопрос: **как одна сущность связана с другой?** И как эта связь хранится в базе данных.

---

## 🔗 Основные типы связей между сущностями

| Тип связи            | Описание                              | Примеры                   |
|----------------------|----------------------------------------|---------------------------|
| `@OneToOne`          | Один к одному                         | У пользователя один профиль |
| `@OneToMany`         | Один ко многим                        | Один автор — много книг     |
| `@ManyToOne`         | Многие к одному                       | Много заказов — один клиент |
| `@ManyToMany`        | Многие ко многим                      | Пользователи и роли        |

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

| Cascade Type            | Что делает                                      |
|-------------------------|-------------------------------------------------|
| `ALL`                   | Применяет все действия                         |
| `PERSIST`               | Сохраняет связанные сущности                   |
| `MERGE`                 | Обновляет их                                   |
| `REMOVE`                | Удаляет                                        |
| `DETACH`, `REFRESH`     | Прочее управление состоянием                   |

---

## 💾 Дополнительно: `mappedBy` vs `JoinColumn`

- `mappedBy` — на стороне **обратной связи** (не владеющей)
- `@JoinColumn` — **владеющая сторона** (где находится внешний ключ)

---

## 🧠 Советы

- Для `@OneToMany` желательно всегда указывать `mappedBy`, иначе Hibernate может создать **лишнюю таблицу связи**.
- При `@ManyToMany` используется **промежуточная таблица**, и `@JoinTable` нужно указывать явно.
- Не злоупотребляй `CascadeType.ALL` — это может привести к неожиданным удалениям/обновлениям.






====




Отлично, давай подробно разберёмся, что такое **тип загрузки (fetching strategy)** для связанных сущностей в JPA / Hibernate — это ключевой момент для производительности и правильной работы с данными.

---

## 📦 Что такое Fetching Strategy?

**Fetching strategy** — это способ, которым **Hibernate загружает связанные сущности** из базы данных при обращении к основному объекту.

Есть два типа:

| Тип загрузки       | Описание |
|--------------------|----------|
| **LAZY** (ленивая) | Связанная сущность загружается **только при первом обращении** к ней. |
| **EAGER** (жадная) | Связанная сущность загружается **сразу вместе с родительской**. |

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

| Параметр                    | LAZY                        | EAGER                       |
|----------------------------|-----------------------------|-----------------------------|
| Когда загружается?          | При первом доступе к полю   | Сразу, при загрузке объекта |
| Производительность          | Лучше, если данные не нужны | Может тянуть ненужные данные |
| Количество SQL-запросов     | Больше (при обращении к полю) | Меньше (если данные нужны сразу) |
| Возможные ошибки            | `LazyInitializationException`, если сессия закрыта | Нет ошибок, но можно грузить слишком много |

---

## 🔄 Как подгрузить связанные сущности вручную?

1. **`JOIN FETCH` в JPQL**:

```java
SELECT u FROM User u JOIN FETCH u.orders WHERE u.id = :id
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

| Связь            | Тип загрузки по умолчанию |
|------------------|---------------------------|
| `@ManyToOne`     | `EAGER`                   |
| `@OneToOne`      | `EAGER`                   |
| `@OneToMany`     | `LAZY`                    |
| `@ManyToMany`    | `LAZY`                    |

---

## ✅ Рекомендации

- Всегда **используй LAZY по умолчанию**, и **подгружай явно**, когда нужно.
- Используй **JOIN FETCH** или **DTO**-проекцию, чтобы избежать N+1.
- Не забывай про `@Transactional`, если используешь LAZY в сервисах.

===================================================================
------------------------------------------------------------------


> ## Hibernate. Стратегии генерирования id

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

> ## Hibernate. тип загрузки (fetching strategy)** для связанных сущностей в JPA


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
