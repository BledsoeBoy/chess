package service;

import dataAccess.DataAccessException;
import dataAccess.MemoryAuthDAO;
import dataAccess.MemoryGameDAO;
import dataAccess.MemoryUserDAO;

public class AuthService {
    public int clearApp(MemoryAuthDAO authDao, MemoryGameDAO gameDao) throws DataAccessException {
        var userDAO = new MemoryUserDAO();

        userDAO.clearUsers();
        gameDao.clearGames();
        authDao.clearAuths();

        return 0;
    }
}
