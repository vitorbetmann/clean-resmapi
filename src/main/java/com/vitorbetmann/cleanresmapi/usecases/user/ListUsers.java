package com.vitorbetmann.cleanresmapi.usecases.user;

import com.vitorbetmann.cleanresmapi.entities.user.User;
import com.vitorbetmann.cleanresmapi.usecases.user.interfaces.UserGateway;

import java.util.List;

public class ListUsers {

    private final UserGateway userGateway;

    public ListUsers(UserGateway userGateway) {
        this.userGateway = userGateway;
    }

    public List<User> execute() {
        return this.userGateway.getAll();
    }
}
