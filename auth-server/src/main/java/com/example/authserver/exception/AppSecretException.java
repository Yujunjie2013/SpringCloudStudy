package com.example.authserver.exception;

public class AppSecretException extends RuntimeException{
    public AppSecretException(String message) {
        super(message);
    }
}
