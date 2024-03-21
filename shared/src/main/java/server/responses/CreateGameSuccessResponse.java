package server.responses;

public class CreateGameSuccessResponse {
    private int gameID;

    public CreateGameSuccessResponse(int gameID) {
        this.gameID = gameID;
    }
    public int getGameID() {
        return gameID;
    }
}
