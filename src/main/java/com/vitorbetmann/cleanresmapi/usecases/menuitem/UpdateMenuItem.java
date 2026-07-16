package com.vitorbetmann.cleanresmapi.usecases.menuitem;

import com.vitorbetmann.cleanresmapi.entities.menuitem.MenuItem;
import com.vitorbetmann.cleanresmapi.usecases.menuitem.exceptions.MenuItemNotFoundException;
import com.vitorbetmann.cleanresmapi.usecases.menuitem.interfaces.MenuItemGateway;
import com.vitorbetmann.cleanresmapi.usecases.restaurant.exceptions.RestaurantNotFoundException;
import com.vitorbetmann.cleanresmapi.usecases.restaurant.interfaces.RestaurantGateway;

import java.math.BigDecimal;

public class UpdateMenuItem {

    private final MenuItemGateway menuItemGateway;
    private final RestaurantGateway restaurantGateway;

    public UpdateMenuItem(MenuItemGateway menuItemGateway, RestaurantGateway restaurantGateway) {
        this.menuItemGateway = menuItemGateway;
        this.restaurantGateway = restaurantGateway;
    }

    public MenuItem execute(Long id, String name, String description, BigDecimal price, boolean availableOnlyAtRestaurant, String photoPath, Long restaurantId) {
        this.menuItemGateway.getById(id).orElseThrow(
                () -> new MenuItemNotFoundException("MenuItem ID not found: " + id)
        );

        var restaurant = this.restaurantGateway.getById(restaurantId).orElseThrow(
                () -> new RestaurantNotFoundException("Restaurant ID not found: " + restaurantId)
        );

        var updatedMenuItem = new MenuItem(id, name, description, price, availableOnlyAtRestaurant, photoPath, restaurant);
        return this.menuItemGateway.save(updatedMenuItem);
    }
}
