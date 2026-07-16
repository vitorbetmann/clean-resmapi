package com.vitorbetmann.cleanresmapi.usecases.menuitem;

import com.vitorbetmann.cleanresmapi.entities.menuitem.MenuItem;
import com.vitorbetmann.cleanresmapi.entities.restaurant.Restaurant;
import com.vitorbetmann.cleanresmapi.entities.user.User;
import com.vitorbetmann.cleanresmapi.entities.usertype.UserType;
import com.vitorbetmann.cleanresmapi.usecases.menuitem.interfaces.MenuItemGateway;
import com.vitorbetmann.cleanresmapi.usecases.restaurant.exceptions.RestaurantNotFoundException;
import com.vitorbetmann.cleanresmapi.usecases.restaurant.interfaces.RestaurantGateway;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class CreateMenuItemTest {

    @Mock
    MenuItemGateway menuItemGateway;

    @Mock
    RestaurantGateway restaurantGateway;

    @InjectMocks
    CreateMenuItem createMenuItem;

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
    void execute_whenRestaurantIsFound_savesAndReturnsMenuItem() {
        // arrange
        when(restaurantGateway.getById(mockId)).thenReturn(Optional.of(mockRestaurant));

        var saved = new MenuItem(mockId, mockName, mockDescription, mockPrice, mockAvailableOnlyAtRestaurant, mockPhotoPath, mockRestaurant);
        when(menuItemGateway.save(any(MenuItem.class))).thenReturn(saved);

        // act
        var result = createMenuItem.execute(mockName, mockDescription, mockPrice, mockAvailableOnlyAtRestaurant, mockPhotoPath, mockRestaurant.getId());

        // assert
        assertEquals(mockId, result.getId());
        assertEquals(mockName, result.getName());
        assertEquals(mockDescription, result.getDescription());
        assertEquals(mockPrice, result.getPrice());
        assertEquals(mockAvailableOnlyAtRestaurant, result.isAvailableOnlyAtRestaurant());
        assertEquals(mockPhotoPath, result.getPhotoPath());
        assertEquals(mockRestaurant, result.getRestaurant());
    }

    @Test
    void execute_whenRestaurantIsNotFound_throwsRestaurantNotFoundException() {
        // arrange
        when(restaurantGateway.getById(mockId)).thenReturn(Optional.empty());

        // assert on act
        assertThrows(RestaurantNotFoundException.class, () -> createMenuItem.execute(mockName, mockDescription, mockPrice, mockAvailableOnlyAtRestaurant, mockPhotoPath, mockId));
        verify(menuItemGateway, never()).save(any());
    }

}
