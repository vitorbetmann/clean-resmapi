package com.vitorbetmann.cleanresmapi.usecases.usertype.exceptions;

import com.vitorbetmann.cleanresmapi.usecases.exceptions.DuplicateFieldException;

public class UserTypeDuplicateNameException extends DuplicateFieldException {

    public UserTypeDuplicateNameException(String msg) {
        super(msg);
    }
}
