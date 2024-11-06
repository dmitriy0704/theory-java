package dev.folomkin.code;

import java.util.HashMap;
import java.util.Map;

public class Code {
    public static void main(String[] args) {
        Map<String, String> map = new HashMap<>();
        map.put("foo", "bar");
        map.put("bar", "baz");
        map.put("baz", "qux");

        for (String key : map.keySet()) {
            System.out.println(key + ": " + map.get(key));
        }
    }
}
