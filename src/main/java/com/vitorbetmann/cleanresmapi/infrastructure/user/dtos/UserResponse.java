package com.vitorbetmann.cleanresmapi.infrastructure.user.dtos;

import com.vitorbetmann.cleanresmapi.entities.user.User;

public record UserResponse(
        Long id,
        String name,
        String email,
        Long userTypeId,
        String address
) {

    public static UserResponse fromDomain(User user) {
        return new UserResponse(user.getId(), user.getName(), user.getEmail(), user.getUserType().getId(), user.getAddress());
    }
}
