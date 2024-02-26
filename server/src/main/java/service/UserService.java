package service;

import dataAccess.*;
import handlers.RegisterRequest;
import model.Auth;
import model.User;

public class UserService {
    public Auth register(RegisterRequest req) throws DataAccessException {
        var userDAO = new MemoryUserDAO();
        userDAO.createUser(req);
        var authDAO = new MemoryAuthDAO();
        return authDAO.createAuth(req);
    }
    //public Auth login(User user) {}
    public void logout(User user) {}
}
