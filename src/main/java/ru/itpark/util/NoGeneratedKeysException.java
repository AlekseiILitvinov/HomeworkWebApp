package ru.itpark.util;

public class NoGeneratedKeysException extends RuntimeException {
    public NoGeneratedKeysException() {
    }

    public NoGeneratedKeysException(String message) {
        super(message);
    }

    public NoGeneratedKeysException(String message, Throwable cause) {
        super(message, cause);
    }

    public NoGeneratedKeysException(Throwable cause) {
        super(cause);
    }

    public NoGeneratedKeysException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
