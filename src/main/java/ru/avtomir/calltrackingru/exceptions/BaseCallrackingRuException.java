package ru.avtomir.calltrackingru.exceptions;

public class BaseCallrackingRuException extends RuntimeException {

    public BaseCallrackingRuException(String message) {
        super(message);
    }

    public BaseCallrackingRuException(String message, Throwable cause) {
        super(message, cause);
    }
}
