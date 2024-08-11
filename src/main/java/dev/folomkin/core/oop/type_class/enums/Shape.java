package dev.folomkin.core.oop.type_class.enums;

enum Shape {
    RECTANGLE, TRIANGLE, CIRCLE;

    public double defineShape(double... x) {
        double area = switch (this) {
            case RECTANGLE -> x[0] * x[1];
            case TRIANGLE -> x[0] * x[1] / 2;
            case CIRCLE -> Math.pow(x[0], 2) + Math.PI;
            default ->
                    throw new EnumConstantNotPresentException(getDeclaringClass(), name());
        };
        return area;
    }
}

class ShapeMain {
    public static void main(String[] args) {
        double x = 2, y = 3;
        Shape sh;
        sh = Shape.RECTANGLE;
        System.out.printf("%9s = %5.2f%n", sh, sh.defineShape(x, y));
        sh = Shape.valueOf(Shape.class, "TRIANGLE");
        System.out.printf("%9s = %5.2f%n", sh, sh.defineShape (x, y));
        sh = Shape.CIRCLE;
        System.out.printf("%9s = %5.2f%n", sh, sh.defineShape (x));
    }
}
