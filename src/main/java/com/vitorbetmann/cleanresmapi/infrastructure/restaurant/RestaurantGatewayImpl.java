package com.vitorbetmann.cleanresmapi.infrastructure.restaurant;

import com.vitorbetmann.cleanresmapi.entities.restaurant.Restaurant;
import com.vitorbetmann.cleanresmapi.usecases.restaurant.interfaces.RestaurantGateway;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
public class RestaurantGatewayImpl implements RestaurantGateway {

    private final RestaurantRepository restaurantRepository;

    public RestaurantGatewayImpl(RestaurantRepository restaurantRepository) {
        this.restaurantRepository = restaurantRepository;
    }

    @Override
    public Restaurant save(Restaurant restaurant) {
        var entity = RestaurantEntity.fromDomain(restaurant);
        var savedEntity = this.restaurantRepository.save(entity);
        return savedEntity.toDomain();
    }

    @Override
    public Optional<Restaurant> getById(Long id) {
        return this.restaurantRepository.findById(id).map(RestaurantEntity::toDomain);
    }

    @Override
    public List<Restaurant> getAll() {
        return this.restaurantRepository.findAll()
                .stream()
                .map(RestaurantEntity::toDomain)
                .toList();
    }

    @Override
    public void delete(Long id) {
        this.restaurantRepository.deleteById(id);
    }
}
