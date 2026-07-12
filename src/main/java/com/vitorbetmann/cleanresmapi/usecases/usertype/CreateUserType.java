package com.vitorbetmann.cleanresmapi.usecases.usertype;

import com.vitorbetmann.cleanresmapi.entities.usertype.UserType;
import com.vitorbetmann.cleanresmapi.usecases.usertype.exceptions.UserTypeDuplicateNameException;
import com.vitorbetmann.cleanresmapi.usecases.usertype.interfaces.UserTypeGateway;

public class CreateUserType {

    private final UserTypeGateway userTypeGateway;

    public CreateUserType(UserTypeGateway userTypeGateway) {
        this.userTypeGateway = userTypeGateway;
    }

    public UserType execute(String name) {
        if (!this.userTypeGateway.isNameUnique(name)) {
            throw new UserTypeDuplicateNameException("UserType name already exists: " + name);
        }
        var userType = UserType.create(name);
        return this.userTypeGateway.save(userType);
    }
}