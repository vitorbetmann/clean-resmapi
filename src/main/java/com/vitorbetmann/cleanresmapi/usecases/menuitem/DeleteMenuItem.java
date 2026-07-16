package com.vitorbetmann.cleanresmapi.usecases.menuitem;

import com.vitorbetmann.cleanresmapi.usecases.menuitem.exceptions.MenuItemNotFoundException;
import com.vitorbetmann.cleanresmapi.usecases.menuitem.interfaces.MenuItemGateway;

public class DeleteMenuItem {

    private final MenuItemGateway menuItemGateway;

    public DeleteMenuItem(MenuItemGateway menuItemGateway) {
        this.menuItemGateway = menuItemGateway;
    }

    public void execute(Long id) {
        var menuItem = this.menuItemGateway.getById(id);
        if (menuItem.isEmpty()) {
            throw new MenuItemNotFoundException("MenuItem ID not found: " + id);
        }
        this.menuItemGateway.delete(id);
    }
}
