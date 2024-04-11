package passoffTests.serverTests.unitTests;

import dataAccess.*;
import server.requests.RegisterRequest;
import model.Auth;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import service.AuthService;
import service.UserService;

public class AuthServiceTest {
    @Test
    void clearApp() throws DataAccessException {
        var userService = new UserService(new MemoryAuthDAO(), new MemoryUserDAO());
        var myObject = new AuthService(new MemoryAuthDAO(), new MemoryGameDAO(), new MemoryUserDAO());

        String username = "juan";
        String password = "password1";
        String email = "hello@gmail.com";

        RegisterRequest parameter1 = new RegisterRequest(username, password, email);

        Auth auth = userService.register(parameter1);
        // Check if auth is not null before accessing its properties
        String token = (auth != null) ? auth.authToken() : null;

        var userDao = new MemoryUserDAO();
        var authDao = new MemoryAuthDAO();

        myObject.clearApp();

        boolean isNull = (userDao.getUser("sam")) == null && (authDao.getAuth(token) == null);

        Assertions.assertTrue(isNull);
    }
}
