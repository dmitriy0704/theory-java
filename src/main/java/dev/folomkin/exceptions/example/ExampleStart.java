package dev.folomkin.exceptions.example;

class ExceptionExample {
    public static void example(int what) {
        int t;
        int[] nums = new int[2];
        System.out.println("Получено " + what);
        try {
            switch (what) {
                case 0:
                    t = 10 / what;
                    break;
                case 1:
                    nums[4] = 4;
                    break;
                case 2:
                    return;
            }

        } catch (ArithmeticException e) {
            System.out.println("Делить на ноль нальзя");
            return;
        } catch (ArrayIndexOutOfBoundsException e) {
            System.out.println("Соответствующий элемент не найден");
        } finally {
            System.out.println("Выход из try");
        }


    }
}

class ExampleStart {
    public static void main(String[] args) throws Exception {
        for (int i = 0; i < 3; i++) {
            ExceptionExample.example(i);
            System.out.println();
        }
    }
}


