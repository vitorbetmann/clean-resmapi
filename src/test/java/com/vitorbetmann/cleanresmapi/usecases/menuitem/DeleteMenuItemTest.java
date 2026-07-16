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

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class DeleteMenuItemTest {

    @Mock
    MenuItemGateway menuItemGateway;

    @InjectMocks
    DeleteMenuItem deleteMenuItem;

    Long mockId = 1L;
    UserType mockUserType = new UserType(mockId, "Owner");
    User mockOwner = new User(mockId, "John", "john@email.com", "password123", mockUserType, "404 John's address", null);
    Restaurant mockRestaurant = new Restaurant(mockId, "Cantina da Praça", "123 Main St", "Italian", "09:00-22:00", mockOwner);
    String mockName = "Spaghetti Carbonara";
    String mockDescription = "Classic Roman pasta with egg, cheese, and pancetta.";
    BigDecimal mockPrice = new BigDecimal("28.90");
    String mockCategory = "Main Course";

    @Test
    void execute_whenIdIsFound_deletesMenuItem() {
        // arrange
        var saved = new MenuItem(mockId, mockName, mockDescription, mockPrice, mockCategory, mockRestaurant);
        when(menuItemGateway.getById(mockId)).thenReturn(Optional.of(saved));

        // act
        deleteMenuItem.execute(mockId);

        //assert
        verify(menuItemGateway).delete(mockId);
    }

    @Test
    void execute_whenIdIsNotFound_throwsMenuItemNotFoundException() {
        // arrange
        when(menuItemGateway.getById(mockId)).thenReturn(Optional.empty());

        // assert on act
        assertThrows(MenuItemNotFoundException.class, () -> deleteMenuItem.execute(mockId));
        verify(menuItemGateway, never()).delete(mockId);
    }

}
