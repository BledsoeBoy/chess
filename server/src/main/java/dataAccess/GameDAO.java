package dataAccess;

import handlers.requests.RegisterRequest;

public interface GameDAO {
    void clearGames() throws DataAccessException;
    void createGame(String gameName) throws DataAccessException;
}
