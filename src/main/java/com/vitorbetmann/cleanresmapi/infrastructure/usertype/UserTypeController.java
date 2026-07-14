package com.vitorbetmann.cleanresmapi.infrastructure.usertype;

import com.vitorbetmann.cleanresmapi.infrastructure.usertype.dtos.CreateUserTypeRequest;
import com.vitorbetmann.cleanresmapi.infrastructure.usertype.dtos.UpdateUserTypeRequest;
import com.vitorbetmann.cleanresmapi.infrastructure.usertype.dtos.UserTypeResponse;
import com.vitorbetmann.cleanresmapi.usecases.usertype.*;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/user-types")
public class UserTypeController {

    private final CreateUserType createUserType;
    private final DeleteUserType deleteUserType;
    private final UpdateUserType updateUserType;
    private final GetUserType getUserType;
    private final ListUserTypes listUserTypes;

    public UserTypeController(CreateUserType createUserType, DeleteUserType deleteUserType, UpdateUserType updateUserType, GetUserType getUserType, ListUserTypes listUserTypes) {
        this.createUserType = createUserType;
        this.deleteUserType = deleteUserType;
        this.updateUserType = updateUserType;
        this.getUserType = getUserType;
        this.listUserTypes = listUserTypes;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public UserTypeResponse create(@RequestBody @Valid CreateUserTypeRequest request) {
        var userType = this.createUserType.execute(request.name());
        return UserTypeResponse.fromDomain(userType);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id) {
        this.deleteUserType.execute(id);
    }

    @GetMapping("/{id}")
    public UserTypeResponse get(@PathVariable Long id) {
        var userType = this.getUserType.execute(id);
        return UserTypeResponse.fromDomain(userType);
    }

    @GetMapping
    public List<UserTypeResponse> getAll() {
        var userTypes = listUserTypes.execute();
        return userTypes.stream().map(UserTypeResponse::fromDomain).toList();
    }

    @PutMapping("/{id}")
    public UserTypeResponse update(@PathVariable Long id, @RequestBody @Valid UpdateUserTypeRequest request) {
        var userType = this.updateUserType.execute(id, request.name());
        return UserTypeResponse.fromDomain(userType);
    }
}
