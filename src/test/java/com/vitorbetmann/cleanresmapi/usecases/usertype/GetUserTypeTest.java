package com.vitorbetmann.cleanresmapi.usecases.usertype;

import com.vitorbetmann.cleanresmapi.entities.usertype.UserType;
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
public class GetUserTypeTest {

    @Mock
    UserTypeGateway userTypeGateway;

    @InjectMocks
    GetUserType getUserType;

    Integer mockId = 1;
    String mockName = "Owner";

    @Test
    void execute_whenIdIsFound_ReturnsUserType() {
        // arrange
        var saved = new UserType(mockId, mockName);
        when(userTypeGateway.getById(mockId)).thenReturn(Optional.of(saved));

        // act
        var result = getUserType.execute(mockId);

        // assert
        assertEquals(mockId, result.getId());
        assertEquals(mockName, result.getName());
    }

    @Test
    void execute_whenIdIsNotFound_throwsUserTypeNotFoundException() {
        // arrange
        when(userTypeGateway.getById(mockId)).thenReturn(Optional.empty());

        // assert on act
        assertThrows(UserTypeNotFoundException.class, () -> getUserType.execute(mockId));
    }
}
