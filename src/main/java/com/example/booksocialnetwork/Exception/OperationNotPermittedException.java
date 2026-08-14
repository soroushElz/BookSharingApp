package com.example.booksocialnetwork.Exception;

public class OperationNotPermittedException extends RuntimeException {

    public OperationNotPermittedException(String message){
        super(message);
    }
}
