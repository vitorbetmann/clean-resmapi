package com.vitorbetmann.cleanresmapi.usecases.user.exceptions;

import com.vitorbetmann.cleanresmapi.usecases.exceptions.NotFoundException;

public class UserNotFoundException extends NotFoundException {

    public UserNotFoundException(String message) {
        super(message);
    }
}
