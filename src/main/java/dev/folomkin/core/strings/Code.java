package dev.folomkin.core.strings;

public class Code {

    public static void main(String[] args) {

        String s1 = "Java16";
        String s2 = "Ja" + "va" + "17";
        String s3 = new String("Java16");
        String s4 = new String(s1);

        System.out.println(s1 + "==" + s2 + " : " + (s1 == s2)); // true
        System.out.println(s3 + "==" + s4 + " : " + (s3 == s4)); // false
        System.out.println(s1 + "==" + s3 + " : " + (s1 == s3)); // false
        System.out.println(s1 + "==" + s4 + " : " + (s1 == s4)); // false
        System.out.println(s1 + " equals " + s2 + " : " + s1.equals(s2)); // true
        System.out.println(s1 + " equals " + s3 + " : " + s1.equals(s3)); // true

    }
}
