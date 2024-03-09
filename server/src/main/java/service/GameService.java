package service;

import chess.ChessGame;
import dataAccess.*;
import model.Auth;
import model.Game;

import java.util.Collection;

public class GameService {
    private final AuthDAO authDAO;
    private final GameDAO gameDAO;
    private static int gameId = 1;

    public GameService(AuthDAO authDAO, GameDAO gameDAO) {
        this.authDAO = authDAO;
        this.gameDAO = gameDAO;
    }
    public int createGame(String gameName) throws DataAccessException {
        if (gameName != null) {
            Game game = new Game(gameId, null, null, gameName, new ChessGame());
            gameId++;
            return gameDAO.createGame(game);
        }
        else {
            return 0;
        }
    }
    public int joinGame(String authToken, String playerColor, Integer gameId) throws DataAccessException {
        Auth auth = authDAO.getAuth(authToken);
        if (auth == null) {
            return -10;
        }

        Game game = gameDAO.getGame(gameId);

        if (playerColor == null) {
            return -5; // No color specified
        } else if ("BLACK".equals(playerColor) && (game.blackUsername() == null)) {
            gameDAO.updateGame(playerColor, game, auth.username());
            return -5; // Success, player joined as black
        } else if ("WHITE".equals(playerColor) && (game.whiteUsername() == null)) {
            gameDAO.updateGame(playerColor, game, auth.username());
            return -5; // Success, player joined as white
        } else {
            return -2; // Unable to join
        }
    }

    public Collection<Game> listGames(String authToken) throws DataAccessException {
        Auth auth = authDAO.getAuth(authToken);
        if (auth == null) {
            return null;
        }
        return gameDAO.listGames();
    }
}
