package com.vitorbetmann.cleanresmapi.usecases.usertype.exceptions;

import com.vitorbetmann.cleanresmapi.usecases.exceptions.DuplicateNameException;

public class UserTypeDuplicateNameException extends DuplicateNameException {

    public UserTypeDuplicateNameException(String msg) {
        super(msg);
    }
}
