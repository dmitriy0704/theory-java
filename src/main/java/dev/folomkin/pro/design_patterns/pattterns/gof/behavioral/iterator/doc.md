# Итератор (Iterator)

- это поведенческий паттерн проектирования, который даёт возможность
  последовательно обходить элементы составных объектов, не раскрывая их
  внутреннего представления.

Благодаря Итератору, клиент может обходить разные коллекции одним и тем же
способом, используя единый интерфейс итераторов.

Применимость: Паттерн можно часто встретить в Java-коде, особенно в программах,
работающих с разными типами коллекций, и где требуется обход разных сущностей.

**Примеры Итератора в стандартных библиотеках Java:**

Все реализации java.util.Iterator (среди прочего также java.util.Scanner).

**Все реализации java.util.Enumeration.**

Признаки применения паттерна: Итератор легко определить по методам навигации (
например, получения следующего/предыдущего элемента и т. д.). Код использующий
итератор зачастую вообще не имеет ссылок на коллекцию, с которой работает
итератор. Итератор либо принимает коллекцию в параметрах конструктора при
создании, либо возвращается самой коллекцией.

## Структура

![iterator_structure.png](/img/design_pattern/design_patterns/iterator_structure.png)

1. Итератор описывает интерфейс для доступа и обхода элементов коллекции.
2. Конкретный итератор реализует алгоритм обхода какой-то
   конкретнойколлекции.Объектитераторадолженсамотсле-
   живать текущую позицию при обходе коллекции, чтобы
   отдельные итераторы могли обходить одну и ту же коллек-
   циюнезависимо.
3. Коллекция описывает интерфейс получения итератора из
   коллекции.Какмыужеговорили,коллекцииневсегдаявля-
   ются списком. Это может быть и база данных, и удалённое
   API,идажедеревоКомпоновщика.Поэтомусамаколлекция
   можетсоздаватьитераторы,таккаконазнает,какиеименно
   итераторы способны с нейработать.
4. Конкретная коллекция возвращает новый экземпляр опре-
   делённого конкретного итератора, связав его с текущим
   объектом коллекции. Обратите внимание, что сигнатура
   метода возвращает интерфейс итератора. Это позволяет
   клиенту не зависеть от конкретных классовитераторов.
5. Клиент работаетсовсемиобъектамичерезинтерфейсыкол-
   лекциииитератора.Такклиентскийкоднезависитоткон-
   кретных классов, что позволяет применять различные
   итераторы, не изменяя существующий кодпрограммы.<br>
   В общем случае клиенты не создают объекты итераторов,а
   получают их из коллекций.Темнеменее, если клиенту тре-
   буется специальный итератор, он всегда может создать его
   самостоятельно.

## Пример кода

Перебор профилей социальной сети

В этом примере паттерн Итератор используется для реа-
лизации обхода нестандартной коллекции, которая инкап-
сулирует доступ к социальному графу Facebook. Коллекция
предоставляетнесколькоитераторов,которыемогутпо-раз-
ному обходить профилилюдей.

>![iterator_example.png](/img/design_pattern/design_patterns/iterator_example.png)
>Пример обхода социальных профилей через итератор.

