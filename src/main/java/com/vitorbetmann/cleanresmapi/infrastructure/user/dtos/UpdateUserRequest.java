package com.vitorbetmann.cleanresmapi.infrastructure.user.dtos;

import com.vitorbetmann.cleanresmapi.entities.usertype.UserType;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record UpdateUserRequest(
        @NotBlank String name,
        @NotBlank @Email String email,
        @NotBlank String password,
        @NotNull Long userTypeId,
        @NotBlank String address
) {
}
