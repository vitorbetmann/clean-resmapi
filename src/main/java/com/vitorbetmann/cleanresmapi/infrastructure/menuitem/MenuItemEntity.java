package com.vitorbetmann.cleanresmapi.infrastructure.menuitem;

import com.vitorbetmann.cleanresmapi.entities.menuitem.MenuItem;
import com.vitorbetmann.cleanresmapi.infrastructure.restaurant.RestaurantEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Entity
@NoArgsConstructor
@Getter
public class MenuItemEntity {

    @Id
    @GeneratedValue
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String description;

    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal price;

    @Column(nullable = false)
    private boolean availableOnlyAtRestaurant;

    @Column(nullable = false)
    private String photoPath;

    @ManyToOne
    @JoinColumn(nullable = false)
    private RestaurantEntity restaurantEntity;

    public MenuItemEntity(Long id, String name, String description, BigDecimal price, boolean availableOnlyAtRestaurant, String photoPath, RestaurantEntity restaurantEntity) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.price = price;
        this.availableOnlyAtRestaurant = availableOnlyAtRestaurant;
        this.photoPath = photoPath;
        this.restaurantEntity = restaurantEntity;
    }

    public MenuItem toDomain() {
        return new MenuItem(this.id, this.name, this.description, this.price, this.availableOnlyAtRestaurant, this.photoPath, this.restaurantEntity.toDomain());
    }

    public static MenuItemEntity fromDomain(MenuItem menuItem) {
        return new MenuItemEntity(menuItem.getId(), menuItem.getName(), menuItem.getDescription(), menuItem.getPrice(), menuItem.isAvailableOnlyAtRestaurant(), menuItem.getPhotoPath(), RestaurantEntity.fromDomain(menuItem.getRestaurant()));
    }
}
