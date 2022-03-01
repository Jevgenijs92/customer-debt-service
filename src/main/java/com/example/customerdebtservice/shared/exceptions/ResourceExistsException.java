package com.example.customerdebtservice.shared.exceptions;

public class ResourceExistsException extends RuntimeException{
    public ResourceExistsException(String message) {
        super(message);
    }
}
