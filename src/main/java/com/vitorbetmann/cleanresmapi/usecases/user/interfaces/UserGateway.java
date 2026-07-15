package com.vitorbetmann.cleanresmapi.usecases.user.interfaces;

import com.vitorbetmann.cleanresmapi.entities.user.User;

import java.util.List;
import java.util.Optional;

public interface UserGateway {

    User save(User user);

    Optional<User> getById(Long id);

    List<User> getAll();

    void delete(Long id);

    boolean isEmailUnique(String email);
}
