package com.vitorbetmann.cleanresmapi.infrastructure.user;

import com.vitorbetmann.cleanresmapi.usecases.user.*;
import com.vitorbetmann.cleanresmapi.usecases.user.interfaces.UserGateway;
import com.vitorbetmann.cleanresmapi.usecases.usertype.interfaces.UserTypeGateway;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class UserConfig {

    @Bean
    public CreateUser createUser(UserGateway userGateway, UserTypeGateway userTypeGateway) {
        return new CreateUser(userGateway, userTypeGateway);
    }

    @Bean
    public DeleteUser deleteUser(UserGateway userGateway) {
        return new DeleteUser(userGateway);
    }

    @Bean
    public GetUser getUser(UserGateway userGateway) {
        return new GetUser(userGateway);
    }

    @Bean
    public ListUsers listUsers(UserGateway userGateway) {
        return new ListUsers(userGateway);
    }

    @Bean
    public UpdateUser updateUser(UserGateway userGateway, UserTypeGateway userTypeGateway) {
        return new UpdateUser(userGateway, userTypeGateway);
    }
}
