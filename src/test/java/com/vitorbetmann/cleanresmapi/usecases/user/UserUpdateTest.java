package com.vitorbetmann.cleanresmapi.usecases.user;

import com.vitorbetmann.cleanresmapi.entities.user.User;
import com.vitorbetmann.cleanresmapi.entities.usertype.UserType;
import com.vitorbetmann.cleanresmapi.usecases.user.exceptions.UserDuplicateEmailException;
import com.vitorbetmann.cleanresmapi.usecases.user.exceptions.UserNotFoundException;
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
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class UserUpdateTest {

    @Mock
    UserGateway userGateway;

    @Mock
    UserTypeGateway userTypeGateway;

    @InjectMocks
    UpdateUser updateUser;

    Long mockId = 1L;
    String mockUserTypeName = "Owner";
    String mockName = "John";
    String mockEmail1 = "john@email.com";
    String mockEmail2 = "john@new-email.com";
    String mockPassword = "password123";
    UserType mockUserType = new UserType(mockId, mockUserTypeName);
    String mockAddress = "404 John's address";

    @Test
    void execute_whenIdIsNotFound_throwsUserNotFoundException() {
        // arrange
        when(userGateway.getById(mockId)).thenReturn(Optional.empty());

        // assert on act
        assertThrows(UserNotFoundException.class, () -> updateUser.execute(mockId, mockName, mockEmail1, mockPassword, mockUserType, mockAddress));
    }

    @Test
    void execute_whenEmailIsUnchanged_returnsUserWithoutSaving() {
        // arrange
        var saved = new User(mockId, mockName, mockEmail1, mockPassword, mockUserType, mockAddress, null);
        when(userGateway.getById(mockId)).thenReturn(Optional.of(saved));

        when(userTypeGateway.getById(mockId)).thenReturn(Optional.of(mockUserType));

        // act
        updateUser.execute(mockId, mockName, mockEmail1, mockPassword, mockUserType, mockAddress);

        // assert
        verify(userGateway, never()).isEmailUnique(mockEmail1);
        verify(userGateway).save(any(User.class));
    }

    @Test
    void execute_whenEmailIsDuplicate_throwsUserDuplicateEmailException() {
        // arrange
        var saved = new User(mockId, mockName, mockEmail1, mockPassword, mockUserType, mockAddress, null);
        when(userGateway.getById(mockId)).thenReturn(Optional.of(saved));

        when(userGateway.isEmailUnique(mockEmail2)).thenReturn(false);

        // assert on act
        assertThrows(UserDuplicateEmailException.class, () -> updateUser.execute(mockId, mockName, mockEmail2, mockPassword, mockUserType, mockAddress));
    }

    @Test
    void execute_whenEmailIsChanged_updatesAndReturnsUser() {
        // arrange
        var saved1 = new User(mockId, mockName, mockEmail1, mockPassword, mockUserType, mockAddress, LocalDateTime.now());
        when(userGateway.getById(mockId)).thenReturn(Optional.of(saved1));

        when(userGateway.isEmailUnique(mockEmail2)).thenReturn(true);

        when(userTypeGateway.getById(mockId)).thenReturn(Optional.of(mockUserType));

        var saved2 = new User(mockId, mockName, mockEmail2, mockPassword, mockUserType, mockAddress, null);
        when(userGateway.save(any(User.class))).thenReturn(saved2);

        // act
        var result = updateUser.execute(mockId, mockName, mockEmail2, mockPassword, mockUserType, mockAddress);

        // assert
        assertEquals(mockEmail2, result.getEmail());
        assertEquals(mockId, result.getId());
    }

}
