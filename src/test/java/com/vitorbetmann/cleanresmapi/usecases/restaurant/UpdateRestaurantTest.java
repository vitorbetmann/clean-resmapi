package com.vitorbetmann.cleanresmapi.usecases.restaurant;

import com.vitorbetmann.cleanresmapi.entities.restaurant.Restaurant;
import com.vitorbetmann.cleanresmapi.entities.user.User;
import com.vitorbetmann.cleanresmapi.entities.usertype.UserType;
import com.vitorbetmann.cleanresmapi.usecases.restaurant.exceptions.RestaurantNotFoundException;
import com.vitorbetmann.cleanresmapi.usecases.restaurant.interfaces.RestaurantGateway;
import com.vitorbetmann.cleanresmapi.usecases.user.exceptions.UserNotFoundException;
import com.vitorbetmann.cleanresmapi.usecases.user.interfaces.UserGateway;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class UpdateRestaurantTest {

    @Mock
    RestaurantGateway restaurantGateway;

    @Mock
    UserGateway userGateway;

    @InjectMocks
    UpdateRestaurant updateRestaurant;

    Long mockId = 1L;
    UserType mockUserType = new UserType(mockId, "Owner");
    User mockOwner = new User(mockId, "John", "john@email.com", "password123", mockUserType, "404 John's address", null);
    String mockName1 = "Cantina da Praça";
    String mockName2 = "Cantina Nova";
    String mockAddress = "123 Main St";
    String mockCuisineType = "Italian";
    String mockOpeningHours = "09:00-22:00";

    @Test
    void execute_whenIdIsNotFound_throwsRestaurantNotFoundException() {
        // arrange
        when(restaurantGateway.getById(mockId)).thenReturn(Optional.empty());

        // assert on act
        assertThrows(RestaurantNotFoundException.class, () -> updateRestaurant.execute(mockId, mockName1, mockAddress, mockCuisineType, mockOpeningHours, mockOwner.getId()));
        verify(restaurantGateway, never()).save(any());
    }

    @Test
    void execute_whenOwnerIsNotFound_throwsUserNotFoundException() {
        // arrange
        var saved = new Restaurant(mockId, mockName1, mockAddress, mockCuisineType, mockOpeningHours, mockOwner);
        when(restaurantGateway.getById(mockId)).thenReturn(Optional.of(saved));

        when(userGateway.getById(mockId)).thenReturn(Optional.empty());

        // assert on act
        assertThrows(UserNotFoundException.class, () -> updateRestaurant.execute(mockId, mockName1, mockAddress, mockCuisineType, mockOpeningHours, mockOwner.getId()));
        verify(restaurantGateway, never()).save(any());
    }

    @Test
    void execute_whenFieldsAreValid_updatesAndReturnsRestaurant() {
        // arrange
        var saved1 = new Restaurant(mockId, mockName1, mockAddress, mockCuisineType, mockOpeningHours, mockOwner);
        when(restaurantGateway.getById(mockId)).thenReturn(Optional.of(saved1));

        when(userGateway.getById(mockId)).thenReturn(Optional.of(mockOwner));

        var saved2 = new Restaurant(mockId, mockName2, mockAddress, mockCuisineType, mockOpeningHours, mockOwner);
        when(restaurantGateway.save(any(Restaurant.class))).thenReturn(saved2);

        // act
        var result = updateRestaurant.execute(mockId, mockName2, mockAddress, mockCuisineType, mockOpeningHours, mockOwner.getId());

        // assert
        assertEquals(mockName2, result.getName());
        assertEquals(mockId, result.getId());
    }

}
