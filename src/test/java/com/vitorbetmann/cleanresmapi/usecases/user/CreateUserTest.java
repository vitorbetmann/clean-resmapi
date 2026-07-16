package com.vitorbetmann.cleanresmapi.usecases.user;

import com.vitorbetmann.cleanresmapi.entities.user.User;
import com.vitorbetmann.cleanresmapi.entities.usertype.UserType;
import com.vitorbetmann.cleanresmapi.usecases.user.exceptions.UserDuplicateEmailException;
import com.vitorbetmann.cleanresmapi.usecases.user.interfaces.UserGateway;
import com.vitorbetmann.cleanresmapi.usecases.usertype.interfaces.UserTypeGateway;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class CreateUserTest {

    @Mock
    UserGateway userGateway;

    @Mock
    UserTypeGateway userTypeGateway;

    @InjectMocks
    CreateUser createUser;

    Long mockId = 1L;
    String mockUserTypeName = "Owner";
    String mockName = "John";
    String mockEmail = "john@email.com";
    String mockPassword = "password123";
    UserType mockUserType = new UserType(mockId, mockUserTypeName);
    String mockAddress = "404 John's address";

    @Test
    void execute_whenEmailIsUnique_savesAndReturnsUserType() {
        // arrange
        when(userGateway.isEmailUnique(mockEmail)).thenReturn(true);

        when(userTypeGateway.getById(mockId)).thenReturn(Optional.of(mockUserType));

        var saved = new User(mockId, mockName, mockEmail, mockPassword, mockUserType, mockAddress, LocalDateTime.now());
        when(userGateway.save(any(User.class))).thenReturn(saved);

        // act
        var result = createUser.execute(mockName, mockEmail, mockPassword, mockUserType.getId(), mockAddress);

        // assert
        assertEquals(mockId, result.getId());
        assertEquals(mockName, result.getName());
        assertEquals(mockEmail, result.getEmail());
        assertEquals(mockPassword, result.getPassword());
        assertEquals(mockUserType, result.getUserType());
        assertEquals(mockAddress, result.getAddress());
    }

    @Test
    void execute_whenNameIsDuplicate_throwsUserDuplicateEmailException() {
        // arrange
        when(userGateway.isEmailUnique(mockEmail)).thenReturn(false);

        // assert on act
        assertThrows(UserDuplicateEmailException.class, () -> createUser.execute(mockName, mockEmail, mockPassword, mockUserType.getId(), mockAddress));
        verify(userGateway, never()).save(any());
    }

}
