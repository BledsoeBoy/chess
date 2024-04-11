package webSocketMessages.serverMessages;

import chess.ChessGame;

public class LoadGame extends ServerMessage {
  public String game;

  public LoadGame() {
    super(ServerMessageType.LOAD_GAME);
    game = "Reload Game";
  }
}
