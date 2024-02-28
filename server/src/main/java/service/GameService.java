package service;

import dataAccess.DataAccessException;
import dataAccess.MemoryAuthDAO;
import dataAccess.MemoryGameDAO;
import dataAccess.MemoryUserDAO;

public class GameService {
    public int createGame(String authToken, String gameName) throws DataAccessException {
        var gameDAO = new MemoryGameDAO();
        var game = gameDAO.getGame(gameName);
        if (game == null) {
            return gameDAO.createGame(gameName);;
        }
        else {
            return 1;
        }
        return gameId;
    }
}
