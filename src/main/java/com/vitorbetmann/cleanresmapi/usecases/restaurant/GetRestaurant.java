package com.vitorbetmann.cleanresmapi.usecases.restaurant;

import com.vitorbetmann.cleanresmapi.entities.restaurant.Restaurant;
import com.vitorbetmann.cleanresmapi.usecases.restaurant.exceptions.RestaurantNotFoundException;
import com.vitorbetmann.cleanresmapi.usecases.restaurant.interfaces.RestaurantGateway;

import java.util.Optional;

public class GetRestaurant {

    private final RestaurantGateway restaurantGateway;

    public GetRestaurant(RestaurantGateway restaurantGateway) {
        this.restaurantGateway = restaurantGateway;
    }

    public Restaurant execute(Long id) {
        Optional<Restaurant> restaurant = this.restaurantGateway.getById(id);
        if (restaurant.isEmpty()) {
            throw new RestaurantNotFoundException("Restaurant ID not found: " + id);
        }
        return restaurant.get();
    }
}
