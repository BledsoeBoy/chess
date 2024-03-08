package dataAccess;

import model.Game;

import java.util.Collection;

public interface GameDAO {
    void clearGames() throws DataAccessException;
    int createGame(Game game) throws DataAccessException;
    void updateGame(String playerColor, Game game, String username) throws DataAccessException;
    Game getGame(int gameId) throws DataAccessException;
    Collection<Game> listGames() throws DataAccessException;
}
