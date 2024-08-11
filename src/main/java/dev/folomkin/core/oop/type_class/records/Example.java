package dev.folomkin.core.oop.type_class.records;

record ImmutableRec(String name, int id) {
    void method() {
    }
}

public class Example {
    public static void main(String[] args) {
        ImmutableRec immutableRec = new ImmutableRec("immutable", 1);
        System.out.println(immutableRec.id());
        System.out.println(immutableRec.name());
    }
}
