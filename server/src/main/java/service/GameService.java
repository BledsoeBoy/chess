package service;

import dataAccess.DataAccessException;
import dataAccess.MemoryAuthDAO;
import dataAccess.MemoryGameDAO;
import model.Auth;
import model.Game;

import java.util.Collection;
import java.util.Objects;

public class GameService {
    public int createGame(String gameName) throws DataAccessException {
        var gameDAO = new MemoryGameDAO();
        if (gameName != null) {
            return gameDAO.createGame(gameName);
        }
        else {
            return 0;
        }
    }
    public int joinGame(String authToken, String playerColor, Integer gameId) throws DataAccessException {
        var authDAO = new MemoryAuthDAO();
        Auth auth = authDAO.getAuth(authToken);

        var gameDAO = new MemoryGameDAO();
        Game game = gameDAO.getGame(gameId);

        if (playerColor == null) {
            return -5; // No color specified
        } else if ("BLACK".equals(playerColor) && (game.blackUsername() == null || Objects.equals(auth.username(), game.blackUsername()))) {
            gameDAO.updateGame(playerColor, game, auth.username());
            return -5; // Success, player joined as black
        } else if ("WHITE".equals(playerColor) && (game.whiteUsername() == null || Objects.equals(auth.username(), game.whiteUsername()))) {
            gameDAO.updateGame(playerColor, game, auth.username());
            return -5; // Success, player joined as white
        } else {
            return -2; // Unable to join
        }
    }

    public Collection<Game> listGames(String authToken) throws DataAccessException {
        var authDao = new MemoryAuthDAO();
        Auth auth = authDao.getAuth(authToken);
        if (auth == null) {
            return null;
        }

        var gameDAO = new MemoryGameDAO();
        return gameDAO.listGames();
    }
}
