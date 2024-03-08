package passoffTests.serverTests.unitTests;

import dataAccess.DataAccessException;
import dataAccess.MemoryAuthDAO;
import dataAccess.MemoryUserDAO;
import handlers.requests.RegisterRequest;
import model.Auth;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import service.UserService;


import static org.junit.jupiter.api.Assertions.assertNull;

public class UserServiceTest {
    @Test
    void positiveRegister() throws DataAccessException {
        var myObject = new UserService(new MemoryAuthDAO(), new MemoryUserDAO());

        String username = "sam";
        String password = "bobby";
        String email = "ruffy@gmail.com";

        RegisterRequest parameter1 = new RegisterRequest(username, password, email);
        String expected = "sam";

        Auth actual = myObject.register(parameter1);
        String actualResult = actual.username();

        Assertions.assertEquals(expected, actualResult);
    }
    @Test
    void negativeRegister() throws DataAccessException {
        UserService userService = new UserService(new MemoryAuthDAO(), new MemoryUserDAO());

        // Create a user with a specific username
        RegisterRequest existingUserRequest = new RegisterRequest("existingUser", "password123", "existing@example.com");
        userService.register(existingUserRequest);

        // Try to register the same user again
        RegisterRequest duplicateUserRequest = new RegisterRequest("existingUser", "newPassword", "new@example.com");
        Auth resultAuth = userService.register(duplicateUserRequest);

        assertNull(resultAuth);
    }
    @Test
    void positiveLogin() throws DataAccessException {
        var myObject = new UserService(new MemoryAuthDAO(), new MemoryUserDAO());

        String username = "juan";
        String password = "password1";
        String email = "hello@gmail.com";

        RegisterRequest parameter1 = new RegisterRequest(username, password, email);

        myObject.register(parameter1);

        Auth newAuth = myObject.login(username, password);
        String actual = newAuth.username();

        String expected = "juan";

        Assertions.assertEquals(expected, actual);
    }

    @Test
    void negativeLogin() throws DataAccessException {
        var myObject = new UserService(new MemoryAuthDAO(), new MemoryUserDAO());

        String username = "juan";
        String password = "password1";
        String email = "hello@gmail.com";

        RegisterRequest parameter1 = new RegisterRequest(username, password, email);

        myObject.register(parameter1);

        Auth newAuth = myObject.login(username, "bobby");

        Assertions.assertNull(newAuth);
    }
    @Test
    void positiveLogout() throws DataAccessException {
        var myObject = new UserService(new MemoryAuthDAO(), new MemoryUserDAO());

        String username = "juan";
        String password = "password1";
        String email = "hello@gmail.com";

        RegisterRequest parameter1 = new RegisterRequest(username, password, email);

        Auth auth = myObject.register(parameter1);
        String authToken = auth.authToken();

        var actual = myObject.logout(authToken);

        Assertions.assertTrue(actual);
    }

    @Test
    void negativeLogout() throws DataAccessException {
        var myObject = new UserService(new MemoryAuthDAO(), new MemoryUserDAO());

        String username = "jobe";
        String password = "something";
        String email = "happy@gmail.com";

        RegisterRequest parameter1 = new RegisterRequest(username, password, email);

        myObject.register(parameter1);

        var actual = myObject.logout("random");

        Assertions.assertFalse(actual);
    }
}
