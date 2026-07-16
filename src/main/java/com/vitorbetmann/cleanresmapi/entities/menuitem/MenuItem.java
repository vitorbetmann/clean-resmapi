package com.vitorbetmann.cleanresmapi.entities.menuitem;

import com.vitorbetmann.cleanresmapi.entities.restaurant.Restaurant;
import lombok.Getter;

import java.math.BigDecimal;

@Getter
public class MenuItem {

    private final Long id;
    private final String name;
    private final String description;
    private final BigDecimal price;
    private final boolean availableOnlyAtRestaurant;
    private final String photoPath;
    private final Restaurant restaurant;

    public MenuItem(Long id, String name, String description, BigDecimal price, boolean availableOnlyAtRestaurant, String photoPath, Restaurant restaurant) {

        if (name == null || name.isBlank()) {
            throw new IllegalArgumentException("Name cannot be null or blank.");
        }

        if (description == null || description.isBlank()) {
            throw new IllegalArgumentException("Description cannot be null or blank.");
        }

        if (price == null) {
            throw new IllegalArgumentException("Price cannot be null.");
        }

        if (price.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Price must be greater than zero.");
        }

        if (photoPath == null || photoPath.isBlank()) {
            throw new IllegalArgumentException("Photo path cannot be null or blank.");
        }

        if (restaurant == null) {
            throw new IllegalArgumentException("Restaurant cannot be null.");
        }

        this.id = id;
        this.name = name;
        this.description = description;
        this.price = price;
        this.availableOnlyAtRestaurant = availableOnlyAtRestaurant;
        this.photoPath = photoPath;
        this.restaurant = restaurant;
    }

    public static MenuItem create(String name, String description, BigDecimal price, boolean availableOnlyAtRestaurant, String photoPath, Restaurant restaurant) {
        return new MenuItem(null, name, description, price, availableOnlyAtRestaurant, photoPath, restaurant);
    }
}
