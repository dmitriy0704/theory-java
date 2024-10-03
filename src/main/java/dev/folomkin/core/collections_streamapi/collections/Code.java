package dev.folomkin.core.collections_streamapi.collections;

import java.util.*;

public class Code {
    public static void main(String[] args) {
        final List<String> names = new ArrayList<>(List.of("Java", "Kotlin", "PHP"));
        final ListIterator<String> iterator = names.listIterator();

        while (iterator.hasNext()) {
            final String name = iterator.next();
            if ("Java".equals(name)) {
                iterator.set("JS");
            }

        }

        System.out.println(names);
    }
}