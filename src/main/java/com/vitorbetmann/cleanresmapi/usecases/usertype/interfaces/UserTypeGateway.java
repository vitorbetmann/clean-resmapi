package com.vitorbetmann.cleanresmapi.usecases.usertype.interfaces;

import com.vitorbetmann.cleanresmapi.entities.usertype.UserType;

import java.util.List;
import java.util.Optional;

public interface UserTypeGateway {

    UserType save(UserType userType);

    Optional<UserType> getById(Long id);

    List<UserType> getAll();

    void delete(Long id);

    boolean isNameUnique(String name);

}
