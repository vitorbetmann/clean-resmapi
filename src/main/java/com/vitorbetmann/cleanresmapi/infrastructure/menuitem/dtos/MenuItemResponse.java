package com.vitorbetmann.cleanresmapi.infrastructure.menuitem.dtos;

import com.vitorbetmann.cleanresmapi.entities.menuitem.MenuItem;

import java.math.BigDecimal;

public record MenuItemResponse(
        Long id,
        String name,
        String description,
        BigDecimal price,
        boolean availableOnlyAtRestaurant,
        String photoPath,
        Long restaurantId
) {

    public static MenuItemResponse fromDomain(MenuItem menuItem) {
        return new MenuItemResponse(menuItem.getId(), menuItem.getName(), menuItem.getDescription(), menuItem.getPrice(), menuItem.isAvailableOnlyAtRestaurant(), menuItem.getPhotoPath(), menuItem.getRestaurant().getId());
    }
}
