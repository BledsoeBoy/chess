package server.requests;

public class JoinGameRequest {
    private final String playerColor;
    private final Integer gameID;
    public JoinGameRequest(String playerColor, Integer gameID) {
        // Default constructor for Gson deserialization
        this.playerColor = playerColor;
        this.gameID = gameID;
    }
    public String getPlayerColor() {
        return playerColor;
    }
    public Integer getGameId() {
        return gameID;
    }
}
