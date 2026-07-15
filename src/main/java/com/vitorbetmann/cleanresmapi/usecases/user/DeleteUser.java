package com.vitorbetmann.cleanresmapi.usecases.user;

import com.vitorbetmann.cleanresmapi.usecases.user.exceptions.UserNotFoundException;
import com.vitorbetmann.cleanresmapi.usecases.user.interfaces.UserGateway;

public class DeleteUser {

    private final UserGateway userGateway;

    public DeleteUser(UserGateway userGateway) {
        this.userGateway = userGateway;
    }

    public void execute(Long id) {
        this.userGateway.getById(id).orElseThrow(
                () -> new UserNotFoundException("User ID not found: " + id)
        );
        this.userGateway.delete(id);
    }

}
