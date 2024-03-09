package handlers.requests;

public class JoinGameRequest {
    private String playerColor;
    private Integer gameID;
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
