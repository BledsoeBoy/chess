package webSocketMessages.userCommands;

import chess.ChessGame;
import chess.ChessMove;
import com.google.gson.Gson;

import java.util.Objects;

/**
 * Represents a command a user can send the server over a websocket
 * 
 * Note: You can add to this class, but you should not alter the existing
 * methods.
 */
public class UserGameCommand {  //client to server (Action)

    public UserGameCommand(String authToken) {
        this.authToken = authToken;
    }

    public enum CommandType {
        JOIN_PLAYER,
        JOIN_OBSERVER,
        MAKE_MOVE,
        LEAVE,
        RESIGN
    }

    protected CommandType commandType;

    private final String authToken;
    protected ChessGame.TeamColor playerColor = null;
    protected Integer gameID = null;
    protected ChessMove move = null;

    public String getAuthString() {
        return authToken;
    }

    public CommandType getCommandType() {
        return this.commandType;
    }

    public String toString() {
        return new Gson().toJson(this);
    }
    public ChessGame.TeamColor getPlayerColor() {
        return playerColor;
    }
    public Integer getGameID() {
        return gameID;
    }
    public ChessMove getMove() {
        return move;
    }
    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (!(o instanceof UserGameCommand that))
            return false;
        return getCommandType() == that.getCommandType() && Objects.equals(getAuthString(), that.getAuthString());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getCommandType(), getAuthString());
    }
}