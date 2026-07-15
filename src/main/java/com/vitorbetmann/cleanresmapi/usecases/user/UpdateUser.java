package com.vitorbetmann.cleanresmapi.usecases.user;

import com.vitorbetmann.cleanresmapi.entities.user.User;
import com.vitorbetmann.cleanresmapi.entities.usertype.UserType;
import com.vitorbetmann.cleanresmapi.usecases.user.exceptions.UserDuplicateEmailException;
import com.vitorbetmann.cleanresmapi.usecases.user.exceptions.UserNotFoundException;
import com.vitorbetmann.cleanresmapi.usecases.user.interfaces.UserGateway;
import com.vitorbetmann.cleanresmapi.usecases.usertype.exceptions.UserTypeNotFoundException;
import com.vitorbetmann.cleanresmapi.usecases.usertype.interfaces.UserTypeGateway;

import java.time.LocalDateTime;

public class UpdateUser {

    private final UserGateway userGateway;
    private final UserTypeGateway userTypeGateway;

    public UpdateUser(UserGateway userGateway, UserTypeGateway userTypeGateway) {
        this.userGateway = userGateway;
        this.userTypeGateway = userTypeGateway;
    }

    public User execute(Long id, String name, String email, String password, UserType userType, String address) {
        var user = this.userGateway.getById(id).orElseThrow(
                () -> new UserNotFoundException("User ID not found: " + id)
        );

        if (!user.getEmail().equals(email) && !this.userGateway.isEmailUnique(email)) {
            throw new UserDuplicateEmailException("User email already in use: " + email);
        }

        var userTypeToSave = this.userTypeGateway.getById(userType.getId()).orElseThrow(
                () -> new UserTypeNotFoundException("UserType not found: " + userType.getName())
        );

        var result = new User(id, name, email, password, userTypeToSave, address, LocalDateTime.now());
        return this.userGateway.save(result);
    }
}
