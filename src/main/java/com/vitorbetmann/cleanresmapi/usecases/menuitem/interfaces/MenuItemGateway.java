package com.vitorbetmann.cleanresmapi.usecases.menuitem.interfaces;

import com.vitorbetmann.cleanresmapi.entities.menuitem.MenuItem;

import java.util.List;
import java.util.Optional;

public interface MenuItemGateway {

    MenuItem save(MenuItem menuItem);

    Optional<MenuItem> getById(Long id);

    List<MenuItem> getAll();

    void delete(Long id);

}
