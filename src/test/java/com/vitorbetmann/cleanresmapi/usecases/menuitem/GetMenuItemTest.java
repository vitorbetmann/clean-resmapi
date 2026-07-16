package com.vitorbetmann.cleanresmapi.usecases.menuitem;

import com.vitorbetmann.cleanresmapi.entities.menuitem.MenuItem;
import com.vitorbetmann.cleanresmapi.entities.restaurant.Restaurant;
import com.vitorbetmann.cleanresmapi.entities.user.User;
import com.vitorbetmann.cleanresmapi.entities.usertype.UserType;
import com.vitorbetmann.cleanresmapi.usecases.menuitem.exceptions.MenuItemNotFoundException;
import com.vitorbetmann.cleanresmapi.usecases.menuitem.interfaces.MenuItemGateway;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class GetMenuItemTest {

    @Mock
    MenuItemGateway menuItemGateway;

    @InjectMocks
    GetMenuItem getMenuItem;

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
    void execute_whenIdIsFound_ReturnsMenuItem() {
        // arrange
        var saved = new MenuItem(mockId, mockName, mockDescription, mockPrice, mockAvailableOnlyAtRestaurant, mockPhotoPath, mockRestaurant);
        when(menuItemGateway.getById(mockId)).thenReturn(Optional.of(saved));

        // act
        var result = getMenuItem.execute(mockId);

        // assert
        assertEquals(mockId, result.getId());
        assertEquals(mockName, result.getName());
    }

    @Test
    void execute_whenIdIsNotFound_throwsMenuItemNotFoundException() {
        // arrange
        when(menuItemGateway.getById(mockId)).thenReturn(Optional.empty());

        // assert on act
        assertThrows(MenuItemNotFoundException.class, () -> getMenuItem.execute(mockId));
    }

}
