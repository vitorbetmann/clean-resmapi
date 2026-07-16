package com.vitorbetmann.cleanresmapi.usecases.restaurant;

import com.vitorbetmann.cleanresmapi.entities.restaurant.Restaurant;
import com.vitorbetmann.cleanresmapi.entities.user.User;
import com.vitorbetmann.cleanresmapi.entities.usertype.UserType;
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
public class CreateRestaurantTest {

    @Mock
    RestaurantGateway restaurantGateway;

    @Mock
    UserGateway userGateway;

    @InjectMocks
    CreateRestaurant createRestaurant;

    Long mockId = 1L;
    UserType mockUserType = new UserType(mockId, "Owner");
    User mockOwner = new User(mockId, "John", "john@email.com", "password123", mockUserType, "404 John's address", null);
    String mockName = "Cantina da Praça";
    String mockAddress = "123 Main St";
    String mockCuisineType = "Italian";
    String mockOpeningHours = "09:00-22:00";

    @Test
    void execute_whenOwnerIsFound_savesAndReturnsRestaurant() {
        // arrange
        when(userGateway.getById(mockId)).thenReturn(Optional.of(mockOwner));

        var saved = new Restaurant(mockId, mockName, mockAddress, mockCuisineType, mockOpeningHours, mockOwner);
        when(restaurantGateway.save(any(Restaurant.class))).thenReturn(saved);

        // act
        var result = createRestaurant.execute(mockName, mockAddress, mockCuisineType, mockOpeningHours, mockOwner.getId());

        // assert
        assertEquals(mockId, result.getId());
        assertEquals(mockName, result.getName());
        assertEquals(mockAddress, result.getAddress());
        assertEquals(mockCuisineType, result.getCuisineType());
        assertEquals(mockOpeningHours, result.getOpeningHours());
        assertEquals(mockOwner, result.getOwner());
    }

    @Test
    void execute_whenOwnerIsNotFound_throwsUserNotFoundException() {
        // arrange
        when(userGateway.getById(mockId)).thenReturn(Optional.empty());

        // assert on act
        assertThrows(UserNotFoundException.class, () -> createRestaurant.execute(mockName, mockAddress, mockCuisineType, mockOpeningHours, mockId));
        verify(restaurantGateway, never()).save(any());
    }

}
