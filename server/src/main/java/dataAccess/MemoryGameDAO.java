package dataAccess;

import chess.ChessGame;
import model.Game;

import java.util.HashMap;

public class MemoryGameDAO implements GameDAO {
        private int nextId = 1;
        private static final HashMap<String, Game> games = new HashMap<>();
        public void clearGames() {
                games.clear();
        }
        public Game getGame(String gameName) {
                return games.get(gameName);
        }
        public void createGame(String gameName) {
                var game = new Game(nextId++, null, null, gameName, new ChessGame());

                games.put(gameName, game);
        }
}
