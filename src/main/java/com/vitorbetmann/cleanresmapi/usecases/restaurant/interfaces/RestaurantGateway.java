package com.vitorbetmann.cleanresmapi.usecases.restaurant.interfaces;

import com.vitorbetmann.cleanresmapi.entities.restaurant.Restaurant;

import java.util.List;
import java.util.Optional;

public interface RestaurantGateway {

    Restaurant save(Restaurant restaurant);

    Optional<Restaurant> getById(Long id);

    List<Restaurant> getAll();

    void delete(Long id);

}
