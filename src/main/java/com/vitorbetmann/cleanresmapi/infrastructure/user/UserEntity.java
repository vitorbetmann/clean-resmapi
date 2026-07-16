package com.vitorbetmann.cleanresmapi.infrastructure.user;

import com.vitorbetmann.cleanresmapi.entities.user.User;
import com.vitorbetmann.cleanresmapi.infrastructure.usertype.UserTypeEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@NoArgsConstructor
@Getter
public class UserEntity {

    @Id
    @GeneratedValue
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false)
    private String password;

    @ManyToOne
    @JoinColumn(nullable = false)
    private UserTypeEntity userTypeEntity;

    @Column(nullable = false)
    private String address;

    @Column(nullable = false)
    private LocalDateTime lastModifiedDate;

    public UserEntity(Long id, String name, String email, String password, UserTypeEntity userTypeEntity, String address, LocalDateTime lastModifiedDate) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.password = password;
        this.userTypeEntity = userTypeEntity;
        this.address = address;
        this.lastModifiedDate = lastModifiedDate;
    }

    public User toDomain() {
        return new User(this.id, this.name, this.email, this.password, this.userTypeEntity.toDomain(), this.address, this.lastModifiedDate);
    }

    public static UserEntity fromDomain(User user) {
        return new UserEntity(user.getId(), user.getName(), user.getEmail(), user.getPassword(), UserTypeEntity.fromDomain(user.getUserType()), user.getAddress(), user.getLastModifiedDate());
    }
}
