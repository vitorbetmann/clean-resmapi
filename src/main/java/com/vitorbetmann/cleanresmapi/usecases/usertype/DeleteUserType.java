package com.vitorbetmann.cleanresmapi.usecases.usertype;

import com.vitorbetmann.cleanresmapi.usecases.usertype.exceptions.UserTypeNotFoundException;
import com.vitorbetmann.cleanresmapi.usecases.usertype.interfaces.UserTypeGateway;

public class DeleteUserType {
    private final UserTypeGateway userTypeGateway;

    public DeleteUserType(UserTypeGateway userTypeGateway) {
        this.userTypeGateway = userTypeGateway;
    }

    public void execute(Integer id) {
        var userType = this.userTypeGateway.getById(id);
        if (userType.isEmpty()) {
            throw new UserTypeNotFoundException("UserType ID not found: " + id);
        }
        this.userTypeGateway.delete(id);
    }
}
