package com.vitorbetmann.cleanresmapi.usecases.menuitem;

import com.vitorbetmann.cleanresmapi.entities.menuitem.MenuItem;
import com.vitorbetmann.cleanresmapi.entities.restaurant.Restaurant;
import com.vitorbetmann.cleanresmapi.entities.user.User;
import com.vitorbetmann.cleanresmapi.entities.usertype.UserType;
import com.vitorbetmann.cleanresmapi.usecases.menuitem.interfaces.MenuItemGateway;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class ListMenuItemsTest {

    @Mock
    MenuItemGateway menuItemGateway;

    @InjectMocks
    ListMenuItems listMenuItems;

    List<MenuItem> mockList;
    Long mockId = 1L;
    UserType mockUserType = new UserType(mockId, "Owner");
    User mockOwner = new User(mockId, "John", "john@email.com", "password123", mockUserType, "404 John's address", null);
    Restaurant mockRestaurant = new Restaurant(mockId, "Cantina da Praça", "123 Main St", "Italian", "09:00-22:00", mockOwner);
    String mockName = "Spaghetti Carbonara";
    String mockDescription = "Classic Roman pasta with egg, cheese, and pancetta.";
    BigDecimal mockPrice = new BigDecimal("28.90");
    String mockCategory = "Main Course";

    @Test
    void execute_whenNoMenuItemsExist_returnsEmptyList() {
        // arrange
        mockList = new ArrayList<>();
        when(menuItemGateway.getAll()).thenReturn(mockList);

        // act
        var result = listMenuItems.execute();

        // assert
        assertEquals(0, result.size());
    }

    @Test
    void execute_whenMenuItemsExist_returnsMenuItemList() {
        // arrange
        mockList = new ArrayList<>();
        var menuItem = new MenuItem(mockId, mockName, mockDescription, mockPrice, mockCategory, mockRestaurant);
        mockList.add(menuItem);
        when(menuItemGateway.getAll()).thenReturn(mockList);

        // act
        var result = listMenuItems.execute();

        // assert
        assertEquals(1, result.size());
    }

}
