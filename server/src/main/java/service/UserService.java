package service;

import dataAccess.*;
import handlers.requests.RegisterRequest;
import model.Auth;

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
        if (user == null) {
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
        if (user != null && user.password().equals(password)) {
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
