package com.vitorbetmann.cleanresmapi.usecases.user;

import com.vitorbetmann.cleanresmapi.entities.user.User;
import com.vitorbetmann.cleanresmapi.usecases.user.exceptions.UserNotFoundException;
import com.vitorbetmann.cleanresmapi.usecases.user.interfaces.UserGateway;

public class GetUser {

    private final UserGateway userGateway;

    public GetUser(UserGateway userGateway) {
        this.userGateway = userGateway;
    }

    public User execute(Long id) {
        return this.userGateway.getById(id).orElseThrow(
                () -> new UserNotFoundException("User ID not found: " + id)
        );
    }
}
