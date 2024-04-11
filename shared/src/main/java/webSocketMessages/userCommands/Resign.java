package webSocketMessages.userCommands;

import model.User;

public class Resign extends UserGameCommand{
  private Integer gameID;
  public Resign(String authToken, Integer gameID) {
    super(authToken);
    this.commandType = CommandType.RESIGN;
    this.gameID = gameID;
  }
}
