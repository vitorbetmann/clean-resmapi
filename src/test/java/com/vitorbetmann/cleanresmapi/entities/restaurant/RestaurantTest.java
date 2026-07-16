package com.vitorbetmann.cleanresmapi.entities.restaurant;

import com.vitorbetmann.cleanresmapi.entities.user.User;
import com.vitorbetmann.cleanresmapi.entities.usertype.UserType;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
public class RestaurantTest {

    Long mockId = 1L;
    UserType mockOwnerUserType = new UserType(mockId, "Owner");
    UserType mockNonOwnerUserType = new UserType(2L, "Customer");
    User mockOwner = new User(mockId, "John", "john@email.com", "password123", mockOwnerUserType, "404 John's address", null);
    User mockNonOwner = new User(2L, "Jane", "jane@email.com", "password123", mockNonOwnerUserType, "404 Jane's address", null);
    String mockName = "Cantina da Praça";
    String mockAddress = "123 Main St";
    String mockCuisineType = "Italian";
    String mockOpeningHours = "09:00-22:00";

    @Test
    void constructor_whenFieldsAreValid_returnsNewRestaurant() {
        var restaurant = new Restaurant(mockId, mockName, mockAddress, mockCuisineType, mockOpeningHours, mockOwner);
        assertEquals(mockId, restaurant.getId());
        assertEquals(mockName, restaurant.getName());
        assertEquals(mockAddress, restaurant.getAddress());
        assertEquals(mockCuisineType, restaurant.getCuisineType());
        assertEquals(mockOpeningHours, restaurant.getOpeningHours());
        assertEquals(mockOwner, restaurant.getOwner());
    }

    @Test
    void constructor_whenNameIsNull_throwsIllegalArgumentException() {
        Exception exception = assertThrows(
                IllegalArgumentException.class,
                () -> new Restaurant(mockId, null, mockAddress, mockCuisineType, mockOpeningHours, mockOwner)
        );

        String expectedMessage = "Name cannot be null or blank.";
        String actualMessage = exception.getMessage();

        assertTrue(actualMessage.contains(expectedMessage));
    }

    @Test
    void constructor_whenAddressIsNull_throwsIllegalArgumentException() {
        Exception exception = assertThrows(
                IllegalArgumentException.class,
                () -> new Restaurant(mockId, mockName, null, mockCuisineType, mockOpeningHours, mockOwner)
        );

        String expectedMessage = "Address cannot be null or blank.";
        String actualMessage = exception.getMessage();

        assertTrue(actualMessage.contains(expectedMessage));
    }

    @Test
    void constructor_whenCuisineTypeIsNull_throwsIllegalArgumentException() {
        Exception exception = assertThrows(
                IllegalArgumentException.class,
                () -> new Restaurant(mockId, mockName, mockAddress, null, mockOpeningHours, mockOwner)
        );

        String expectedMessage = "Cuisine type cannot be null or blank.";
        String actualMessage = exception.getMessage();

        assertTrue(actualMessage.contains(expectedMessage));
    }

    @Test
    void constructor_whenOpeningHoursIsNull_throwsIllegalArgumentException() {
        Exception exception = assertThrows(
                IllegalArgumentException.class,
                () -> new Restaurant(mockId, mockName, mockAddress, mockCuisineType, null, mockOwner)
        );

        String expectedMessage = "Opening hours cannot be null or blank.";
        String actualMessage = exception.getMessage();

        assertTrue(actualMessage.contains(expectedMessage));
    }

    @Test
    void constructor_whenOwnerIsNull_throwsIllegalArgumentException() {
        Exception exception = assertThrows(
                IllegalArgumentException.class,
                () -> new Restaurant(mockId, mockName, mockAddress, mockCuisineType, mockOpeningHours, null)
        );

        String expectedMessage = "Owner cannot be null.";
        String actualMessage = exception.getMessage();

        assertTrue(actualMessage.contains(expectedMessage));
    }

    @Test
    void constructor_whenOwnerIsNotOfTypeOwner_throwsIllegalArgumentException() {
        Exception exception = assertThrows(
                IllegalArgumentException.class,
                () -> new Restaurant(mockId, mockName, mockAddress, mockCuisineType, mockOpeningHours, mockNonOwner)
        );

        String expectedMessage = "Owner must be a User of type Owner.";
        String actualMessage = exception.getMessage();

        assertTrue(actualMessage.contains(expectedMessage));
    }

    @Test
    void create_returnsRestaurantWithNullId() {
        var restaurant = Restaurant.create(mockName, mockAddress, mockCuisineType, mockOpeningHours, mockOwner);
        assertNull(restaurant.getId());
    }

}
