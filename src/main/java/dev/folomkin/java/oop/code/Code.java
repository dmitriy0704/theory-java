package dev.folomkin.java.oop.code;


class Code {
    public static void main(String[] args) {
        Color color = Color.GREEN;
        Color color2 = Color.BLUE;
        System.out.println(color == color2);
        System.out.println(
                Color.GREEN.ordinal()==color2.ordinal()
        );
    }
}

enum Color {
    RED, BLACK, WHITE, BLUE, GREEN, YELLOW, ORANGE;
}