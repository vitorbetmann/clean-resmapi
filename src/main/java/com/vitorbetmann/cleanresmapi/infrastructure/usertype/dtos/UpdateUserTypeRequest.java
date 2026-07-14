package com.vitorbetmann.cleanresmapi.infrastructure.usertype.dtos;

import jakarta.validation.constraints.NotBlank;

public record UpdateUserTypeRequest(
        @NotBlank String name) {
}
