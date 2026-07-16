package com.vitorbetmann.cleanresmapi.usecases.menuitem.exceptions;

import com.vitorbetmann.cleanresmapi.usecases.exceptions.NotFoundException;

public class MenuItemNotFoundException extends NotFoundException {

    public MenuItemNotFoundException(String message) {
        super(message);
    }

}
