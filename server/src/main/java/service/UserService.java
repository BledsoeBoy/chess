package service;

import dataAccess.*;
import handlers.requests.RegisterRequest;
import model.Auth;

public class UserService {
    public Auth register(RegisterRequest req) throws DataAccessException {
        var userDAO = new MemoryUserDAO();
        var user = userDAO.getUser(req.username());
        if (user == null) {
            userDAO.createUser(req);
            var authDAO = new MemoryAuthDAO();
            return authDAO.createAuth(req.username());
        }
        else {
            return null;
        }
    }

    public Auth login(String username, String password) throws DataAccessException {
        var userDAO = new MemoryUserDAO();
        var user = userDAO.getUser(username);
        if (user != null && user.password().equals(password)) {
            var authDAO = new MemoryAuthDAO();
            return authDAO.createAuth(username);
        } else {
            return null;
        }
    }

    public boolean logout(String authToken) throws DataAccessException {
        var authDAO = new MemoryAuthDAO();
        Auth auth = authDAO.getAuth(authToken);
        if (auth == null) {
            return false;
        } else {
            authDAO.deleteAuth(authToken);
            return true;
        }
    }
}
