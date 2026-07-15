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

    public User execute(User user) {
        if (!this.userGateway.isEmailUnique(user.getEmail())) {
            throw new UserDuplicateEmailException("User email already in use: " + user.getEmail());
        }

        var userType = this.userTypeGateway.getById(user.getUserType().getId()).orElseThrow(
                () -> new UserTypeNotFoundException("UserType not found: " + user.getUserType().getName())
        );

        var result = User.create(user.getName(), user.getEmail(), user.getPassword(), userType, user.getAddress());
        return this.userGateway.save(result);
    }
}
