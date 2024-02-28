package service;

import dataAccess.DataAccessException;
import dataAccess.MemoryAuthDAO;
import dataAccess.MemoryGameDAO;
import dataAccess.MemoryUserDAO;

public class AuthService {
    public Object clearApp() throws DataAccessException {
        var userDAO = new MemoryUserDAO();
        var gameDAO = new MemoryGameDAO();
        var authDAO = new MemoryAuthDAO();
        userDAO.clearUsers();
        gameDAO.clearGames();
        authDAO.clearAuths();

        return "";
    }
}
