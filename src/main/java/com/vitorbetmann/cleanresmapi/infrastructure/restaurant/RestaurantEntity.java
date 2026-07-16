package com.vitorbetmann.cleanresmapi.infrastructure.restaurant;

import com.vitorbetmann.cleanresmapi.entities.restaurant.Restaurant;
import com.vitorbetmann.cleanresmapi.infrastructure.user.UserEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor
@Getter
public class RestaurantEntity {

    @Id
    @GeneratedValue
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String address;

    @Column(nullable = false)
    private String cuisineType;

    @Column(nullable = false)
    private String openingHours;

    @ManyToOne
    @JoinColumn(nullable = false)
    private UserEntity ownerEntity;

    public RestaurantEntity(Long id, String name, String address, String cuisineType, String openingHours, UserEntity ownerEntity) {
        this.id = id;
        this.name = name;
        this.address = address;
        this.cuisineType = cuisineType;
        this.openingHours = openingHours;
        this.ownerEntity = ownerEntity;
    }

    public Restaurant toDomain() {
        return new Restaurant(this.id, this.name, this.address, this.cuisineType, this.openingHours, this.ownerEntity.toDomain());
    }

    public static RestaurantEntity fromDomain(Restaurant restaurant) {
        return new RestaurantEntity(restaurant.getId(), restaurant.getName(), restaurant.getAddress(), restaurant.getCuisineType(), restaurant.getOpeningHours(), UserEntity.fromDomain(restaurant.getOwner()));
    }
}
