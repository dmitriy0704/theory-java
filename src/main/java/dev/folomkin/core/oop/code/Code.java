package dev.folomkin.core.oop.code;

import java.util.function.BiFunction;

class Letter {
    private String salutation;
    private String body;

    public Letter(String salutation, String body) {
        this.salutation = salutation;
        this.body = body;
    }

    public String getSalutation() {
        return salutation;
    }

    public void setSalutation(String salutation) {
        this.salutation = salutation;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    @Override
    public String toString() {
        return "Letter{" +
                "salutation='" + salutation + '\'' +
                ", body='" + body + '\'' +
                '}';
    }


}

class Code {
    public static void main(String[] args) {
        Letter letter = createLetter("Title", "BodyLetter");
        System.out.println(letter);

        BiFunction<String, String, Letter> LETTER_CREATOR = Letter::new;
    }

    static Letter createLetter(String salutation, String body) {
        return new Letter(salutation, body);
    }
}
