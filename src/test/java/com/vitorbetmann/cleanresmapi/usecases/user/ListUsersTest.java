package com.vitorbetmann.cleanresmapi.usecases.user;

import com.vitorbetmann.cleanresmapi.entities.user.User;
import com.vitorbetmann.cleanresmapi.entities.usertype.UserType;
import com.vitorbetmann.cleanresmapi.usecases.user.interfaces.UserGateway;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class ListUsersTest {

    @Mock
    UserGateway userGateway;

    @InjectMocks
    ListUsers listUsers;

    List<User> mockList;
    Long mockId = 1L;
    String mockUserTypeName = "Owner";
    String mockName = "John";
    String mockEmail = "john@email.com";
    String mockPassword = "password123";
    UserType mockUserType = new UserType(mockId, mockUserTypeName);
    String mockAddress = "404 John's address";

    @Test
    void execute_whenNoUsersExist_returnsEmptyList() {
        // arrange
        mockList = new ArrayList<>();
        when(userGateway.getAll()).thenReturn(mockList);

        // act
        var result = listUsers.execute();

        // assert
        assertEquals(0, result.size());
    }

    @Test
    void execute_whenUsersExist_returnsUserList() {
        // arrange
        mockList = new ArrayList<>();
        var user = new User(mockId, mockName, mockEmail, mockPassword, mockUserType, mockAddress, LocalDateTime.now());
        mockList.add(user);
        when(userGateway.getAll()).thenReturn(mockList);

        // act
        var result = listUsers.execute();

        // assert
        assertEquals(1, result.size());
    }

}
