package com.vitorbetmann.cleanresmapi.entities;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class UserTypeTest {

    @Test
    void constructor_whenNameIsValid_returnsNewUserType() {
        UserType userType = new UserType(1, "mock");
        assertEquals("mock", userType.getName());
        assertEquals(1, userType.getId());
    }

    @Test
    void constructor_whenNameIsNull_throwsIllegalArgumentException() {
        Exception exception = assertThrows(IllegalArgumentException.class, () -> new UserType(null, null));

        String expectedMessage = "Name cannot be null or blank.";
        String actualMessage = exception.getMessage();

        assertTrue(actualMessage.contains(expectedMessage));
    }

    @Test
    void constructor_whenNameIsBlank_throwsIllegalArgumentException() {
        Exception exception = assertThrows(IllegalArgumentException.class, () -> new UserType(null, ""));

        String expectedMessage = "Name cannot be null or blank.";
        String actualMessage = exception.getMessage();

        assertTrue(actualMessage.contains(expectedMessage));
    }

    @Test
    void create_returnsUserTypeWithNullId() {
        UserType userType = UserType.create("mock");
        assertNull(userType.getId());
    }

}
