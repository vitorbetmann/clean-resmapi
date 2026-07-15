package com.vitorbetmann.cleanresmapi.entities.user;

import com.vitorbetmann.cleanresmapi.entities.usertype.UserType;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
public class UserTest {

    Long mockId = 1L;
    String mockUserTypeName = "Owner";
    String mockName = "John";
    String mockEmail = "john@email.com";
    String mockPassword = "password123";
    UserType mockUserType = new UserType(mockId, mockUserTypeName);
    String mockAddress = "404 John's address";

    @Test
    void constructor_whenEmailIsValid_returnsNewUser() {
        var user = new User(mockId, mockName, mockEmail, mockPassword, mockUserType, mockAddress, null);
        assertEquals(mockId, user.getId());
        assertEquals(mockName, user.getName());
        assertEquals(mockEmail, user.getEmail());
        assertEquals(mockPassword, user.getPassword());
        assertEquals(mockUserType, user.getUserType());
        assertEquals(mockAddress, user.getAddress());
    }

    @Test
    void constructor_whenNameIsNull_throwsIllegalArgumentException() {
        Exception exception = assertThrows(
                IllegalArgumentException.class,
                () -> new User(mockId, null, mockEmail, mockPassword, mockUserType, mockAddress, LocalDateTime.now())
        );

        String expectedMessage = "Name cannot be null or blank.";
        String actualMessage = exception.getMessage();

        assertTrue(actualMessage.contains(expectedMessage));
    }

    @Test
    void constructor_whenEmailIsNull_throwsIllegalArgumentException() {
        Exception exception = assertThrows(
                IllegalArgumentException.class,
                () -> new User(mockId, mockName, null, mockPassword, mockUserType, mockAddress, LocalDateTime.now())
        );

        String expectedMessage = "Email cannot be null or blank.";
        String actualMessage = exception.getMessage();

        assertTrue(actualMessage.contains(expectedMessage));
    }

    @Test
    void constructor_whenPasswordIsNull_throwsIllegalArgumentException() {
        Exception exception = assertThrows(
                IllegalArgumentException.class,
                () -> new User(mockId, mockName, mockEmail, null, mockUserType, mockAddress, LocalDateTime.now())
        );

        String expectedMessage = "Password cannot be null or blank.";
        String actualMessage = exception.getMessage();

        assertTrue(actualMessage.contains(expectedMessage));
    }

    @Test
    void constructor_whenUserIsNull_throwsIllegalArgumentException() {
        Exception exception = assertThrows(
                IllegalArgumentException.class,
                () -> new User(mockId, mockName, mockEmail, mockPassword, null, mockAddress, LocalDateTime.now())
        );

        String expectedMessage = "User Type cannot be null.";
        String actualMessage = exception.getMessage();

        assertTrue(actualMessage.contains(expectedMessage));
    }

    @Test
    void constructor_whenAddressIsBlank_throwsIllegalArgumentException() {
        Exception exception = assertThrows(
                IllegalArgumentException.class,
                () -> new User(mockId, mockName, mockEmail, mockPassword, mockUserType, null, LocalDateTime.now())
        );
        String expectedMessage = "Address cannot be null or blank.";
        String actualMessage = exception.getMessage();

        assertTrue(actualMessage.contains(expectedMessage));
    }

    @Test
    void constructor_whenLastModifiedDateIsProvided_preservesGivenValue() {
        var now = LocalDateTime.now();
        var user = new User(mockId, mockName, mockEmail, mockPassword, mockUserType, mockAddress, now);
        assertEquals(now, user.getLastModifiedDate());
    }

    @Test
    void create_returnsUserWithNullId() {
        User user = User.create(mockName, mockEmail, mockPassword, mockUserType, mockAddress);
        assertNull(user.getId());
    }

}
