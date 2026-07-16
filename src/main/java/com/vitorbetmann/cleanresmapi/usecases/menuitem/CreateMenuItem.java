package com.vitorbetmann.cleanresmapi.usecases.menuitem;

import com.vitorbetmann.cleanresmapi.entities.menuitem.MenuItem;
import com.vitorbetmann.cleanresmapi.usecases.menuitem.interfaces.MenuItemGateway;
import com.vitorbetmann.cleanresmapi.usecases.restaurant.exceptions.RestaurantNotFoundException;
import com.vitorbetmann.cleanresmapi.usecases.restaurant.interfaces.RestaurantGateway;

import java.math.BigDecimal;

public class CreateMenuItem {

    private final MenuItemGateway menuItemGateway;
    private final RestaurantGateway restaurantGateway;

    public CreateMenuItem(MenuItemGateway menuItemGateway, RestaurantGateway restaurantGateway) {
        this.menuItemGateway = menuItemGateway;
        this.restaurantGateway = restaurantGateway;
    }

    public MenuItem execute(String name, String description, BigDecimal price, String category, Long restaurantId) {
        var restaurant = this.restaurantGateway.getById(restaurantId).orElseThrow(
                () -> new RestaurantNotFoundException("Restaurant ID not found: " + restaurantId)
        );

        var menuItem = MenuItem.create(name, description, price, category, restaurant);
        return this.menuItemGateway.save(menuItem);
    }
}
