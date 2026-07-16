package com.vitorbetmann.cleanresmapi.infrastructure.restaurant.dtos;

import com.vitorbetmann.cleanresmapi.entities.restaurant.Restaurant;

public record RestaurantResponse(
        Long id,
        String name,
        String address,
        String cuisineType,
        String openingHours,
        Long ownerId
) {

    public static RestaurantResponse fromDomain(Restaurant restaurant) {
        return new RestaurantResponse(restaurant.getId(), restaurant.getName(), restaurant.getAddress(), restaurant.getCuisineType(), restaurant.getOpeningHours(), restaurant.getOwner().getId());
    }
}
