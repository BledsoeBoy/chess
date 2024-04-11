package server.websocket;

import chess.ChessGame;
import com.google.gson.Gson;
import dataAccess.AuthDAO;
import dataAccess.DataAccessException;
import dataAccess.GameDAO;
import exception.ResponseException;
import model.Auth;
import model.Game;
import org.eclipse.jetty.websocket.api.Session;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketMessage;
import org.eclipse.jetty.websocket.api.annotations.WebSocket;
import server.Server;
import webSocketMessages.serverMessages.LoadGame;
import webSocketMessages.serverMessages.Notification;
import webSocketMessages.serverMessages.ServerMessage;
import webSocketMessages.userCommands.JoinPlayer;
import webSocketMessages.userCommands.UserGameCommand;

import java.io.IOException;
import java.util.Timer;


@WebSocket
public class WebSocketHandler {
    private final AuthDAO authDAO;
    private final GameDAO gameDAO;

    private final ConnectionManager connections = new ConnectionManager();

    public WebSocketHandler(AuthDAO authDAO, GameDAO gameDAO) {
        this.authDAO = authDAO;
        this.gameDAO = gameDAO;
    }

    @OnWebSocketMessage
    public void onMessage(Session session, String message) throws IOException, DataAccessException, ResponseException {
        UserGameCommand action = new Gson().fromJson(message, UserGameCommand.class);
        Auth auth = authDAO.getAuth(action.getAuthString());
        switch (action.getCommandType()) {
            case JOIN_PLAYER -> { joinPlayer(auth.username(), action.getPlayerColor(), action.getGameID(), session);
            }
            case JOIN_OBSERVER -> { joinObserver(auth.username(), session);
            }
            case MAKE_MOVE -> { makeMove(auth.username(), session);
            }
            case LEAVE -> { leave(auth.username(), session);
            }
            case RESIGN -> { resign(auth.username(), session);
            }
        }
    }

    private void joinPlayer(String playerName, ChessGame.TeamColor playerColor, Integer gameID, Session session) throws IOException, DataAccessException {
        connections.add(playerName, session);
        var message = String.format("%s has joined the game on %s team", playerName, playerColor);
        var notification = new Notification(message);
        connections.broadcast(playerName, notification);
    }

    private void joinObserver(String playerName, Session session) throws IOException {
        connections.add(playerName, session);
        var message = String.format("%s has joined the game as an observer", playerName);
        var notification = new Notification(message);
        connections.broadcast(playerName, notification);
    }

    public void makeMove(String playerName, Session session) throws ResponseException {
        try {
            var message = String.format("%s has joined the game as an observer", playerName);
            var notification = new Notification(message);
            connections.broadcast("", notification);
        } catch (Exception ex) {
            throw new ResponseException(500, ex.getMessage());
        }
    }

    public void leave(String playerName, Session session) throws ResponseException {
        try {
            var message = String.format("%s has joined the game as an observer", playerName);
            var notification = new Notification(message);
            connections.broadcast("", notification);
        } catch (Exception ex) {
            throw new ResponseException(500, ex.getMessage());
        }
    }

    public void resign(String playerName, Session session) throws ResponseException {
        try {
            var message = String.format("%s has joined the game as an observer", playerName);
            var notification = new Notification(message);
            connections.broadcast("", notification);
        } catch (Exception ex) {
            throw new ResponseException(500, ex.getMessage());
        }
    }
}