package com.vitorbetmann.cleanresmapi.infrastructure.menuitem;

import com.vitorbetmann.cleanresmapi.entities.menuitem.MenuItem;
import com.vitorbetmann.cleanresmapi.usecases.menuitem.interfaces.MenuItemGateway;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
public class MenuItemGatewayImpl implements MenuItemGateway {

    private final MenuItemRepository menuItemRepository;

    public MenuItemGatewayImpl(MenuItemRepository menuItemRepository) {
        this.menuItemRepository = menuItemRepository;
    }

    @Override
    public MenuItem save(MenuItem menuItem) {
        var entity = MenuItemEntity.fromDomain(menuItem);
        var savedEntity = this.menuItemRepository.save(entity);
        return savedEntity.toDomain();
    }

    @Override
    public Optional<MenuItem> getById(Long id) {
        return this.menuItemRepository.findById(id).map(MenuItemEntity::toDomain);
    }

    @Override
    public List<MenuItem> getAll() {
        return this.menuItemRepository.findAll()
                .stream()
                .map(MenuItemEntity::toDomain)
                .toList();
    }

    @Override
    public void delete(Long id) {
        this.menuItemRepository.deleteById(id);
    }
}
