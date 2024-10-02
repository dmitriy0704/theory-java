package dev.folomkin.design_patterns.patterns;


class Coffee {
    public void grindCoffee(){
        // перемалываем кофе
    }
    public void makeCoffee(){
        // делаем кофе
    }
    public void pourIntoCup(){
        // наливаем в чашку
    }
}

class Americano extends Coffee {}
class Cappuccino extends Coffee {}
class CaffeLatte extends Coffee {}
class Espresso extends Coffee {}

public class Code {
    public static void main(String[] args) {

    }
}
