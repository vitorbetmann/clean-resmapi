package com.vitorbetmann.cleanresmapi.entities.menuitem;

import com.vitorbetmann.cleanresmapi.entities.restaurant.Restaurant;
import com.vitorbetmann.cleanresmapi.entities.user.User;
import com.vitorbetmann.cleanresmapi.entities.usertype.UserType;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
public class MenuItemTest {

    Long mockId = 1L;
    UserType mockUserType = new UserType(mockId, "Owner");
    User mockOwner = new User(mockId, "John", "john@email.com", "password123", mockUserType, "404 John's address", null);
    Restaurant mockRestaurant = new Restaurant(mockId, "Cantina da Praça", "123 Main St", "Italian", "09:00-22:00", mockOwner);
    String mockName = "Spaghetti Carbonara";
    String mockDescription = "Classic Roman pasta with egg, cheese, and pancetta.";
    BigDecimal mockPrice = new BigDecimal("28.90");
    boolean mockAvailableOnlyAtRestaurant = true;
    String mockPhotoPath = "/photos/spaghetti-carbonara.jpg";

    @Test
    void constructor_whenFieldsAreValid_returnsNewMenuItem() {
        var menuItem = new MenuItem(mockId, mockName, mockDescription, mockPrice, mockAvailableOnlyAtRestaurant, mockPhotoPath, mockRestaurant);
        assertEquals(mockId, menuItem.getId());
        assertEquals(mockName, menuItem.getName());
        assertEquals(mockDescription, menuItem.getDescription());
        assertEquals(mockPrice, menuItem.getPrice());
        assertEquals(mockAvailableOnlyAtRestaurant, menuItem.isAvailableOnlyAtRestaurant());
        assertEquals(mockPhotoPath, menuItem.getPhotoPath());
        assertEquals(mockRestaurant, menuItem.getRestaurant());
    }

    @Test
    void constructor_whenNameIsNull_throwsIllegalArgumentException() {
        Exception exception = assertThrows(
                IllegalArgumentException.class,
                () -> new MenuItem(mockId, null, mockDescription, mockPrice, mockAvailableOnlyAtRestaurant, mockPhotoPath, mockRestaurant)
        );

        String expectedMessage = "Name cannot be null or blank.";
        String actualMessage = exception.getMessage();

        assertTrue(actualMessage.contains(expectedMessage));
    }

    @Test
    void constructor_whenDescriptionIsNull_throwsIllegalArgumentException() {
        Exception exception = assertThrows(
                IllegalArgumentException.class,
                () -> new MenuItem(mockId, mockName, null, mockPrice, mockAvailableOnlyAtRestaurant, mockPhotoPath, mockRestaurant)
        );

        String expectedMessage = "Description cannot be null or blank.";
        String actualMessage = exception.getMessage();

        assertTrue(actualMessage.contains(expectedMessage));
    }

    @Test
    void constructor_whenPriceIsNull_throwsIllegalArgumentException() {
        Exception exception = assertThrows(
                IllegalArgumentException.class,
                () -> new MenuItem(mockId, mockName, mockDescription, null, mockAvailableOnlyAtRestaurant, mockPhotoPath, mockRestaurant)
        );

        String expectedMessage = "Price cannot be null.";
        String actualMessage = exception.getMessage();

        assertTrue(actualMessage.contains(expectedMessage));
    }

    @Test
    void constructor_whenPriceIsZeroOrLess_throwsIllegalArgumentException() {
        Exception exception = assertThrows(
                IllegalArgumentException.class,
                () -> new MenuItem(mockId, mockName, mockDescription, BigDecimal.ZERO, mockAvailableOnlyAtRestaurant, mockPhotoPath, mockRestaurant)
        );

        String expectedMessage = "Price must be greater than zero.";
        String actualMessage = exception.getMessage();

        assertTrue(actualMessage.contains(expectedMessage));
    }

    @Test
    void constructor_whenPhotoPathIsNull_throwsIllegalArgumentException() {
        Exception exception = assertThrows(
                IllegalArgumentException.class,
                () -> new MenuItem(mockId, mockName, mockDescription, mockPrice, mockAvailableOnlyAtRestaurant, null, mockRestaurant)
        );

        String expectedMessage = "Photo path cannot be null or blank.";
        String actualMessage = exception.getMessage();

        assertTrue(actualMessage.contains(expectedMessage));
    }

    @Test
    void constructor_whenRestaurantIsNull_throwsIllegalArgumentException() {
        Exception exception = assertThrows(
                IllegalArgumentException.class,
                () -> new MenuItem(mockId, mockName, mockDescription, mockPrice, mockAvailableOnlyAtRestaurant, mockPhotoPath, null)
        );

        String expectedMessage = "Restaurant cannot be null.";
        String actualMessage = exception.getMessage();

        assertTrue(actualMessage.contains(expectedMessage));
    }

    @Test
    void create_returnsMenuItemWithNullId() {
        var menuItem = MenuItem.create(mockName, mockDescription, mockPrice, mockAvailableOnlyAtRestaurant, mockPhotoPath, mockRestaurant);
        assertNull(menuItem.getId());
    }

}
