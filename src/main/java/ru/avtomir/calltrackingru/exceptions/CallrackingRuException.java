package ru.avtomir.calltrackingru.exceptions;

public class CallrackingRuException extends RuntimeException {

    public CallrackingRuException(String message) {
        super(message);
    }

    public CallrackingRuException(String message, Throwable cause) {
        super(message, cause);
    }
}
