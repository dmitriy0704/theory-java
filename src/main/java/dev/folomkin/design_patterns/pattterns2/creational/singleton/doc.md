# Singleton

Мы используем шаблон Singleton, когда нам нужно иметь только один экземпляр
нашего класса и предоставить глобальную точку доступа к нему.

Существует две формы шаблона проектирования Singleton:

- Раннее создание экземпляра: мы создаем экземпляр во время загрузки.
- Ленивое создание экземпляра: мы создаем экземпляр, когда нам это необходимо.

Чтобы создать класс Singleton, нам нужно иметь:

- статический (static) член класса
- частный (private) конструктор (предотвращает создание экземпляра
  одноэлементного класса вне класса)
- статический фабричный метод (предоставляет глобальную точку для доступа к
  одноэлементному объекту и возвращает экземпляр клиенту)


```java
class NotificationService {
    // Также может быть инициализирован в статическом блоке
    private final static NotificationService INSTANCE = new NotificationService();

    private NotificationService() {
    }

    public static NotificationService getInstance() {
        return INSTANCE;
    }

    private void sendNotification(String message) {
        System.out.println("Notification sent " + message);
    }
}

```

Пример реализации ленивой (lazy) загрузки шаблона Singleton:

