package com.vitorbetmann.cleanresmapi.usecases.menuitem;

import com.vitorbetmann.cleanresmapi.entities.menuitem.MenuItem;
import com.vitorbetmann.cleanresmapi.usecases.menuitem.interfaces.MenuItemGateway;

import java.util.List;

public class ListMenuItems {

    private final MenuItemGateway menuItemGateway;

    public ListMenuItems(MenuItemGateway menuItemGateway) {
        this.menuItemGateway = menuItemGateway;
    }

    public List<MenuItem> execute() {
        return this.menuItemGateway.getAll();
    }
}
