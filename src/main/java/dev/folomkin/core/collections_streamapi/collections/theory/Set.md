## ___Set\<E>___

Интерфейс Set моделирует математическое множество, в котором нет одинаковых
элементов. Часто встречающиеся реализации HashSet и LinkedHashSet. Подинтерфейс
SortedSet содержит отсортированные упорядоченные элементы. Его реализация -
TreeSet.

***Методы:***

- **boolean add(E o)** - добавляет указанный элемент в коллекцию, если его еще
  нет;
- **boolean remove(Object o)** - удаляет указанный объект, если он есть
- **boolean contains(Object o)** - возвращает true, если объект присутствует в
  множестве
- **boolean addAll(Collections<? extends E> c)** - создает объединение множеств
- **boolean retainAll(Collection<?> c )** - создает пересечение множеств

Реализации интерфейса Set включают:

- **класс HashSet\<E>** - хранит элементы в хеш-таблице по хеш коду; HashSet
  является самой лучшей реализацией для Set;
- **LinkedHashSet\<E>** - элементы хранятся в виде двусвязного списка, что
  позволяет организовать упорядоченные итерации вставки и удаления; элементы
  хэшируются с использованием метода hashCode() и организованы в связный список
  в соответствии с порядком вставки. В отличие от HashSet, класс LinkedHashSet
  строит связный список с использованием хэш-таблицы для увеличения
  эффективности операций вставки и удаления элементов (за счет более сложной
  структуры). Этот класс поддерживает связный список элементов в том порядке, в
  котором они вставлялись, т.е. в порядке метода add();