```java

package dev.folomkin.design_patterns.pattterns.behavioral.iterator;


import java.util.*;

//-> ./ iterators/ProfileIterator.java: Интерфейс итератора

/**
 * Общий интерфейс итераторов.
 */
interface ProfileIterator {
    boolean hasNext();

    dev.folomkin.pro.design_patterns.pattterns.gof.behavioral.iterator.Profile getNext();

    void reset();
}


//-> ./iterators/FacebookIterator.java: Итератор пользователей сети Facebook

/**
 * Конкретный итератор.
 */
class FacebookIterator implements dev.folomkin.pro.design_patterns.pattterns.gof.behavioral.iterator.ProfileIterator {
    // Итератору нужна ссылка на коллекцию, которую он обходит.
    private dev.folomkin.pro.design_patterns.pattterns.gof.behavioral.iterator.Facebook facebook;
    private String type;
    private String email;

    // Но каждый итератор обходит коллекцию, независимо от
    // остальных, поэтому он содержит информацию о текущей
    // позиции обхода.
    private int currentPosition = 0;
    private List<String> emails = new ArrayList<>();
    private List<dev.folomkin.pro.design_patterns.pattterns.gof.behavioral.iterator.Profile> profiles = new ArrayList<>();

    public FacebookIterator(dev.folomkin.pro.design_patterns.pattterns.gof.behavioral.iterator.Facebook facebook, String type, String email) {
        this.facebook = facebook;
        this.type = type;
        this.email = email;
    }

    private void lazyLoad() {
        if (emails.size() == 0) {
            List<String> profiles = facebook.requestProfileFriendsFromFacebook(this.email, this.type);
            for (String profile : profiles) {
                this.emails.add(profile);
                this.profiles.add(null);
            }
        }
    }

    @Override
    public boolean hasNext() {
        lazyLoad();
        return currentPosition < emails.size();
    }

    // Итератор реализует методы базового интерфейса по-своему.
    @Override
    public dev.folomkin.pro.design_patterns.pattterns.gof.behavioral.iterator.Profile getNext() {
        if (!hasNext()) {
            return null;
        }

        String friendEmail = emails.get(currentPosition);
        dev.folomkin.pro.design_patterns.pattterns.gof.behavioral.iterator.Profile friendProfile = profiles.get(currentPosition);
        if (friendProfile == null) {
            friendProfile = facebook.requestProfileFromFacebook(friendEmail);
            profiles.set(currentPosition, friendProfile);
        }
        currentPosition++;
        return friendProfile;
    }

    @Override
    public void reset() {
        currentPosition = 0;
    }
}

//-> ./iterators/LinkedInIterator.java: Итератор пользователей сети LinkedIn
class LinkedInIterator implements dev.folomkin.pro.design_patterns.pattterns.gof.behavioral.iterator.ProfileIterator {
    private dev.folomkin.pro.design_patterns.pattterns.gof.behavioral.iterator.LinkedIn linkedIn;
    private String type;
    private String email;
    private int currentPosition = 0;
    private List<String> emails = new ArrayList<>();
    private List<dev.folomkin.pro.design_patterns.pattterns.gof.behavioral.iterator.Profile> contacts = new ArrayList<>();

    public LinkedInIterator(dev.folomkin.pro.design_patterns.pattterns.gof.behavioral.iterator.LinkedIn linkedIn, String type, String email) {
        this.linkedIn = linkedIn;
        this.type = type;
        this.email = email;
    }

    private void lazyLoad() {
        if (emails.size() == 0) {
            List<String> profiles = linkedIn.requestRelatedContactsFromLinkedInAPI(this.email, this.type);
            for (String profile : profiles) {
                this.emails.add(profile);
                this.contacts.add(null);
            }
        }
    }

    @Override
    public boolean hasNext() {
        lazyLoad();
        return currentPosition < emails.size();
    }

    @Override
    public dev.folomkin.pro.design_patterns.pattterns.gof.behavioral.iterator.Profile getNext() {
        if (!hasNext()) {
            return null;
        }

        String friendEmail = emails.get(currentPosition);
        dev.folomkin.pro.design_patterns.pattterns.gof.behavioral.iterator.Profile friendContact = contacts.get(currentPosition);
        if (friendContact == null) {
            friendContact = linkedIn.requestContactInfoFromLinkedInAPI(friendEmail);
            contacts.set(currentPosition, friendContact);
        }
        currentPosition++;
        return friendContact;
    }

    @Override
    public void reset() {
        currentPosition = 0;
    }
}


//-> ./social_networks
//-> ./social_networks/SocialNetwork.java: Интерфейс социальной сети


/**
 * Общий интерфейс коллекций должен определить фабричный метод
 * для производства итератора. Можно определить сразу несколько
 * методов, чтобы дать пользователям различные варианты обхода
 * одной и той же коллекции.
 */
interface SocialNetwork {
    dev.folomkin.pro.design_patterns.pattterns.gof.behavioral.iterator.ProfileIterator createFriendsIterator(String profileEmail);

    dev.folomkin.pro.design_patterns.pattterns.gof.behavioral.iterator.ProfileIterator createCoworkersIterator(String profileEmail);
}


//-> ./social_networks/Facebook.java: Facebook


/**
 * Конкретная коллекция знает, объекты каких итераторов нужно создавать.
 */

class Facebook implements dev.folomkin.pro.design_patterns.pattterns.gof.behavioral.iterator.SocialNetwork {
    private List<dev.folomkin.pro.design_patterns.pattterns.gof.behavioral.iterator.Profile> profiles;

    public Facebook(List<dev.folomkin.pro.design_patterns.pattterns.gof.behavioral.iterator.Profile> cache) {
        if (cache != null) {
            this.profiles = cache;
        } else {
            this.profiles = new ArrayList<>();
        }
    }

    public dev.folomkin.pro.design_patterns.pattterns.gof.behavioral.iterator.Profile requestProfileFromFacebook(String profileEmail) {
        // Здесь бы был POST запрос к одному из адресов API Facebook. Но вместо
        // этого мы эмулируем долгое сетевое соединение, прямо как в реальной
        // жизни...
        simulateNetworkLatency();
        System.out.println("Facebook: Loading profile '" + profileEmail + "' over the network...");

        // ...и возвращаем тестовые данные.
        return findProfile(profileEmail);
    }

    public List<String> requestProfileFriendsFromFacebook(String profileEmail, String contactType) {
        // Здесь бы был POST запрос к одному из адресов API Facebook. Но вместо
        // этого мы эмулируем долгое сетевое соединение, прямо как в реальной
        // жизни...
        simulateNetworkLatency();
        System.out.println("Facebook: Loading '" + contactType + "' list of '" + profileEmail + "' over the network...");

        // ...и возвращаем тестовые данные.
        dev.folomkin.pro.design_patterns.pattterns.gof.behavioral.iterator.Profile profile = findProfile(profileEmail);
        if (profile != null) {
            return profile.getContacts(contactType);
        }
        return null;
    }

    private dev.folomkin.pro.design_patterns.pattterns.gof.behavioral.iterator.Profile findProfile(String profileEmail) {
        for (dev.folomkin.pro.design_patterns.pattterns.gof.behavioral.iterator.Profile profile : profiles) {
            if (profile.getEmail().equals(profileEmail)) {
                return profile;
            }
        }
        return null;
    }

    private void simulateNetworkLatency() {
        try {
            Thread.sleep(2500);
        } catch (InterruptedException ex) {
            ex.printStackTrace();
        }
    }

    // ...Основной код коллекции...

    // Код получения нужного итератора.
    @Override
    public dev.folomkin.pro.design_patterns.pattterns.gof.behavioral.iterator.ProfileIterator createFriendsIterator(String profileEmail) {
        return new dev.folomkin.pro.design_patterns.pattterns.gof.behavioral.iterator.FacebookIterator(this, "friends", profileEmail);
    }

    @Override
    public dev.folomkin.pro.design_patterns.pattterns.gof.behavioral.iterator.ProfileIterator createCoworkersIterator(String profileEmail) {
        return new dev.folomkin.pro.design_patterns.pattterns.gof.behavioral.iterator.FacebookIterator(this, "coworkers", profileEmail);
    }

}


//-> ./social_networks/LinkedIn.java: LinkedIn
class LinkedIn implements dev.folomkin.pro.design_patterns.pattterns.gof.behavioral.iterator.SocialNetwork {
    private List<dev.folomkin.pro.design_patterns.pattterns.gof.behavioral.iterator.Profile> contacts;

    public LinkedIn(List<dev.folomkin.pro.design_patterns.pattterns.gof.behavioral.iterator.Profile> cache) {
        if (cache != null) {
            this.contacts = cache;
        } else {
            this.contacts = new ArrayList<>();
        }
    }

    public dev.folomkin.pro.design_patterns.pattterns.gof.behavioral.iterator.Profile requestContactInfoFromLinkedInAPI(String profileEmail) {
        // Здесь бы был POST запрос к одному из адресов API LinkedIn. Но вместо
        // этого мы эмулируем долгое сетевое соединение, прямо как в реальной
        // жизни...
        simulateNetworkLatency();
        System.out.println("LinkedIn: Loading profile '" + profileEmail + "' over the network...");

        // ...и возвращаем тестовые данные.
        return findContact(profileEmail);
    }

    public List<String> requestRelatedContactsFromLinkedInAPI(String profileEmail, String contactType) {
        // Здесь бы был POST запрос к одному из адресов API LinkedIn. Но вместо
        // этого мы эмулируем долгое сетевое соединение, прямо как в реальной
        // жизни...
        simulateNetworkLatency();
        System.out.println("LinkedIn: Loading '" + contactType + "' list of '" + profileEmail + "' over the network...");

        // ...и возвращаем тестовые данные.
        dev.folomkin.pro.design_patterns.pattterns.gof.behavioral.iterator.Profile profile = findContact(profileEmail);
        if (profile != null) {
            return profile.getContacts(contactType);
        }
        return null;
    }

    private dev.folomkin.pro.design_patterns.pattterns.gof.behavioral.iterator.Profile findContact(String profileEmail) {
        for (dev.folomkin.pro.design_patterns.pattterns.gof.behavioral.iterator.Profile profile : contacts) {
            if (profile.getEmail().equals(profileEmail)) {
                return profile;
            }
        }
        return null;
    }

    private void simulateNetworkLatency() {
        try {
            Thread.sleep(2500);
        } catch (InterruptedException ex) {
            ex.printStackTrace();
        }
    }

    @Override
    public dev.folomkin.pro.design_patterns.pattterns.gof.behavioral.iterator.ProfileIterator createFriendsIterator(String profileEmail) {
        return new dev.folomkin.pro.design_patterns.pattterns.gof.behavioral.iterator.LinkedInIterator(this, "friends", profileEmail);
    }

    @Override
    public dev.folomkin.pro.design_patterns.pattterns.gof.behavioral.iterator.ProfileIterator createCoworkersIterator(String profileEmail) {
        return new dev.folomkin.pro.design_patterns.pattterns.gof.behavioral.iterator.LinkedInIterator(this, "coworkers", profileEmail);
    }
}


//-> ./profile
//-> ./profile/Profile.java: Профиль пользователя
class Profile {
    private String name;
    private String email;
    private Map<String, List<String>> contacts = new HashMap<>();

    public Profile(String email, String name, String... contacts) {
        this.email = email;
        this.name = name;

        // Parse contact list from a set of "friend:email@gmail.com" pairs.
        for (String contact : contacts) {
            String[] parts = contact.split(":");
            String contactType = "friend", contactEmail;
            if (parts.length == 1) {
                contactEmail = parts[0];
            } else {
                contactType = parts[0];
                contactEmail = parts[1];
            }
            if (!this.contacts.containsKey(contactType)) {
                this.contacts.put(contactType, new ArrayList<>());
            }
            this.contacts.get(contactType).add(contactEmail);
        }
    }

    public String getEmail() {
        return email;
    }

    public String getName() {
        return name;
    }

    public List<String> getContacts(String contactType) {
        if (!this.contacts.containsKey(contactType)) {
            this.contacts.put(contactType, new ArrayList<>());
        }
        return contacts.get(contactType);
    }
}

//-> ./spammer
//-> ./spammer/SocialSpammer.java: Приложение рассылки сообщений


/**
 * Вот ещё полезная тактика: мы можем передавать объект
 * итератора вместо коллекции в клиентские классы. При таком
 * подходе клиентский код не будет иметь доступа к коллекциям, а
 * значит, его не будут волновать подробности их реализаций. Ему
 * будет доступен только общий интерфейс итераторов.
 */

class SocialSpammer {
    public dev.folomkin.pro.design_patterns.pattterns.gof.behavioral.iterator.SocialNetwork network;
    public dev.folomkin.pro.design_patterns.pattterns.gof.behavioral.iterator.ProfileIterator iterator;

    public SocialSpammer(dev.folomkin.pro.design_patterns.pattterns.gof.behavioral.iterator.SocialNetwork network) {
        this.network = network;
    }

    public void sendSpamToFriends(String profileEmail, String message) {
        System.out.println("\nIterating over friends...\n");
        iterator = network.createFriendsIterator(profileEmail);
        while (iterator.hasNext()) {
            dev.folomkin.pro.design_patterns.pattterns.gof.behavioral.iterator.Profile profile = iterator.getNext();
            sendMessage(profile.getEmail(), message);
        }
    }

    public void sendSpamToCoworkers(String profileEmail, String message) {
        System.out.println("\nIterating over coworkers...\n");
        iterator = network.createCoworkersIterator(profileEmail);
        while (iterator.hasNext()) {
            dev.folomkin.pro.design_patterns.pattterns.gof.behavioral.iterator.Profile profile = iterator.getNext();
            sendMessage(profile.getEmail(), message);
        }
    }

    public void sendMessage(String email, String message) {
        System.out.println("Sent message to: '" + email + "'. Message body: '" + message + "'");
    }
}


//-> ./Demo.java: Клиентский код

/**
 * Демо-класс. Здесь всё сводится воедино.
 * Класс приложение конфигурирует классы, как захочет.
 */
public class Demo {
    public static Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {
        System.out.println("Please specify social network to target spam tool (default:Facebook):");
        System.out.println("1. Facebook");
        System.out.println("2. LinkedIn");
        String choice = scanner.nextLine();

        dev.folomkin.pro.design_patterns.pattterns.gof.behavioral.iterator.SocialNetwork network;
        if (choice.equals("2")) {
            network = new dev.folomkin.pro.design_patterns.pattterns.gof.behavioral.iterator.LinkedIn(createTestProfiles());
        } else {
            network = new dev.folomkin.pro.design_patterns.pattterns.gof.behavioral.iterator.Facebook(createTestProfiles());
        }

        dev.folomkin.pro.design_patterns.pattterns.gof.behavioral.iterator.SocialSpammer spammer = new dev.folomkin.pro.design_patterns.pattterns.gof.behavioral.iterator.SocialSpammer(network);
        spammer.sendSpamToFriends("anna.smith@bing.com",
                "Hey! This is Anna's friend Josh. Can you do me a favor and like this post [link]?");
        spammer.sendSpamToCoworkers("anna.smith@bing.com",
                "Hey! This is Anna's boss Jason. Anna told me you would be interested in [link].");
    }

    public static List<dev.folomkin.pro.design_patterns.pattterns.gof.behavioral.iterator.Profile> createTestProfiles() {
        List<dev.folomkin.pro.design_patterns.pattterns.gof.behavioral.iterator.Profile> data = new ArrayList<dev.folomkin.pro.design_patterns.pattterns.gof.behavioral.iterator.Profile>();
        data.add(new dev.folomkin.pro.design_patterns.pattterns.gof.behavioral.iterator.Profile("anna.smith@bing.com", "Anna Smith", "friends:mad_max@ya.com", "friends:catwoman@yahoo.com", "coworkers:sam@amazon.com"));
        data.add(new dev.folomkin.pro.design_patterns.pattterns.gof.behavioral.iterator.Profile("mad_max@ya.com", "Maximilian", "friends:anna.smith@bing.com", "coworkers:sam@amazon.com"));
        data.add(new dev.folomkin.pro.design_patterns.pattterns.gof.behavioral.iterator.Profile("bill@microsoft.eu", "Billie", "coworkers:avanger@ukr.net"));
        data.add(new dev.folomkin.pro.design_patterns.pattterns.gof.behavioral.iterator.Profile("avanger@ukr.net", "John Day", "coworkers:bill@microsoft.eu"));
        data.add(new dev.folomkin.pro.design_patterns.pattterns.gof.behavioral.iterator.Profile("sam@amazon.com", "Sam Kitting", "coworkers:anna.smith@bing.com", "coworkers:mad_max@ya.com", "friends:catwoman@yahoo.com"));
        data.add(new dev.folomkin.pro.design_patterns.pattterns.gof.behavioral.iterator.Profile("catwoman@yahoo.com", "Liza", "friends:anna.smith@bing.com", "friends:sam@amazon.com"));
        return data;
    }
}


```

## Шаги реализации

1. Создайте общий интерфейс итераторов. Обязательный минимум — это операция
   получения следующего элемента коллекции. Но для удобства можно предусмотреть
   и другое. Например, методы для получения предыдущего элемента, текущей
   позиции, проверки окончания обхода и прочие.
2. Создайте интерфейс коллекции и опишите в нём метод получения итератора.
   Важно, чтобы сигнатура метода возвращала общий интерфейс итераторов, а не
   один из конкретных итераторов.
3. Создайте классы конкретных итераторов для тех коллекций, которые нужно
   обходить с помощью паттерна. Итератор должен быть привязан только к одному
   объекту коллекции. Обычно эта связь устанавливается через конструктор.
4. Реализуйте методы получения итератора в конкретных классах коллекций. Они
   должны создавать новый итератор того класса, который способен работать с
   данным типом коллекции. Коллекция должна передавать ссылку на собственный
   объект в конструктор итератора.
5. В клиентском коде и в классах коллекций не должно остаться кода обхода
   элементов. Клиент должен получать новый итератор из объекта коллекции каждый
   раз, когда ему нужно перебрать её элементы.