package com.vitorbetmann.cleanresmapi.infrastructure.usertype;

import com.vitorbetmann.cleanresmapi.entities.usertype.UserType;
import com.vitorbetmann.cleanresmapi.usecases.usertype.interfaces.UserTypeGateway;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
public class UserTypeGatewayImpl implements UserTypeGateway {

    private final UserTypeRepository userTypeRepository;

    public UserTypeGatewayImpl(UserTypeRepository userTypeRepository) {
        this.userTypeRepository = userTypeRepository;
    }

    @Override
    public UserType save(UserType userType) {
        var entity = UserTypeEntity.fromDomain(userType);
        var savedEntity = this.userTypeRepository.save(entity);
        return savedEntity.toDomain();
    }

    @Override
    public Optional<UserType> getById(Long id) {
        return this.userTypeRepository.findById(id).map(UserTypeEntity::toDomain);
    }

    @Override
    public List<UserType> getAll() {
        return this.userTypeRepository.findAll()
                .stream()
                .map(UserTypeEntity::toDomain)
                .toList();
    }

    @Override
    public void delete(Long id) {
        this.userTypeRepository.deleteById(id);
    }

    @Override
    public boolean isNameUnique(String name) {
        return !userTypeRepository.existsByName(name);
    }
}
