package com.vitorbetmann.cleanresmapi.usecases.usertype;

import com.vitorbetmann.cleanresmapi.entities.usertype.UserType;
import com.vitorbetmann.cleanresmapi.usecases.usertype.interfaces.UserTypeGateway;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class ListUserTypesTest {

    @Mock
    UserTypeGateway userTypeGateway;

    @InjectMocks
    ListUserTypes listUserTypes;

    List<UserType> mockList;
    Integer mockId = 1;
    String mockName = "Owner";

    @Test
    void execute_whenNoUserTypesExist_returnsEmptyList() {
        // arrange
        mockList = new ArrayList<>();
        when(userTypeGateway.getAll()).thenReturn(mockList);

        // act
        var result = listUserTypes.execute();

        // assert
        assertEquals(0, result.size());
    }

    @Test
    void execute_whenUserTypesExist_returnsUserTypeList() {
        // arrange
        mockList = new ArrayList<>();
        var userType = new UserType(mockId, mockName);
        mockList.add(userType);
        when(userTypeGateway.getAll()).thenReturn(mockList);

        // act
        var result = listUserTypes.execute();

        // assert
        assertEquals(1, result.size());
    }

}
