package com.vitorbetmann.cleanresmapi.usecases.usertype.exceptions;

import com.vitorbetmann.cleanresmapi.usecases.exceptions.NotFoundException;

public class UserTypeNotFoundException extends NotFoundException {

    public UserTypeNotFoundException(String msg) {
        super(msg);
    }

}
