package com.vitorbetmann.cleanresmapi.infrastructure.usertype.dtos;

import com.vitorbetmann.cleanresmapi.entities.usertype.UserType;

public record UserTypeResponse(
        Long id,
        String name
) {

    public static UserTypeResponse fromDomain(UserType userType) {
        return new UserTypeResponse(userType.getId(), userType.getName());
    }
}
