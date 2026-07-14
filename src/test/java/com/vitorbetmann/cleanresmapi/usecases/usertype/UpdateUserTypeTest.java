package com.vitorbetmann.cleanresmapi.usecases.usertype;

import com.vitorbetmann.cleanresmapi.entities.usertype.UserType;
import com.vitorbetmann.cleanresmapi.usecases.usertype.exceptions.UserTypeDuplicateNameException;
import com.vitorbetmann.cleanresmapi.usecases.usertype.exceptions.UserTypeNotFoundException;
import com.vitorbetmann.cleanresmapi.usecases.usertype.interfaces.UserTypeGateway;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class UpdateUserTypeTest {

    @Mock
    UserTypeGateway userTypeGateway;

    @InjectMocks
    UpdateUserType updateUserType;

    Long mockId = 1L;
    String mockName1 = "Owner";
    String mockName2 = "Customer";


    @Test
    void execute_whenIdIsNotFound_throwsUserTypeNotFoundException() {
        // arrange
        when(userTypeGateway.getById(mockId)).thenReturn(Optional.empty());

        // assert on act
        assertThrows(UserTypeNotFoundException.class, () -> updateUserType.execute(mockId, mockName1));
    }

    @Test
    void execute_whenNameIsUnchanged_returnsUserTypeWithoutSaving() {
        // arrange
        var saved = new UserType(mockId, mockName1);
        when(userTypeGateway.getById(mockId)).thenReturn(Optional.of(saved));

        // act
        updateUserType.execute(mockId, mockName1);

        // assert
        verify(userTypeGateway, never()).save(saved);
    }

    @Test
    void execute_whenNameIsDuplicate_throwsUserTypeDuplicateNameException() {
        // arrange
        var saved = new UserType(mockId, mockName1);
        when(userTypeGateway.getById(mockId)).thenReturn(Optional.of(saved));

        when(userTypeGateway.isNameUnique(mockName2)).thenReturn(false);

        // assert on act
        assertThrows(UserTypeDuplicateNameException.class, () -> updateUserType.execute(mockId, mockName2));
    }

    @Test
    void execute_whenNameIsChanged_updatesAndReturnsUserType() {
        // arrange
        var saved1 = new UserType(mockId, mockName1);
        when(userTypeGateway.getById(mockId)).thenReturn(Optional.of(saved1));

        when(userTypeGateway.isNameUnique(mockName2)).thenReturn(true);

        var saved2 = new UserType(mockId, mockName2);
        when(userTypeGateway.save(any(UserType.class))).thenReturn(saved2);

        // act
        var result = updateUserType.execute(mockId, mockName2);

        // assert
        assertEquals(mockName2, result.getName());
        assertEquals(mockId, result.getId());
    }
}
