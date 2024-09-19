package dev.folomkin.core.oop_classType.oop;


interface Book {
    void readBook();
}

class SpringBook implements Book {

    @Override
    public void readBook() {
        System.out.println("Spring Book");
    }
}

class SpringBootBook extends SpringBook {
    @Override
    public void readBook() {
        System.out.println("Spring Boot Book");
    }
}

class Code extends SpringBootBook{
    public void readBook() {

    }
    public static void main(String[] args) {

    }
}
