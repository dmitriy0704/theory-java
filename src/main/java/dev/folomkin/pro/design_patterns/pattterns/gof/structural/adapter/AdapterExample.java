package dev.folomkin.pro.design_patterns.pattterns.gof.structural.adapter;


// ../round

// ../round/RoundHole.java: Круглое отверстие

/**
 * КруглоеОтверстие совместимо с КруглымиКолышками.
 * Классы с совместимыми интерфейсами: КруглоеОтверстие и
 */
class RoundHole {
    private double radius;

    public RoundHole(double radius) {
        this.radius = radius;
    }

    // Вернуть радиус отверстия.
    public double getRadius() {
        return radius;
    }

    public boolean fits(RoundPeg peg) {
        boolean result;
        result = (this.getRadius() >= peg.getRadius());
        return result;
    }
}

//  ../round/RoundPeg.java: Круглый колышек
class RoundPeg {
    /**
     * КруглыеКолышки совместимы с КруглымиОтверстиями.
     */
    private double radius;

    public RoundPeg() {
    }

    public RoundPeg(double radius) {
        this.radius = radius;

    }

    // Вернуть радиус круглого колышка.
    public double getRadius() {
        return radius;
    }

}


// ../square
// ../square/SquarePeg.java: Квадратный колышек

/**
 * КвадратныеКолышки несовместимы с КруглымиОтверстиями (они остались в проекте
 * после бывших разработчиков). Но мы должны как-то интегрировать их в нашу
 * систему. Устаревший, несовместимый класс: КвадратныйКолышек.
 */

class SquarePeg {
    private double width;

    public SquarePeg(double width) {
        this.width = width;
    }

    // Вернуть ширину квадратного колышка.
    public double getWidth() {
        return width;
    }

    public double getSquare() {
        double result;
        result = Math.pow(this.width, 2);
        return result;
    }
}

// ../adapters
// ../adapters/SquarePegAdapter.java: Адаптер квадратных колышков к круглым отверстиям

/**
 * Адаптер позволяет использовать КвадратныеКолышки и КруглыеОтверстия вместе.
 */

class SquarePegAdapter extends RoundPeg {
    private SquarePeg peg;

    public SquarePegAdapter(SquarePeg peg) {
        this.peg = peg;
    }

    @Override
    public double getRadius() {
        double result;
        // Рассчитываем минимальный радиус, в который пролезет этот колышек.
        // Вычислить половину диагонали квадратного колышка по
        // теореме Пифагора.
        result = (Math.sqrt(Math.pow((peg.getWidth() / 2), 2) * 2));
        return result;
    }
}


// Клиентский код

public class AdapterExample {
    public static void main(String[] args) {
        // Круглое к круглому — всё работает.
        RoundHole hole = new RoundHole(5);
        RoundPeg rpeg = new RoundPeg(5);
        if (hole.fits(rpeg)) {
            System.out.println("Round peg r5 fits round hole r5.");
        }

        SquarePeg smallSqPeg = new SquarePeg(2);
        SquarePeg largeSqPeg = new SquarePeg(20);
        // hole.fits(smallSqPeg); // Не скомпилируется.

        // Адаптер решит проблему.
        SquarePegAdapter smallSqPegAdapter = new SquarePegAdapter(smallSqPeg);
        SquarePegAdapter largeSqPegAdapter = new SquarePegAdapter(largeSqPeg);
        if (hole.fits(smallSqPegAdapter)) {
            System.out.println("Square peg w2 fits round hole r5.");
        }
        if (!hole.fits(largeSqPegAdapter)) {
            System.out.println("Square peg w20 does not fit into round hole r5.");
        }
    }
}
