package com.vitorbetmann.cleanresmapi.infrastructure.menuitem.dtos;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.math.BigDecimal;

public record CreateMenuItemRequest(
        @NotBlank String name,
        @NotBlank String description,
        @NotNull @Positive BigDecimal price,
        @NotNull Boolean availableOnlyAtRestaurant,
        @NotBlank String photoPath,
        @NotNull Long restaurantId
) {
}
