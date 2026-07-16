package com.vitorbetmann.cleanresmapi.usecases.restaurant;

import com.vitorbetmann.cleanresmapi.entities.restaurant.Restaurant;
import com.vitorbetmann.cleanresmapi.usecases.restaurant.exceptions.RestaurantNotFoundException;
import com.vitorbetmann.cleanresmapi.usecases.restaurant.interfaces.RestaurantGateway;
import com.vitorbetmann.cleanresmapi.usecases.user.exceptions.UserNotFoundException;
import com.vitorbetmann.cleanresmapi.usecases.user.interfaces.UserGateway;

public class UpdateRestaurant {

    private final RestaurantGateway restaurantGateway;
    private final UserGateway userGateway;

    public UpdateRestaurant(RestaurantGateway restaurantGateway, UserGateway userGateway) {
        this.restaurantGateway = restaurantGateway;
        this.userGateway = userGateway;
    }

    public Restaurant execute(Long id, String name, String address, String cuisineType, String openingHours, Long ownerId) {
        this.restaurantGateway.getById(id).orElseThrow(
                () -> new RestaurantNotFoundException("Restaurant ID not found: " + id)
        );

        var owner = this.userGateway.getById(ownerId).orElseThrow(
                () -> new UserNotFoundException("User ID not found: " + ownerId)
        );

        var updatedRestaurant = new Restaurant(id, name, address, cuisineType, openingHours, owner);
        return this.restaurantGateway.save(updatedRestaurant);
    }
}
