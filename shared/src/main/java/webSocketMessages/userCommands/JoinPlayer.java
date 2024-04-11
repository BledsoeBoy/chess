package webSocketMessages.userCommands;

import chess.ChessGame;

public class JoinPlayer extends UserGameCommand {
  private Integer gameID;
  private ChessGame.TeamColor playerColor;
  public JoinPlayer(String authToken, Integer gameID) {
    super(authToken);
    this.commandType = CommandType.JOIN_PLAYER;
    this.gameID = gameID;
  }
}
