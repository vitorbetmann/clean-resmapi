package com.vitorbetmann.cleanresmapi.infrastructure.restaurant;

import com.vitorbetmann.cleanresmapi.usecases.restaurant.*;
import com.vitorbetmann.cleanresmapi.usecases.restaurant.interfaces.RestaurantGateway;
import com.vitorbetmann.cleanresmapi.usecases.user.interfaces.UserGateway;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RestaurantConfig {

    @Bean
    public CreateRestaurant createRestaurant(RestaurantGateway restaurantGateway, UserGateway userGateway) {
        return new CreateRestaurant(restaurantGateway, userGateway);
    }

    @Bean
    public DeleteRestaurant deleteRestaurant(RestaurantGateway restaurantGateway) {
        return new DeleteRestaurant(restaurantGateway);
    }

    @Bean
    public GetRestaurant getRestaurant(RestaurantGateway restaurantGateway) {
        return new GetRestaurant(restaurantGateway);
    }

    @Bean
    public ListRestaurants listRestaurants(RestaurantGateway restaurantGateway) {
        return new ListRestaurants(restaurantGateway);
    }

    @Bean
    public UpdateRestaurant updateRestaurant(RestaurantGateway restaurantGateway, UserGateway userGateway) {
        return new UpdateRestaurant(restaurantGateway, userGateway);
    }
}
