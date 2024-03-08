package model;

import chess.ChessGame;
import com.google.gson.Gson;

public record Game(int gameID, String whiteUsername, String blackUsername, String gameName, ChessGame game) {
    public Game setGameID(int gameID) {
        return new Game(gameID, this.whiteUsername, this.blackUsername, this.gameName, this.game);
    }
    public String toString() {
        return new Gson().toJson(this);
    }
}
