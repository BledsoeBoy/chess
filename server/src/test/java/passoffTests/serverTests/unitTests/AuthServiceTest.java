package passoffTests.serverTests.unitTests;

import dataAccess.DataAccessException;
import dataAccess.MemoryAuthDAO;
import dataAccess.MemoryUserDAO;
import handlers.requests.RegisterRequest;
import model.Auth;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import service.AuthService;
import service.UserService;

public class AuthServiceTest {
    @Test
    void clearApp() throws DataAccessException {
        var userService = new UserService();
        var myObject = new AuthService();

        String username = "juan";
        String password = "password1";
        String email = "hello@gmail.com";

        RegisterRequest parameter1 = new RegisterRequest(username, password, email);

        Auth auth = userService.register(parameter1);
        String token = auth.authToken();

        var userDAO = new MemoryUserDAO();
        var authDAO = new MemoryAuthDAO();

        myObject.clearApp();

        boolean isNull = false;

        if ((userDAO.getUser("sam")) == null && (authDAO.getAuth(token) == null)) {
            isNull = true;
        }

        Assertions.assertTrue(isNull);
    }
}
