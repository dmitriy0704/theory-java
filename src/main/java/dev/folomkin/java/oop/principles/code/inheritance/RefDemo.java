package dev.folomkin.java.oop.principles.code.inheritance;

public class RefDemo {
    public static void main(String[] args) {
        BoxWeight weightBox = new BoxWeight(3, 5, 7, 8);
        Box plainBox = new Box();
        double vol;

        vol = plainBox.volume();
        System.out.println("weightBox: " + vol);
        System.out.println("weightBox: " + weightBox.weight);
        System.out.println();

        plainBox = weightBox;

        vol = plainBox.volume();
        System.out.println("plainBox: " + vol);


    }
}
