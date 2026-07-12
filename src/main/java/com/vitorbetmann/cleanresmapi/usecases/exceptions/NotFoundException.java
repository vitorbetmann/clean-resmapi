package com.vitorbetmann.cleanresmapi.usecases.exceptions;

public class NotFoundException extends RuntimeException {

    public NotFoundException(String msg) {
        super(msg);
    }

}
