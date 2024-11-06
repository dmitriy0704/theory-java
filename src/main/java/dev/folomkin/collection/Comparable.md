### _Comparable_

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

### Comparator

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
