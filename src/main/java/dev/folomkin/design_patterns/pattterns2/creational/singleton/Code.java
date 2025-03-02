package dev.folomkin.design_patterns.pattterns2.creational.singleton;

public class Code {
}

class NotificationService {
    // Также может быть инициализирован в статическом блоке
    private static NotificationService instance;

    private NotificationService() {
    }

    public static NotificationService getInstance() {
        if (instance == null) {
            instance = new NotificationService();
        }
        return instance;
    }

    private void sendNotification(String message) {
        System.out.println("Notification sent " + message);
    }
}
