package dev.folomkin.java.features;


final class Pear extends Fruit {
}

final class Apple extends Fruit {
}

sealed class Fruit permits Apple, Pear {
}

class Code {
    public static void main(String[] args) {
    }
}
