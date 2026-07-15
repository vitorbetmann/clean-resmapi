package com.vitorbetmann.cleanresmapi.usecases.user;

import com.vitorbetmann.cleanresmapi.entities.user.User;
import com.vitorbetmann.cleanresmapi.entities.usertype.UserType;
import com.vitorbetmann.cleanresmapi.usecases.user.exceptions.UserNotFoundException;
import com.vitorbetmann.cleanresmapi.usecases.user.interfaces.UserGateway;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class DeleteUserTest {

    @Mock
    UserGateway userGateway;

    @InjectMocks
    DeleteUser deleteUser;

    Long mockId = 1L;
    String mockUserTypeName = "Owner";
    String mockName = "John";
    String mockEmail = "john@email.com";
    String mockPassword = "password123";
    UserType mockUserType = new UserType(mockId, mockUserTypeName);
    String mockAddress = "404 John's address";

    @Test
    void execute_whenIdIsFound_deletesUser() {
        // arrange
        var saved = new User(mockId, mockName, mockEmail, mockPassword, mockUserType, mockAddress, LocalDateTime.now());
        when(userGateway.getById(mockId)).thenReturn(Optional.of(saved));

        // act
        deleteUser.execute(mockId);

        //assert
        verify(userGateway).delete(mockId);
    }

    @Test
    void execute_whenIdIsNotFound_throwsUserNotFoundException() {
        // arrange
        when(userGateway.getById(mockId)).thenReturn(Optional.empty());

        // assert on act
        assertThrows(UserNotFoundException.class, () -> deleteUser.execute(mockId));
        verify(userGateway, never()).delete(mockId);
    }

}
