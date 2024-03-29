package service;

import dataAccess.*;
import server.requests.RegisterRequest;
import model.Auth;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.UUID;

public class UserService {
    private final AuthDAO authDAO;
    private final UserDAO userDAO;

    public UserService(AuthDAO authDAO, UserDAO userDAO) {
        this.authDAO = authDAO;
        this.userDAO = userDAO;
    }
    public Auth register(RegisterRequest req) throws DataAccessException {
        var user = userDAO.getUser(req.username());
        var authToken = UUID.randomUUID().toString();
        Auth auth = new Auth(authToken, req.username());
        if (user == null && req.password() != null) {
            userDAO.createUser(req);
            return authDAO.createAuth(auth);
        }
        else {
            return null;
        }
    }

    public Auth login(String username, String password) throws DataAccessException {
        var user = userDAO.getUser(username);
        var authToken = UUID.randomUUID().toString();
        Auth auth = new Auth(authToken, username);

        if (user == null) {
            return null;
        }

        var hashedPassword = user.password();

        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();

        if (user != null && encoder.matches(password, hashedPassword)) {
            return authDAO.createAuth(auth);
        } else {
            return null;
        }
    }

    public boolean logout(String authToken) throws DataAccessException {
        Auth auth = authDAO.getAuth(authToken);
        if (auth == null) {
            return false;
        } else {
            authDAO.deleteAuth(authToken);
            return true;
        }
    }
}
