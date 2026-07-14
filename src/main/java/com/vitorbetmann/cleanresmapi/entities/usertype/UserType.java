package com.vitorbetmann.cleanresmapi.entities.usertype;

import lombok.Getter;

@Getter
public class UserType {

    private final Long id;
    private final String name;

    public UserType(Long id, String name) {

        if (name == null || name.isBlank()) {
            throw new IllegalArgumentException("Name cannot be null or blank.");
        }

        this.id = id;
        this.name = name;
    }

    public static UserType create(String name) {
        return new UserType(null, name);
    }

}
