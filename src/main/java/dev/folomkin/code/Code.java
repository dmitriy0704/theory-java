package dev.folomkin.code;

import java.lang.ref.SoftReference;

public class Code {

    public static void main(String[] args) {
        SoftReference<StringBuilder> reference = new SoftReference<>(new StringBuilder());

    }
}
