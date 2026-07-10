package com.vitorbetmann.cleanresmapi.usecases.usertype;

import com.vitorbetmann.cleanresmapi.entities.usertype.UserType;

public class CreateUserType {

    private final UserTypeGateway userTypeGateway;

    public CreateUserType(UserTypeGateway userTypeGateway) {
        this.userTypeGateway = userTypeGateway;
    }

    public UserType execute(String name) {
        if (!this.userTypeGateway.isNameUnique(name)) {
            throw new IllegalArgumentException("Name already exists.");
        }
        var userType = UserType.create(name);
        return this.userTypeGateway.save(userType);
    }
}