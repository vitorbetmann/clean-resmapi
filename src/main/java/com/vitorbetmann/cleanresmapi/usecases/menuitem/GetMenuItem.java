package com.vitorbetmann.cleanresmapi.usecases.menuitem;

import com.vitorbetmann.cleanresmapi.entities.menuitem.MenuItem;
import com.vitorbetmann.cleanresmapi.usecases.menuitem.exceptions.MenuItemNotFoundException;
import com.vitorbetmann.cleanresmapi.usecases.menuitem.interfaces.MenuItemGateway;

import java.util.Optional;

public class GetMenuItem {

    private final MenuItemGateway menuItemGateway;

    public GetMenuItem(MenuItemGateway menuItemGateway) {
        this.menuItemGateway = menuItemGateway;
    }

    public MenuItem execute(Long id) {
        Optional<MenuItem> menuItem = this.menuItemGateway.getById(id);
        if (menuItem.isEmpty()) {
            throw new MenuItemNotFoundException("MenuItem ID not found: " + id);
        }
        return menuItem.get();
    }
}
