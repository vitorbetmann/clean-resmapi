package com.vitorbetmann.cleanresmapi.usecases.usertype;

import com.vitorbetmann.cleanresmapi.entities.usertype.UserType;
import com.vitorbetmann.cleanresmapi.usecases.usertype.exceptions.UserTypeNotFoundException;
import com.vitorbetmann.cleanresmapi.usecases.usertype.interfaces.UserTypeGateway;

import java.util.Optional;

public class GetUserType {

    private final UserTypeGateway userTypeGateway;


    public GetUserType(UserTypeGateway userTypeGateway) {
        this.userTypeGateway = userTypeGateway;
    }

    public UserType execute(Integer id) {
        Optional<UserType> userType = this.userTypeGateway.getById(id);
        if (userType.isEmpty()) {
            throw new UserTypeNotFoundException("UserType ID not found: " + id);
        }
        return userType.get();
    }
}
