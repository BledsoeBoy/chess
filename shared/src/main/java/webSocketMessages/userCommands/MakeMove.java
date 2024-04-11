package webSocketMessages.userCommands;

import chess.ChessMove;

public class MakeMove extends UserGameCommand {
  public MakeMove(String authToken, Integer gameID, ChessMove move) {
    super(authToken);
    this.commandType = CommandType.MAKE_MOVE;
    this.gameID = gameID;
    this.move = move;
  }
}