- **TreeSet\<E>** - также поддерживает подинтерфейсы NavigableSet и SortedSet;
  хранит элементы в виде структуры «дерево», в которой элементы отсортированы и
  управляемы; это эффективно для поиска, удаления и добавления элементов (оценка
  времени поиска – O(log(n)).

__Пример HashSet\<E>__

```java

public class ExampleStart {
    public static void main(String[] args) {
        Book book1 = new Book(1, "Война и мир");
        Book book1Dup = new Book(1, "Война и мир");
        Book book2 = new Book(2, "Анна Каренина");
        Book book3 = new Book(3, "Java для чайников");
        Set<Book> set1 = new HashSet<Book>();
        set1.add(book1);
        set1.add(book1Dup);
        set1.add(book1);
        set1.add(book3);
        set1.add(null);
        set1.add(book2);
        System.out.println(set1);
        set1.remove(book1);
        set1.remove(book3);
        System.out.println(set1);
        Set<Book> set2 = new HashSet<>();
        set2.add(book3);
        System.out.println(set2);
        set2.addAll(set1);
        System.out.println(set2);
        set2.remove(null);
        System.out.println(set2);
    }
}

class Book {
    private int id;
    private String title;

    public Book(int id, String title) {
        this.id = id;
        this.title = title;
    }

    @Override
    public String toString() {
        return id + ":" + title;
    }

    //Две книги равны, если у них одинаковые id;
    @Override
    public boolean equals(Object o) {
        if (!(o instanceof Book)) {
            return false;
        }
        return this.id == ((Book) o).id;
    }

    //Два объекта равны, если они имеют одинаковый хеш-код.
    @Override
    public int hashCode() {
        return id;
    }

}
```

__Пример для LinkedHashSet\<E>__

```java
public class ExampleStart {
    public static void main(String[] args) {
        Book book1 = new Book(1, "Анна Каренина");
        Book book1Dup = new Book(1, "Анна Каренина");
        Book book2 = new Book(2, "Война и мир");
        Book book3 = new Book(3, "Java для чайников");
        Set<Book> set = new LinkedHashSet<>();
        set.add(book1);
        set.add(book1Dup);
        set.add(book1);
        set.add(book3);
        set.add(null);
        set.add(null);
        set.add(book2);
        System.out.println(set);
    }
}

class Book {
    private int id;
    private String title;

    public Book(int id, String title) {
        this.id = id;
        this.title = title;
    }

    @Override
    public String toString() {
        return id + ":" + title;
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof Book)) {
            return false;
        }
        return this.id == ((Book) o).id;
    }

    @Override
    public int hashCode() {
        return id;
    }
}
```

### ___Интерфейсы NavigableSet\<E> и SortedSet\<E>___

Объекты в __SortedSet__ сортируются естественным способом интерфейсом Comparable
или с помощью объекта Comparator или по добавлению методом add().

Интерфейс __NavigableSet\<E>__ является подинтерфейсом множества Set и объявляет
дополнительные методы навигации (нахождение ближайшего элемента):

- **Iterator \<E> descendingIterator()** - возвращает итератор для элементов
  данного множества в убывающем порядке;
- **Iterator \<E> iterator()** - возвращает итератор для элементов
  данного множества в возрастающем порядке;

___Операции для отдельных элементов___

- **E floor(E e)** - возвращает наибольший, меньший, равный заданному
  элементу/null(если такого нет) элемент множества;
- **E ceiling(E e)** - возвращает наименьший, больший, равный заданному, null(
  если такого нет) элемент множества;
- **E lower(E e)** - возвращает наибольший, строго меньший заданного, null(если
  такого нет) элемент множества;
- **E higher(E e)** - возвращает наименьший, строго заданного или null(если
  такого нет) элемент множества;

___Операции над подмножеством___

- **SortedSet\<E> headSet(E toElement)** - возвращает подмножество данного
  множества, состоящее из элементов строго меньших toElement;
- **SortedSet\<E> tailSet(E fromElement)** - возвращает подмножество данного
  множества, состоящее из элементов которые больше или равны fromElement;
- **SortedSet\<E> subSet(E fromElement, E toElement)** - возвращает подмножество
  данного множества, элементы которого находятся в диапазоне от fromElement
  включительно до toElement не включая;

### ___Класс TreeSet\<E> с Comparable___

```java
class AddressBookEntry implements Comparable<AddressBookEntry> {
    private String name, address, phone;

    public AddressBookEntry(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return name;
    }

    @Override
    public int compareTo(AddressBookEntry another) {
        return this.name.compareToIgnoreCase(another.name);
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof AddressBookEntry)) {
            return false;
        }
        return this.name.equalsIgnoreCase(((AddressBookEntry) obj).name);
    }

    @Override
    public int hashCode() {
        return name.length();
    }
}

public class ExampleStart {
    public static void main(String[] args) {
        AddressBookEntry addr1 = new AddressBookEntry("петр");
        AddressBookEntry addr2 = new AddressBookEntry("ПАВЕЛ");
        AddressBookEntry addr3 = new AddressBookEntry("Сергей");
        AddressBookEntry addr4 = new AddressBookEntry("Олег");
        AddressBookEntry addr5 = new AddressBookEntry("Алексей");
        TreeSet<AddressBookEntry> set = new TreeSet<>();
        set.add(addr1);
        set.add(addr2);
        set.add(addr3);
        set.add(addr4);
        set.add(addr5);
        System.out.println(set);
        System.out.println(set.floor(addr2));
        System.out.println(set.lower(addr2));
        System.out.println(set.headSet(addr2));
        System.out.println(set.tailSet(addr2));
    }
}
```

### ___Класс TreeSet\<E> с Comparator___

```java
class PhoneBookEntry {
    public String name, address, phone;

    public PhoneBookEntry(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return name;
    }
}

public class ExampleStart {
    public static class PhoneBookComparator
            implements Comparator<PhoneBookEntry> {
        @Override
        public int compare(PhoneBookEntry p1, PhoneBookEntry p2) {
            return p2.name.compareToIgnoreCase(p1.name); //по убыванию от name;
        }
    }

    public static void main(String[] args) {
        PhoneBookEntry addr1 = new PhoneBookEntry("петр");
        PhoneBookEntry addr2 = new PhoneBookEntry("ПАВЕЛ");
        PhoneBookEntry addr3 = new PhoneBookEntry("Сергей");
        PhoneBookEntry addr4 = new PhoneBookEntry("Олег");
        PhoneBookEntry addr5 = new PhoneBookEntry("Алексей");

        Comparator<PhoneBookEntry> comp = new PhoneBookComparator();
        TreeSet<PhoneBookEntry> set = new TreeSet<>(comp);
        set.add(addr1);
        set.add(addr2);
        set.add(addr3);
        set.add(addr4);
        set.add(addr5);
        System.out.println(set);
        Set<PhoneBookEntry> newSet = set.descendingSet();
        System.out.println(newSet);
    }
}
```
