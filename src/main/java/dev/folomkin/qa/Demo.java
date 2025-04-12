package dev.folomkin.qa;

class MyObject {

}

class MyClass {
    static MyObject staticObj = new MyObject(); // Статическое поле хранит ссылку на объект
}

public class Demo {
    public static void main(String[] args) {
        // Объект staticObj считается "живым" благодаря статической ссылке
        MyClass.staticObj = new MyObject();
    }
}

