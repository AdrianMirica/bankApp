package com.interview.bankApp.exception;

public class InvalidInputException extends Exception {

    public InvalidInputException() {}

    public InvalidInputException(String message) {
        super(message);
    }
}
