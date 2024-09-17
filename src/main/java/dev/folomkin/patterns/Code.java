package dev.folomkin.patterns;


class Probe {
    // Instance variables
    // Important methods

    // Конструктор приватный, чтобы не создавалось несколько объектов
    private Probe() {
        // Initialize variables here
    }

    //Каждый раз, когда вызывается этот метод, он возвращает один и тот же
    // объект. Таким образом, шаблон способен блокировать создание нескольких
    // объектов.
    private static Probe getInstance(Probe probe) {
        if (probe == null)
            probe = new Probe();
        return probe;
    }
}

public class Code {
}