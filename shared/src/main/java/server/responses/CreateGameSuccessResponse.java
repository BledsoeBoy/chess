package server.responses;

public class CreateGameSuccessResponse {
    private final int gameID;

    public CreateGameSuccessResponse(int gameID) {
        this.gameID = gameID;
    }
    public int getGameID() {
        return gameID;
    }
}
