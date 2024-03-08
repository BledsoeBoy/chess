package dataAccess;

import chess.ChessGame;
import model.Game;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class MemoryGameDAO implements GameDAO {
        private static int nextId = 1;
        private static final Map<Integer, Game> games = new HashMap<>();
        public void clearGames() {
                games.clear();
        }
        public Game getGame(int gameId) {
                return games.get(gameId);
        }
        public int createGame(Game game) {
                for (Map.Entry<Integer, Game> entry : games.entrySet()) {
                        nextId++;
                }
                var gameData = new Game(nextId, null, null, game.gameName(), game.game());

                games.put(gameData.gameID(), gameData);

                return gameData.gameID();
        }
        public void updateGame(String playerColor, Game game, String username) {
                var id = game.gameID();
                var gameName = game.gameName();
                var blackUsername = game.blackUsername();
                var whiteUsername = game.whiteUsername();
                var chessGame = game.game();

                if (playerColor.equals("WHITE")) {
                        Game newGameData = new Game(id, username, blackUsername, gameName, chessGame);
                        games.replace(id, newGameData);
                } else if (playerColor.equals("BLACK")) {
                        Game newGameData = new Game(id, whiteUsername, username, gameName, chessGame);
                        games.replace(id, newGameData);
                }
        }

        public Collection<Game> listGames() {
                Map<Integer, Game> gamesWithoutChessGame = new HashMap<>();

                for (Map.Entry<Integer, Game> entry : games.entrySet()) {
                        int gameId = entry.getKey();
                        Game originalGame = entry.getValue();
                        Game gameWithoutChessGame = new Game(
                                originalGame.gameID(),
                                originalGame.whiteUsername(),
                                originalGame.blackUsername(),
                                originalGame.gameName(),
                                null // Set ChessGame to null or use a default value
                        );

                        gamesWithoutChessGame.put(gameId, gameWithoutChessGame);
                }
                return gamesWithoutChessGame.values();
        }
}
