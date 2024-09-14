package dev.folomkin.core.oop_classType.class_type.records;

public class Code {
    // Использование традиционного оператора switch для получения способа
    // доставки товара с заданным идентификатором. Для большинства товаров
    // используется стандартная доставка, но некоторые требуют специальной
    // транспортировки,
    enum ShipMethod {
        STANDARD, TRUCK, AIR, OVERNIGHT
        // Стандартный, грузовой, самолетом, срочный
    }

    public static void main(String[] args) {
        int productID = 5099;
        boolean extraCharge;
        ShipMethod shipBy = switch (productID) {
            case 1774, 8708, 6709 -> {
                extraCharge = true;
                yield ShipMethod.TRUCK;
            }
            case 4657, 1887 -> {
                extraCharge = false;
                yield ShipMethod.AIR;
            }
            case 2907, 5099 -> {
                extraCharge = false;
                yield ShipMethod.OVERNIGHT;
            }
            default -> {
                extraCharge = false;
                yield ShipMethod.STANDARD;
            }
        };
        System.out.println("productID " + productID + " : shipBy " + shipBy);
        if (extraCharge) {
            System.out.println("productID " + productID + " : extraCharge");
        }
    }
}
