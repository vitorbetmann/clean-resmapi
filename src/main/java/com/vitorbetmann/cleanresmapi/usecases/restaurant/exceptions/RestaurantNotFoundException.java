package com.vitorbetmann.cleanresmapi.usecases.restaurant.exceptions;

import com.vitorbetmann.cleanresmapi.usecases.exceptions.NotFoundException;

public class RestaurantNotFoundException extends NotFoundException {

    public RestaurantNotFoundException(String message) {
        super(message);
    }

}
