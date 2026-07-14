package com.vitorbetmann.cleanresmapi.infrastructure.usertype;

import org.springframework.data.jpa.repository.JpaRepository;

public interface UserTypeRepository extends JpaRepository<UserTypeEntity, Long> {

    boolean existsByName(String name);

}
