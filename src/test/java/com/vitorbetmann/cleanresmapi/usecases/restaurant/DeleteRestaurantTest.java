package com.vitorbetmann.cleanresmapi.usecases.restaurant;

import com.vitorbetmann.cleanresmapi.entities.restaurant.Restaurant;
import com.vitorbetmann.cleanresmapi.entities.user.User;
import com.vitorbetmann.cleanresmapi.entities.usertype.UserType;
import com.vitorbetmann.cleanresmapi.usecases.restaurant.exceptions.RestaurantNotFoundException;
import com.vitorbetmann.cleanresmapi.usecases.restaurant.interfaces.RestaurantGateway;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class DeleteRestaurantTest {

    @Mock
    RestaurantGateway restaurantGateway;

    @InjectMocks
    DeleteRestaurant deleteRestaurant;

    Long mockId = 1L;
    UserType mockUserType = new UserType(mockId, "Owner");
    User mockOwner = new User(mockId, "John", "john@email.com", "password123", mockUserType, "404 John's address", null);
    String mockName = "Cantina da Praça";
    String mockAddress = "123 Main St";
    String mockCuisineType = "Italian";
    String mockOpeningHours = "09:00-22:00";

    @Test
    void execute_whenIdIsFound_deletesRestaurant() {
        // arrange
        var saved = new Restaurant(mockId, mockName, mockAddress, mockCuisineType, mockOpeningHours, mockOwner);
        when(restaurantGateway.getById(mockId)).thenReturn(Optional.of(saved));

        // act
        deleteRestaurant.execute(mockId);

        //assert
        verify(restaurantGateway).delete(mockId);
    }

    @Test
    void execute_whenIdIsNotFound_throwsRestaurantNotFoundException() {
        // arrange
        when(restaurantGateway.getById(mockId)).thenReturn(Optional.empty());

        // assert on act
        assertThrows(RestaurantNotFoundException.class, () -> deleteRestaurant.execute(mockId));
        verify(restaurantGateway, never()).delete(mockId);
    }

}
