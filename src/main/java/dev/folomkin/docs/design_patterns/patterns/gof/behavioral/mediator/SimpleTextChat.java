package dev.folomkin.docs.design_patterns.patterns.gof.behavioral.mediator;

import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

public class SimpleTextChat implements Chat {

    User admin;
    List<User> users = new ArrayList<>();

    public void setAdmin(User admin) {
        this.admin = admin;
    }

    public void addUserToChat(User user) {
        this.users.add(user);
    }

    @Override
    public void sendMessage(String message, User user) {
        for (User u : users) {
            if (u != user) {
                u.getMessage(message);
            }
        }
        admin.getMessage(message);
    }
}
