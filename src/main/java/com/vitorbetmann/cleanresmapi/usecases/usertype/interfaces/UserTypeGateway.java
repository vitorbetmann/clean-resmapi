package com.vitorbetmann.cleanresmapi.usecases.usertype.interfaces;

import com.vitorbetmann.cleanresmapi.entities.usertype.UserType;

import java.util.List;
import java.util.Optional;

public interface UserTypeGateway {

    UserType save(UserType userType);

    Optional<UserType> getById(Integer id);

    List<UserType> getAll();

    void delete(Integer id);

    boolean isNameUnique(String name);

}
