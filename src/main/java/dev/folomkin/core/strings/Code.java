package dev.folomkin.core.strings;

import java.util.StringJoiner;

public class Code {

    public static void main(String[ ] args) {

        StringJoiner joiner = new StringJoiner(":", "<<", ">>");
        String result = joiner.add("blanc").add("rouge").add("blanc").toString();
        System.out.println(result);


    }
}
