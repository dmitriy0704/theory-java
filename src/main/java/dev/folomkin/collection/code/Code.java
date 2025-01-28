package dev.folomkin.collection.code;

import java.util.*;

public class Code {
    public static void main(String[] args) {

        Hashtable<String, Double> balance = new Hashtable<String, Double>();
        Enumeration<String> names;
        String str;
        double bal;
        balance.put("John Doe", 3434.34);
        balance.put("Tom Smith", 123.22);
        balance.put("Jane Baker", 1378.00);
        balance.put("Tod Hall", 99.22);
        balance.put("Ralph Smith", -19.08);

        // Отобразить балансы всех счетов иэ хеш-таблицы.

        Set<String> keys = balance.keySet();
        Iterator<String> iterator = keys.iterator();
        while (iterator.hasNext()) {
            str = iterator.next();
            System.out.println(str + ": " + balance.get(str));
        }
        System.out.println();
        bal = balance.get("John Doe");
        balance.put("John Smith", bal + 1000);
        System.out.println(bal);

    }
}
