package webSocketMessages.userCommands;

import chess.ChessGame;

public class JoinPlayer extends UserGameCommand {
  public Integer gameID;
  public ChessGame.TeamColor playerColor;
  public JoinPlayer(String authToken, Integer gameID, ChessGame.TeamColor playerColor) {
    super(authToken);
    this.commandType = CommandType.JOIN_PLAYER;
    this.gameID = gameID;
    this.playerColor = playerColor;
  }
}
