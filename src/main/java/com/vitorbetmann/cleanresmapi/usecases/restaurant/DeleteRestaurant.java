package com.vitorbetmann.cleanresmapi.usecases.restaurant;

import com.vitorbetmann.cleanresmapi.usecases.restaurant.exceptions.RestaurantNotFoundException;
import com.vitorbetmann.cleanresmapi.usecases.restaurant.interfaces.RestaurantGateway;

public class DeleteRestaurant {

    private final RestaurantGateway restaurantGateway;

    public DeleteRestaurant(RestaurantGateway restaurantGateway) {
        this.restaurantGateway = restaurantGateway;
    }

    public void execute(Long id) {
        var restaurant = this.restaurantGateway.getById(id);
        if (restaurant.isEmpty()) {
            throw new RestaurantNotFoundException("Restaurant ID not found: " + id);
        }
        this.restaurantGateway.delete(id);
    }
}
