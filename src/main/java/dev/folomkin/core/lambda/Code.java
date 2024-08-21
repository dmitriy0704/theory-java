package dev.folomkin.core.lambda;


interface MyValue {
    double getValue();
}

public class Code {
    public static void main(String[] args) {
        MyValue myValue = () -> 98.6;
    }
}
