package com.vitorbetmann.cleanresmapi.infrastructure.menuitem;

import com.vitorbetmann.cleanresmapi.infrastructure.menuitem.dtos.CreateMenuItemRequest;
import com.vitorbetmann.cleanresmapi.infrastructure.menuitem.dtos.MenuItemResponse;
import com.vitorbetmann.cleanresmapi.infrastructure.menuitem.dtos.UpdateMenuItemRequest;
import com.vitorbetmann.cleanresmapi.usecases.menuitem.*;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/menu-items")
public class MenuItemController {

    private final CreateMenuItem createMenuItem;
    private final DeleteMenuItem deleteMenuItem;
    private final UpdateMenuItem updateMenuItem;
    private final GetMenuItem getMenuItem;
    private final ListMenuItems listMenuItems;

    public MenuItemController(CreateMenuItem createMenuItem, DeleteMenuItem deleteMenuItem, UpdateMenuItem updateMenuItem, GetMenuItem getMenuItem, ListMenuItems listMenuItems) {
        this.createMenuItem = createMenuItem;
        this.deleteMenuItem = deleteMenuItem;
        this.updateMenuItem = updateMenuItem;
        this.getMenuItem = getMenuItem;
        this.listMenuItems = listMenuItems;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public MenuItemResponse create(@RequestBody @Valid CreateMenuItemRequest request) {
        var menuItem = this.createMenuItem.execute(request.name(), request.description(), request.price(), request.availableOnlyAtRestaurant(), request.photoPath(), request.restaurantId());
        return MenuItemResponse.fromDomain(menuItem);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id) {
        this.deleteMenuItem.execute(id);
    }

    @GetMapping("/{id}")
    public MenuItemResponse get(@PathVariable Long id) {
        var menuItem = this.getMenuItem.execute(id);
        return MenuItemResponse.fromDomain(menuItem);
    }

    @GetMapping
    public List<MenuItemResponse> getAll() {
        var menuItems = listMenuItems.execute();
        return menuItems.stream().map(MenuItemResponse::fromDomain).toList();
    }

    @PutMapping("/{id}")
    public MenuItemResponse update(@PathVariable Long id, @RequestBody @Valid UpdateMenuItemRequest request) {
        var menuItem = this.updateMenuItem.execute(id, request.name(), request.description(), request.price(), request.availableOnlyAtRestaurant(), request.photoPath(), request.restaurantId());
        return MenuItemResponse.fromDomain(menuItem);
    }
}
