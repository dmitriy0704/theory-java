package dev.folomkin.core.oop.code;

class CardAction {
    public void doPayment(double amountPayment) {
        System.out.println("complete from debt card");
    }
}

class CreditCardAction extends CardAction {
    @Override
    public void doPayment(double amountPayment) { // override method
        System.out.println("complete from credit card");
    }

    public boolean checkCreditLimit() { // own method
        return true;
    }
}


public class Code {
    public static void main(String[] args) {
        // Объект по ссылке action1 создается при помощи вызова конструктора класса
        // CardAction и, соответственно, при вызове метода doPayment() вызывается
        // версия метода из класса CardAction.
        CardAction action1 = new CardAction();

        // При создании объекта action2 ссылка
        // типа CardAction инициализируется объектом типа CreditCardAction.
        CardAction action2 = new CreditCardAction();

        CreditCardAction cc = new CreditCardAction();
        // CreditCardAction cca = new CardAction(); // compile error: class cast
        action1.doPayment(15.5); // method of CardAction
        action2.doPayment(21.2); // polymorphic method: CreditCardAction
        // dc2.checkCreditLimit(); // compile error: non-polymorphic method
        ((CreditCardAction) action2).checkCreditLimit(); // ок
        cc.doPayment(7.0); // polymorphic method: CreditCardAction
        cc.checkCreditLimit(); // non-polymorphic method CreditCardAction
        ((CreditCardAction) action1).checkCreditLimit(); // runtime error: class cast
    }
}
