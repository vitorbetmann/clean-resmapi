package com.vitorbetmann.cleanresmapi.usecases.exceptions;

public class DuplicateFieldException extends RuntimeException {

    public DuplicateFieldException(String msg) {
        super(msg);
    }
}
