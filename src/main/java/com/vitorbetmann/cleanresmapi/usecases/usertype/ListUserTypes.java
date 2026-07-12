package com.vitorbetmann.cleanresmapi.usecases.usertype;

import com.vitorbetmann.cleanresmapi.entities.usertype.UserType;
import com.vitorbetmann.cleanresmapi.usecases.usertype.interfaces.UserTypeGateway;

import java.util.List;

public class ListUserTypes {

    private final UserTypeGateway userTypeGateway;

    public ListUserTypes(UserTypeGateway userTypeGateway) {
        this.userTypeGateway = userTypeGateway;
    }

    public List<UserType> execute() {
        return this.userTypeGateway.getAll();
    }
}
