package dev.folomkin.core.oop.enums;

enum Apples {
    Jonathan(10), GoldenDel(20), RedDel(30), Winesap(40), Cortland(50);

    private int price;

    Apples(int p) {
        price = p;
    }
    public int getPrice() {
        return price;
    }
}

class EnumDemo {
    public static void main(String[] args) {
        Apples apple;
        apple = Apples.RedDel;
        System.out.println("Apple: " + apple);
        System.out.println();
        apple = Apples.GoldenDel;
        if (apple == Apples.GoldenDel) {
            System.out.println("ap содержит GoldenDel");
        }
        int price = apple.RedDel.getPrice();
        System.out.println("Apple price: " + price);
    }
}