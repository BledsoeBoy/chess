package dataAccess;

import model.Game;

import java.util.Collection;

public interface GameDAO {
    void clearGames() throws DataAccessException;
    int createGame(String gameName) throws DataAccessException;
    void updateGame(String playerColor, Game game, String username) throws DataAccessException;
    Collection<Game> listGames();
}
