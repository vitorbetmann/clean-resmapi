package com.vitorbetmann.cleanresmapi.usecases.user;

import com.vitorbetmann.cleanresmapi.entities.user.User;
import com.vitorbetmann.cleanresmapi.usecases.user.interfaces.UserGateway;
import com.vitorbetmann.cleanresmapi.usecases.user.exceptions.UserDuplicateEmailException;
import com.vitorbetmann.cleanresmapi.usecases.usertype.exceptions.UserTypeNotFoundException;
import com.vitorbetmann.cleanresmapi.usecases.usertype.interfaces.UserTypeGateway;

public class CreateUser {

    private final UserGateway userGateway;
    private final UserTypeGateway userTypeGateway;

    public CreateUser(UserGateway userGateway, UserTypeGateway userTypeGateway) {
        this.userGateway = userGateway;
        this.userTypeGateway = userTypeGateway;
    }

    public User execute(String name, String email, String password, Long userTypeId, String address) {
        if (!this.userGateway.isEmailUnique(email)) {
            throw new UserDuplicateEmailException("User email already in use: " + email);
        }

        var userType = this.userTypeGateway.getById(userTypeId).orElseThrow(
                () -> new UserTypeNotFoundException("UserType ID not found: " + userTypeId)
        );

        var result = User.create(name, email, password, userType, address);
        return this.userGateway.save(result);
    }
}
