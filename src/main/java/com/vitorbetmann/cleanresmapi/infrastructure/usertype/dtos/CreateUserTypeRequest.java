package com.vitorbetmann.cleanresmapi.infrastructure.usertype.dtos;

import jakarta.validation.constraints.NotBlank;

public record CreateUserTypeRequest(
        @NotBlank String name) {
}
