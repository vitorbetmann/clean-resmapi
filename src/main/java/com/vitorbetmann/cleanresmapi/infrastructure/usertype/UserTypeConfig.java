package com.vitorbetmann.cleanresmapi.infrastructure.usertype;

import com.vitorbetmann.cleanresmapi.usecases.usertype.*;
import com.vitorbetmann.cleanresmapi.usecases.usertype.interfaces.UserTypeGateway;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class UserTypeConfig {

    @Bean
    public CreateUserType createUserType(UserTypeGateway userTypeGateway) {
        return new CreateUserType(userTypeGateway);
    }

    @Bean
    public DeleteUserType deleteUserType(UserTypeGateway userTypeGateway) {
        return new DeleteUserType(userTypeGateway);
    }

    @Bean
    public GetUserType getUserType(UserTypeGateway userTypeGateway) {
        return new GetUserType(userTypeGateway);
    }

    @Bean
    public ListUserTypes listUserTypes(UserTypeGateway userTypeGateway) {
        return new ListUserTypes(userTypeGateway);
    }

    @Bean
    public UpdateUserType updateUserType(UserTypeGateway userTypeGateway) {
        return new UpdateUserType(userTypeGateway);
    }
}
