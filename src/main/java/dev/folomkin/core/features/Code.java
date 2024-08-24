package dev.folomkin.core.features;

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
        ShipMethod shipBy = switch (productID) {
            case 1774, 8708, 6709:
                yield ShipMethod.TRUCK;
            case 4657, 1887:
                yield ShipMethod.AIR;
            case 2907, 5099:
                yield ShipMethod.OVERNIGHT;
            default:
                yield ShipMethod.STANDARD;
        };
        System.out.println("productID " + productID + " : shipBy " + shipBy);
    }
}
