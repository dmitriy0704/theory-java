# JPA и Spring Data JPA

Java Persistence API (JPA) — это стандарт языка Java для управления реляционными
данными в Java-приложениях. Этот стандарт обеспечивает удобный механизм
взаимодействия с базой данных через объектно-реляционное отображение (ORM).

Spring Data JPA упрощает работу с JPA путём автоматизации написания
повторяющегося кода. Этот фреймворк построен на основе JPA и предлагает
упрощенное API для исполнения CRUD-операций и создания запросов на основе имён
методов.

Пример JPA-сущности:

```java
@Entity
public class FunExampleEntity {
  @Id
  private Long id;
  // Поля, которые также требуются.
}
```

Используя Spring Data JPA, мы можем определить репозиторий следующим образом:

```java
public interface FunExampleEntityRepository extends 
        JpaRepository<FunExampleEntity, Long> {
    List<FunExampleEntity> findByFunPropertyName(String funPropertyName);
}
```

## Особенности Spring Data JPA

### Реализация шаблона "репозиторий" в Spring Data JPA


=======
# Изучение Spring Data JPA

## Подключение к БД

### H2

```sql
create table if not exists purchase
(
    id int auto_increment primary key,
    product varchar(50) not null,
    price double not null
);
```

```yml
spring:
    h2:
    console: 
        enabled: true
    datasource:
        url: jdbc:h2:mem:testdb
        driverClassName: org.h2.Driver
        username: sa
        password: password
    jpa:
        database-platform: org.hibernate.dialect.H2Dialect

```

### Postgres

```yml
spring:
    application:
        name: spring-data-jdbc
    datasource:
        url: jdbc:postgresql://localhost:5433/demo_db
        username: user
        password: pass
    jpa:
        hibernate:
        ddl-auto: create
        database-platform: org.hibernate.dialect.PostgreSQLDialect
        database: POSTGRESQL
        show-sql: true
    sql:
        init:
        platform: postgres
        mode: always
    docker:
        compose:
            enabled: true
            lifecycle-management: start_and_stop

#  docker-compose.yml

volumes:
  pg_demo:

services:
  demo_db:
    image: postgres
    restart: always
    environment:
      - POSTGRES_USER=user
      - POSTGRES_PASSWORD=pass
      - POSTGRES_DB=demo_db
    volumes:
      - pg_demo:/var/lib/postgresql/data
    ports:
      - "127.0.0.1:5433:5432"

```

## Связи сущностей

### Один ко многим

Пример связи "один ко многим" для сущностей Singer и Album. У одного певца может быть несколько альбомов.

```java

@Entity
@Table(name = "singer")
public class Singer implements Serializable {

    // Поля....

    private Set<Album> albums = new HashSet<>();

    @OneToMany(mappedBy = "singer",
            cascade = CascadeType.ALL, orphanRemoval = true)
    public Set<Album> getAlbums() {
        return albums;
    }

    public boolean addAlbum(Album album) {
        album.setSinger(this);
        return getAlbums().add(album);
    }

    // Getters and Setters, toString()
}


@Entity
@Table(name = "album")
public class Album implements Serializable {

    // поля...

    private Singer singer;

    @ManyToOne
    @JoinColumn(name = "singer_id")
    public Singer getSinger() {
        return singer;
    }

    public void setSinger(Singer singer) {
        this.singer = singer;
    }

    // Getters and Setters, toString()
}

```

### Многие ко многим

Для связей "многие ко многим" требуется промежуточная таблица, через которую
осуществляется соединение.

```java

@Entity
@Table(name = "singer")
public class Singer implements Serializable {

    // ...
    private Set<Album> albums = new HashSet<>();
    private Set<Instrument> instruments = new HashSet<>();

    // -> Один певец играет на нескольких инструментах или инструмент
    // принадлежит нескольким певцам
    @ManyToMany
    @JoinTable(name = "singer_instrument",
            joinColumns = @JoinColumn(name = "singer_id"),
            inverseJoinColumns = @JoinColumn(name = "instrument_id"))
    public Set<Instrument> getInstruments() {
        return instruments;
    }

    public void setInstruments(Set<Instrument> instruments) {
        this.instruments = new HashSet<>();
    }

    // ...
}

```

Метод получения свойства instruments из класса Singer снабжен аннотацией
@ManyToMany. В данном коде предоставляется аннотация @JoinTable для указания
промежуточной таблицы для соединения, которую
должна искать библиотека Hibernate.

В атрибуте "name" задается имя промежуточной
таблицы для соединения, в атрибуте joinColumns определяется столбец с внешним
ключом для таблицы SINGER, а в атрибуте inverseJoinColumns указывается столбец
с внешним ключом для таблицы INSTRUMENT на другой стороне устанавливаемой связи.
Исходный код класса Instrument, здесь атрибуты joinColumns и inverseJoinColumns
поменялись местами, отражая отношение "многие ко многим".

```java

@Entity
@Table(name = "instrument")
public class Instrument implements Serializable {

    // ...
    private Set<Singer> singers = new HashSet<>();

    //...

    @ManyToMany
    @JoinTable(name = "singer_instrument",
            joinColumns = @JoinColumn(name = "instrument_id"),
            inverseJoinColumns = @JoinColumn(name = "singer_id"))
    public Set<Singer> getSingers() {
        return this.singers;
    }

    //...

    public void setSingers(Set<Singer> singers) {
        this.singers = singers;
    }
}

```