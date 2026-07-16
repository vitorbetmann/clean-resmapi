package com.vitorbetmann.cleanresmapi.infrastructure.restaurant;

import com.vitorbetmann.cleanresmapi.infrastructure.restaurant.dtos.CreateRestaurantRequest;
import com.vitorbetmann.cleanresmapi.infrastructure.restaurant.dtos.RestaurantResponse;
import com.vitorbetmann.cleanresmapi.infrastructure.restaurant.dtos.UpdateRestaurantRequest;
import com.vitorbetmann.cleanresmapi.usecases.restaurant.*;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/restaurants")
public class RestaurantController {

    private final CreateRestaurant createRestaurant;
    private final DeleteRestaurant deleteRestaurant;
    private final UpdateRestaurant updateRestaurant;
    private final GetRestaurant getRestaurant;
    private final ListRestaurants listRestaurants;

    public RestaurantController(CreateRestaurant createRestaurant, DeleteRestaurant deleteRestaurant, UpdateRestaurant updateRestaurant, GetRestaurant getRestaurant, ListRestaurants listRestaurants) {
        this.createRestaurant = createRestaurant;
        this.deleteRestaurant = deleteRestaurant;
        this.updateRestaurant = updateRestaurant;
        this.getRestaurant = getRestaurant;
        this.listRestaurants = listRestaurants;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public RestaurantResponse create(@RequestBody @Valid CreateRestaurantRequest request) {
        var restaurant = this.createRestaurant.execute(request.name(), request.address(), request.cuisineType(), request.openingHours(), request.ownerId());
        return RestaurantResponse.fromDomain(restaurant);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id) {
        this.deleteRestaurant.execute(id);
    }

    @GetMapping("/{id}")
    public RestaurantResponse get(@PathVariable Long id) {
        var restaurant = this.getRestaurant.execute(id);
        return RestaurantResponse.fromDomain(restaurant);
    }

    @GetMapping
    public List<RestaurantResponse> getAll() {
        var restaurants = listRestaurants.execute();
        return restaurants.stream().map(RestaurantResponse::fromDomain).toList();
    }

    @PutMapping("/{id}")
    public RestaurantResponse update(@PathVariable Long id, @RequestBody @Valid UpdateRestaurantRequest request) {
        var restaurant = this.updateRestaurant.execute(id, request.name(), request.address(), request.cuisineType(), request.openingHours(), request.ownerId());
        return RestaurantResponse.fromDomain(restaurant);
    }
}
