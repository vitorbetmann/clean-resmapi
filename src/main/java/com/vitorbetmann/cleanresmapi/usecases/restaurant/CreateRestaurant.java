package com.vitorbetmann.cleanresmapi.usecases.restaurant;

import com.vitorbetmann.cleanresmapi.entities.restaurant.Restaurant;
import com.vitorbetmann.cleanresmapi.usecases.restaurant.interfaces.RestaurantGateway;
import com.vitorbetmann.cleanresmapi.usecases.user.exceptions.UserNotFoundException;
import com.vitorbetmann.cleanresmapi.usecases.user.interfaces.UserGateway;

public class CreateRestaurant {

    private final RestaurantGateway restaurantGateway;
    private final UserGateway userGateway;

    public CreateRestaurant(RestaurantGateway restaurantGateway, UserGateway userGateway) {
        this.restaurantGateway = restaurantGateway;
        this.userGateway = userGateway;
    }

    public Restaurant execute(String name, String address, String cuisineType, String openingHours, Long ownerId) {
        var owner = this.userGateway.getById(ownerId).orElseThrow(
                () -> new UserNotFoundException("User ID not found: " + ownerId)
        );

        var restaurant = Restaurant.create(name, address, cuisineType, openingHours, owner);
        return this.restaurantGateway.save(restaurant);
    }
}
