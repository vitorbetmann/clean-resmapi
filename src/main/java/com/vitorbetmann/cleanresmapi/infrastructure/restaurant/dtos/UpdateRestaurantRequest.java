package com.vitorbetmann.cleanresmapi.infrastructure.restaurant.dtos;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record UpdateRestaurantRequest(
        @NotBlank String name,
        @NotBlank String address,
        @NotBlank String cuisineType,
        @NotBlank String openingHours,
        @NotNull Long ownerId
) {
}
