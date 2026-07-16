package com.vitorbetmann.cleanresmapi.entities.restaurant;

import com.vitorbetmann.cleanresmapi.entities.user.User;
import lombok.Getter;

@Getter
public class Restaurant {

    private static final String OWNER_USER_TYPE_NAME = "Owner";

    private final Long id;
    private final String name;
    private final String address;
    private final String cuisineType;
    private final String openingHours;
    private final User owner;

    public Restaurant(Long id, String name, String address, String cuisineType, String openingHours, User owner) {

        if (name == null || name.isBlank()) {
            throw new IllegalArgumentException("Name cannot be null or blank.");
        }

        if (address == null || address.isBlank()) {
            throw new IllegalArgumentException("Address cannot be null or blank.");
        }

        if (cuisineType == null || cuisineType.isBlank()) {
            throw new IllegalArgumentException("Cuisine type cannot be null or blank.");
        }

        if (openingHours == null || openingHours.isBlank()) {
            throw new IllegalArgumentException("Opening hours cannot be null or blank.");
        }

        if (owner == null) {
            throw new IllegalArgumentException("Owner cannot be null.");
        }

        if (!OWNER_USER_TYPE_NAME.equals(owner.getUserType().getName())) {
            throw new IllegalArgumentException("Owner must be a User of type Owner.");
        }

        this.id = id;
        this.name = name;
        this.address = address;
        this.cuisineType = cuisineType;
        this.openingHours = openingHours;
        this.owner = owner;
    }

    public static Restaurant create(String name, String address, String cuisineType, String openingHours, User owner) {
        return new Restaurant(null, name, address, cuisineType, openingHours, owner);
    }
}
