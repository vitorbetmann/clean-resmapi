package com.vitorbetmann.cleanresmapi.usecases.user.exceptions;

import com.vitorbetmann.cleanresmapi.usecases.exceptions.DuplicateFieldException;

public class UserDuplicateEmailException extends DuplicateFieldException {

    public UserDuplicateEmailException(String message) {
        super(message);
    }
}
