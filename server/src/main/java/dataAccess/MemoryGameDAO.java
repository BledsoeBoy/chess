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
        public int createGame(String gameName) {
                ChessGame game = new ChessGame();

                for (Map.Entry<Integer, Game> entry : games.entrySet()) {
                        nextId++;
                }
                var gameData = new Game(nextId, null, null, gameName, game);


                games.put(gameData.gameID(), gameData);

                return gameData.gameID();
        }
        public void updateGame(String playerColor, Game game, String username) {
                var id = game.gameID();
                var gameName = game.gameName();
                var chessGame = game.game();

                if (playerColor.equals("WHITE")) {
                        Game newGameData = new Game(id, username, null, gameName, chessGame);
                        games.replace(id, newGameData);
                } else if (playerColor.equals("BLACK")) {
                        Game newGameData = new Game(id, null, username, gameName, chessGame);
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
