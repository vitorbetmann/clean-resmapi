package com.vitorbetmann.cleanresmapi.usecases.menuitem;

import com.vitorbetmann.cleanresmapi.entities.menuitem.MenuItem;
import com.vitorbetmann.cleanresmapi.entities.restaurant.Restaurant;
import com.vitorbetmann.cleanresmapi.entities.user.User;
import com.vitorbetmann.cleanresmapi.entities.usertype.UserType;
import com.vitorbetmann.cleanresmapi.usecases.menuitem.exceptions.MenuItemNotFoundException;
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
public class UpdateMenuItemTest {

    @Mock
    MenuItemGateway menuItemGateway;

    @Mock
    RestaurantGateway restaurantGateway;

    @InjectMocks
    UpdateMenuItem updateMenuItem;

    Long mockId = 1L;
    UserType mockUserType = new UserType(mockId, "Owner");
    User mockOwner = new User(mockId, "John", "john@email.com", "password123", mockUserType, "404 John's address", null);
    Restaurant mockRestaurant = new Restaurant(mockId, "Cantina da Praça", "123 Main St", "Italian", "09:00-22:00", mockOwner);
    String mockName1 = "Spaghetti Carbonara";
    String mockName2 = "Spaghetti Bolognese";
    String mockDescription = "Classic Italian pasta.";
    BigDecimal mockPrice = new BigDecimal("28.90");
    String mockCategory = "Main Course";

    @Test
    void execute_whenIdIsNotFound_throwsMenuItemNotFoundException() {
        // arrange
        when(menuItemGateway.getById(mockId)).thenReturn(Optional.empty());

        // assert on act
        assertThrows(MenuItemNotFoundException.class, () -> updateMenuItem.execute(mockId, mockName1, mockDescription, mockPrice, mockCategory, mockRestaurant.getId()));
        verify(menuItemGateway, never()).save(any());
    }

    @Test
    void execute_whenRestaurantIsNotFound_throwsRestaurantNotFoundException() {
        // arrange
        var saved = new MenuItem(mockId, mockName1, mockDescription, mockPrice, mockCategory, mockRestaurant);
        when(menuItemGateway.getById(mockId)).thenReturn(Optional.of(saved));

        when(restaurantGateway.getById(mockId)).thenReturn(Optional.empty());

        // assert on act
        assertThrows(RestaurantNotFoundException.class, () -> updateMenuItem.execute(mockId, mockName1, mockDescription, mockPrice, mockCategory, mockRestaurant.getId()));
        verify(menuItemGateway, never()).save(any());
    }

    @Test
    void execute_whenFieldsAreValid_updatesAndReturnsMenuItem() {
        // arrange
        var saved1 = new MenuItem(mockId, mockName1, mockDescription, mockPrice, mockCategory, mockRestaurant);
        when(menuItemGateway.getById(mockId)).thenReturn(Optional.of(saved1));

        when(restaurantGateway.getById(mockId)).thenReturn(Optional.of(mockRestaurant));

        var saved2 = new MenuItem(mockId, mockName2, mockDescription, mockPrice, mockCategory, mockRestaurant);
        when(menuItemGateway.save(any(MenuItem.class))).thenReturn(saved2);

        // act
        var result = updateMenuItem.execute(mockId, mockName2, mockDescription, mockPrice, mockCategory, mockRestaurant.getId());

        // assert
        assertEquals(mockName2, result.getName());
        assertEquals(mockId, result.getId());
    }

}
