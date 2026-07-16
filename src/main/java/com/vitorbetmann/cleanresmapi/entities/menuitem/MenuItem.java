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
    private final String category;
    private final Restaurant restaurant;

    public MenuItem(Long id, String name, String description, BigDecimal price, String category, Restaurant restaurant) {

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

        if (category == null || category.isBlank()) {
            throw new IllegalArgumentException("Category cannot be null or blank.");
        }

        if (restaurant == null) {
            throw new IllegalArgumentException("Restaurant cannot be null.");
        }

        this.id = id;
        this.name = name;
        this.description = description;
        this.price = price;
        this.category = category;
        this.restaurant = restaurant;
    }

    public static MenuItem create(String name, String description, BigDecimal price, String category, Restaurant restaurant) {
        return new MenuItem(null, name, description, price, category, restaurant);
    }
}
