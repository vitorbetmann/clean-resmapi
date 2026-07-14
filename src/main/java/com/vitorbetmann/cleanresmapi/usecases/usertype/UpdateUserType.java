package com.vitorbetmann.cleanresmapi.usecases.usertype;

import com.vitorbetmann.cleanresmapi.entities.usertype.UserType;
import com.vitorbetmann.cleanresmapi.usecases.usertype.exceptions.UserTypeDuplicateNameException;
import com.vitorbetmann.cleanresmapi.usecases.usertype.exceptions.UserTypeNotFoundException;
import com.vitorbetmann.cleanresmapi.usecases.usertype.interfaces.UserTypeGateway;

public class UpdateUserType {

    private final UserTypeGateway userTypeGateway;

    public UpdateUserType(UserTypeGateway userTypeGateway) {
        this.userTypeGateway = userTypeGateway;
    }

    public UserType execute(Long id, String name) {
        var userType = this.userTypeGateway.getById(id);
        if (userType.isEmpty()) {
            throw new UserTypeNotFoundException("UserType ID not found: " + id);
        }
        if (userType.get().getName().equals(name)) {
            return userType.get();
        }
        if (!this.userTypeGateway.isNameUnique(name)) {
            throw new UserTypeDuplicateNameException("UserType name already exists: " + name);
        }

        var updatedUserType = new UserType(id, name);
        return this.userTypeGateway.save(updatedUserType);
    }
}
