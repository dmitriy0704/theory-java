package dev.folomkin.oop.classes_objects.example.expkg1;

public class ExClass0 {
    private long OrderId;
    static int bonus;

    static void doSomething(String str) {
        System.out.println(str);
    }

    public ExClass0(long orderId) {
        this.OrderId = orderId;
    }

    public long getOrderId() {
        return OrderId;
    }

    public void setOrderId(long orderId) {
        OrderId = orderId;
    }
}
