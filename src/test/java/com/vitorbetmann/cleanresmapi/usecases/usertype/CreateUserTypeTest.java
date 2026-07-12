package com.vitorbetmann.cleanresmapi.usecases.usertype;

import com.vitorbetmann.cleanresmapi.entities.usertype.UserType;
import com.vitorbetmann.cleanresmapi.usecases.usertype.exceptions.UserTypeDuplicateNameException;
import com.vitorbetmann.cleanresmapi.usecases.usertype.interfaces.UserTypeGateway;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class CreateUserTypeTest {

    @Mock
    UserTypeGateway userTypeGateway;

    @InjectMocks
    CreateUserType createUserType;

    Integer mockId = 1;
    String mockName = "Owner";

    @Test
    void execute_whenNameIsUnique_savesAndReturnsUserType() {
        // arrange
        when(userTypeGateway.isNameUnique(mockName)).thenReturn(true);

        var saved = new UserType(mockId, mockName);
        when(userTypeGateway.save(any(UserType.class))).thenReturn(saved);

        // act
        var result = createUserType.execute(mockName);

        // assert
        assertEquals(mockId, result.getId());
        assertEquals(mockName, result.getName());
    }

    @Test
    void execute_whenNameIsDuplicate_throwsUserTypeDuplicateNameException() {
        // arrange
        when(userTypeGateway.isNameUnique(mockName)).thenReturn(false);

        // assert on act
        assertThrows(UserTypeDuplicateNameException.class, () -> createUserType.execute(mockName));
        verify(userTypeGateway, never()).save(any());
    }

}
