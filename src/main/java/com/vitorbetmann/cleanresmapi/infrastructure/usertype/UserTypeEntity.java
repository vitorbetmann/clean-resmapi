package com.vitorbetmann.cleanresmapi.infrastructure.usertype;

import com.vitorbetmann.cleanresmapi.entities.usertype.UserType;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;


@Entity
@NoArgsConstructor
@Getter
public class UserTypeEntity {

    @Id
    @GeneratedValue
    private Long id;

    @Column(nullable = false, unique = true)
    private String name;

    public UserTypeEntity(Long id, String name) {
        this.id = id;
        this.name = name;
    }

    public UserType toDomain() {
        return new UserType(this.id, this.name);
    }

    public static UserTypeEntity fromDomain(UserType userType) {
        return new UserTypeEntity(userType.getId(), userType.getName());
    }
}
