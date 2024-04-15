package ui.websocket;

import chess.ChessGame;
import chess.ChessMove;
import com.google.gson.Gson;
import exception.ResponseException;
import webSocketMessages.serverMessages.ServerMessage;
import webSocketMessages.userCommands.*;
import webSocketMessages.serverMessages.Notification;

import javax.websocket.*;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

public class WebSocketFacade extends Endpoint {

  Session session;
  NotificationHandler notificationHandler;

  public WebSocketFacade(String url, NotificationHandler notificationHandler) throws ResponseException {
    try {
      url = url.replace("http", "ws");
      URI socketURI = new URI(url + "/connect");
      this.notificationHandler = notificationHandler;

      WebSocketContainer container = ContainerProvider.getWebSocketContainer();
      this.session = container.connectToServer(this, socketURI);

      //set message handler
      this.session.addMessageHandler(new MessageHandler.Whole<String>() {
        @Override
        public void onMessage(String message) {
          notificationHandler.notify(message);
        }
      });
    } catch (DeploymentException | IOException | URISyntaxException ex) {
      throw new ResponseException(500, ex.getMessage());
    }
  }

  //Endpoint requires this method, but you don't have to do anything
  @Override
  public void onOpen(Session session, EndpointConfig endpointConfig) {
  }

  public void joinPlayer(String authToken, Integer gameID, ChessGame.TeamColor playerColor) throws ResponseException {
    try {
      var action = new JoinPlayer(authToken, gameID, playerColor);
      this.session.getBasicRemote().sendText(new Gson().toJson(action));
    } catch (IOException ex) {
      throw new ResponseException(500, ex.getMessage());
    }
  }

  public void joinObserver(String authToken, Integer gameID) throws ResponseException {
    try {
      var action = new JoinObserver(authToken, gameID);
      this.session.getBasicRemote().sendText(new Gson().toJson(action));
    } catch (IOException ex) {
      throw new ResponseException(500, ex.getMessage());
    }
  }

  public void makeMove(String authToken, Integer gameID, ChessMove move) throws ResponseException {
    try {
      var action = new MakeMove(authToken, gameID, move);
      this.session.getBasicRemote().sendText(new Gson().toJson(action));
    } catch (IOException ex) {
      throw new ResponseException(500, ex.getMessage());
    }
  }

  public void leave(String authToken, Integer gameID) throws ResponseException {
    try {
      var action = new Leave(authToken, gameID);
      this.session.getBasicRemote().sendText(new Gson().toJson(action));
    } catch (IOException ex) {
      throw new ResponseException(500, ex.getMessage());
    }
  }

  public void resign(String authToken, Integer gameID) throws ResponseException {
    try {
      var action = new Resign(authToken, gameID);
      this.session.getBasicRemote().sendText(new Gson().toJson(action));
    } catch (IOException ex) {
      throw new ResponseException(500, ex.getMessage());
    }
  }
}
