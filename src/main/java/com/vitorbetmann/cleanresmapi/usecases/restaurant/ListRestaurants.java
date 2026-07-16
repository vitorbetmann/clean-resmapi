package com.vitorbetmann.cleanresmapi.usecases.restaurant;

import com.vitorbetmann.cleanresmapi.entities.restaurant.Restaurant;
import com.vitorbetmann.cleanresmapi.usecases.restaurant.interfaces.RestaurantGateway;

import java.util.List;

public class ListRestaurants {

    private final RestaurantGateway restaurantGateway;

    public ListRestaurants(RestaurantGateway restaurantGateway) {
        this.restaurantGateway = restaurantGateway;
    }

    public List<Restaurant> execute() {
        return this.restaurantGateway.getAll();
    }
}
