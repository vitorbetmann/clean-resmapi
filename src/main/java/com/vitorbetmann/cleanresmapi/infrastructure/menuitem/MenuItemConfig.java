package com.vitorbetmann.cleanresmapi.infrastructure.menuitem;

import com.vitorbetmann.cleanresmapi.usecases.menuitem.*;
import com.vitorbetmann.cleanresmapi.usecases.menuitem.interfaces.MenuItemGateway;
import com.vitorbetmann.cleanresmapi.usecases.restaurant.interfaces.RestaurantGateway;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class MenuItemConfig {

    @Bean
    public CreateMenuItem createMenuItem(MenuItemGateway menuItemGateway, RestaurantGateway restaurantGateway) {
        return new CreateMenuItem(menuItemGateway, restaurantGateway);
    }

    @Bean
    public DeleteMenuItem deleteMenuItem(MenuItemGateway menuItemGateway) {
        return new DeleteMenuItem(menuItemGateway);
    }

    @Bean
    public GetMenuItem getMenuItem(MenuItemGateway menuItemGateway) {
        return new GetMenuItem(menuItemGateway);
    }

    @Bean
    public ListMenuItems listMenuItems(MenuItemGateway menuItemGateway) {
        return new ListMenuItems(menuItemGateway);
    }

    @Bean
    public UpdateMenuItem updateMenuItem(MenuItemGateway menuItemGateway, RestaurantGateway restaurantGateway) {
        return new UpdateMenuItem(menuItemGateway, restaurantGateway);
    }
}
