package handlers.requests;

public class JoinGameRequest {
    private String playerColor;
    private Integer gameID;
    public JoinGameRequest() {
        // Default constructor for Gson deserialization
    }
    public String getPlayerColor() {
        return playerColor;
    }
    public Integer getGameId() {
        return gameID;
    }
}
