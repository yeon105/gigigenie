package com.gigigenie.exception;

public class CustomJWTException extends RuntimeException {
    public CustomJWTException(String message) {
        super(message);
    }

    public static class OutOfStockException extends RuntimeException {
        public OutOfStockException(String message) {
            super(message);
        }
    }
}
