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
    String mockCategory = "Main Course";

    @Test
    void constructor_whenFieldsAreValid_returnsNewMenuItem() {
        var menuItem = new MenuItem(mockId, mockName, mockDescription, mockPrice, mockCategory, mockRestaurant);
        assertEquals(mockId, menuItem.getId());
        assertEquals(mockName, menuItem.getName());
        assertEquals(mockDescription, menuItem.getDescription());
        assertEquals(mockPrice, menuItem.getPrice());
        assertEquals(mockCategory, menuItem.getCategory());
        assertEquals(mockRestaurant, menuItem.getRestaurant());
    }

    @Test
    void constructor_whenNameIsNull_throwsIllegalArgumentException() {
        Exception exception = assertThrows(
                IllegalArgumentException.class,
                () -> new MenuItem(mockId, null, mockDescription, mockPrice, mockCategory, mockRestaurant)
        );

        String expectedMessage = "Name cannot be null or blank.";
        String actualMessage = exception.getMessage();

        assertTrue(actualMessage.contains(expectedMessage));
    }

    @Test
    void constructor_whenDescriptionIsNull_throwsIllegalArgumentException() {
        Exception exception = assertThrows(
                IllegalArgumentException.class,
                () -> new MenuItem(mockId, mockName, null, mockPrice, mockCategory, mockRestaurant)
        );

        String expectedMessage = "Description cannot be null or blank.";
        String actualMessage = exception.getMessage();

        assertTrue(actualMessage.contains(expectedMessage));
    }

    @Test
    void constructor_whenPriceIsNull_throwsIllegalArgumentException() {
        Exception exception = assertThrows(
                IllegalArgumentException.class,
                () -> new MenuItem(mockId, mockName, mockDescription, null, mockCategory, mockRestaurant)
        );

        String expectedMessage = "Price cannot be null.";
        String actualMessage = exception.getMessage();

        assertTrue(actualMessage.contains(expectedMessage));
    }

    @Test
    void constructor_whenPriceIsZeroOrLess_throwsIllegalArgumentException() {
        Exception exception = assertThrows(
                IllegalArgumentException.class,
                () -> new MenuItem(mockId, mockName, mockDescription, BigDecimal.ZERO, mockCategory, mockRestaurant)
        );

        String expectedMessage = "Price must be greater than zero.";
        String actualMessage = exception.getMessage();

        assertTrue(actualMessage.contains(expectedMessage));
    }

    @Test
    void constructor_whenCategoryIsNull_throwsIllegalArgumentException() {
        Exception exception = assertThrows(
                IllegalArgumentException.class,
                () -> new MenuItem(mockId, mockName, mockDescription, mockPrice, null, mockRestaurant)
        );

        String expectedMessage = "Category cannot be null or blank.";
        String actualMessage = exception.getMessage();

        assertTrue(actualMessage.contains(expectedMessage));
    }

    @Test
    void constructor_whenRestaurantIsNull_throwsIllegalArgumentException() {
        Exception exception = assertThrows(
                IllegalArgumentException.class,
                () -> new MenuItem(mockId, mockName, mockDescription, mockPrice, mockCategory, null)
        );

        String expectedMessage = "Restaurant cannot be null.";
        String actualMessage = exception.getMessage();

        assertTrue(actualMessage.contains(expectedMessage));
    }

    @Test
    void create_returnsMenuItemWithNullId() {
        var menuItem = MenuItem.create(mockName, mockDescription, mockPrice, mockCategory, mockRestaurant);
        assertNull(menuItem.getId());
    }

}
