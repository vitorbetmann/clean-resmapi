package com.vitorbetmann.cleanresmapi.infrastructure.user;

import com.vitorbetmann.cleanresmapi.entities.user.User;
import com.vitorbetmann.cleanresmapi.usecases.user.interfaces.UserGateway;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
public class UserGatewayImpl implements UserGateway {

    private final UserRepository userRepository;

    public UserGatewayImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public User save(User user) {
        var entity = UserEntity.fromDomain(user);
        var savedEntity = this.userRepository.save(entity);
        return savedEntity.toDomain();
    }

    @Override
    public Optional<User> getById(Long id) {
        return this.userRepository.findById(id).map(UserEntity::toDomain);
    }

    @Override
    public List<User> getAll() {
        return this.userRepository.findAll()
                .stream()
                .map(UserEntity::toDomain)
                .toList();
    }

    @Override
    public void delete(Long id) {
        this.userRepository.deleteById(id);

    }

    @Override
    public boolean isEmailUnique(String email) {
        return !this.userRepository.existsByEmail(email);
    }
}
