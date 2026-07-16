package com.vitorbetmann.cleanresmapi.usecases.restaurant;

import com.vitorbetmann.cleanresmapi.entities.restaurant.Restaurant;
import com.vitorbetmann.cleanresmapi.entities.user.User;
import com.vitorbetmann.cleanresmapi.entities.usertype.UserType;
import com.vitorbetmann.cleanresmapi.usecases.restaurant.interfaces.RestaurantGateway;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class ListRestaurantsTest {

    @Mock
    RestaurantGateway restaurantGateway;

    @InjectMocks
    ListRestaurants listRestaurants;

    List<Restaurant> mockList;
    Long mockId = 1L;
    UserType mockUserType = new UserType(mockId, "Owner");
    User mockOwner = new User(mockId, "John", "john@email.com", "password123", mockUserType, "404 John's address", null);
    String mockName = "Cantina da Praça";
    String mockAddress = "123 Main St";
    String mockCuisineType = "Italian";
    String mockOpeningHours = "09:00-22:00";

    @Test
    void execute_whenNoRestaurantsExist_returnsEmptyList() {
        // arrange
        mockList = new ArrayList<>();
        when(restaurantGateway.getAll()).thenReturn(mockList);

        // act
        var result = listRestaurants.execute();

        // assert
        assertEquals(0, result.size());
    }

    @Test
    void execute_whenRestaurantsExist_returnsRestaurantList() {
        // arrange
        mockList = new ArrayList<>();
        var restaurant = new Restaurant(mockId, mockName, mockAddress, mockCuisineType, mockOpeningHours, mockOwner);
        mockList.add(restaurant);
        when(restaurantGateway.getAll()).thenReturn(mockList);

        // act
        var result = listRestaurants.execute();

        // assert
        assertEquals(1, result.size());
    }

}
