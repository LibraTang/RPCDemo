package com.example.rpcdemo.exception;

public class MethodNotFoundException extends Exception {
    public MethodNotFoundException(String message) {
        super(message);
    }
}
