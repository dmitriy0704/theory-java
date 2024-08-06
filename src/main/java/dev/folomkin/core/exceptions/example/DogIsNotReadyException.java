package dev.folomkin.core.exceptions.example;

public class DogIsNotReadyException extends Exception {
    public DogIsNotReadyException(String message) {
        super(message);
    }
}
