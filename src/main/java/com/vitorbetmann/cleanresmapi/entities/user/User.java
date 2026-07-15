package com.vitorbetmann.cleanresmapi.entities.user;

import com.vitorbetmann.cleanresmapi.entities.usertype.UserType;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
public class User {

    private final Long id;
    private final String name;
    private final String email;
    private final String password;
    private final UserType userType;
    private final String address;

    @Setter
    private LocalDateTime lastModifiedDate;

    public User(Long id, String name, String email, String password, UserType userType, String address, LocalDateTime lastModifiedDate) {

        if (name == null || name.isBlank()) {
            throw new IllegalArgumentException("Name cannot be null or blank.");
        }

        if (email == null || email.isBlank()) {
            throw new IllegalArgumentException("Email cannot be null or blank.");
        }

        if (password == null || password.isBlank()) {
            throw new IllegalArgumentException("Password cannot be null or blank.");
        }

        if (userType == null) {
            throw new IllegalArgumentException("User Type cannot be null.");
        }

        if (address == null || address.isBlank()) {
            throw new IllegalArgumentException("Address cannot be null or blank.");
        }

        this.id = id;
        this.name = name;
        this.email = email;
        this.password = password;
        this.userType = userType;
        this.address = address;
        this.lastModifiedDate = lastModifiedDate;
    }

    public static User create(String name, String email, String password, UserType userType, String address) {
        return new User(null, name, email, password, userType, address, LocalDateTime.now());
    }
}
