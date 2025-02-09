package dev.folomkin.testing.mockito;


public interface UserService {
    User findUser(String username, int age);
}

class User {
    String name;
    int age;

    public User(String name, int age) {
        this.name = name;
        this.age = age;
    }
}
