package com.vitorbetmann.cleanresmapi.infrastructure.user;

import com.vitorbetmann.cleanresmapi.infrastructure.user.dtos.CreateUserRequest;
import com.vitorbetmann.cleanresmapi.infrastructure.user.dtos.UpdateUserRequest;
import com.vitorbetmann.cleanresmapi.infrastructure.user.dtos.UserResponse;
import com.vitorbetmann.cleanresmapi.usecases.user.*;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/users")
public class UserController {

    private final CreateUser createUser;
    private final DeleteUser deleteUser;
    private final UpdateUser updateUser;
    private final GetUser getUser;
    private final ListUsers listUsers;

    public UserController(CreateUser createUser, DeleteUser deleteUser, UpdateUser updateUser, GetUser getUser, ListUsers listUsers) {
        this.createUser = createUser;
        this.deleteUser = deleteUser;
        this.updateUser = updateUser;
        this.getUser = getUser;
        this.listUsers = listUsers;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public UserResponse create(@RequestBody @Valid CreateUserRequest request) {
        var user = this.createUser.execute(request.name(), request.email(), request.password(), request.userTypeId(), request.address());
        return UserResponse.fromDomain(user);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id) {
        this.deleteUser.execute(id);
    }

    @GetMapping("/{id}")
    public UserResponse get(@PathVariable Long id) {
        var user = this.getUser.execute(id);
        return UserResponse.fromDomain(user);
    }

    @GetMapping
    public List<UserResponse> getAll() {
        var users = listUsers.execute();
        return users.stream().map(UserResponse::fromDomain).toList();
    }

    @PutMapping("/{id}")
    public UserResponse update(@PathVariable Long id, @RequestBody @Valid UpdateUserRequest request) {
        var user = this.updateUser.execute(id, request.name(), request.email(), request.password(), request.userTypeId(), request.address());
        return UserResponse.fromDomain(user);
    }
}
