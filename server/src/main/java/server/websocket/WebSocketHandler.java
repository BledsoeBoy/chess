package server.websocket;

import com.google.gson.Gson;
import dataaccess.DataAccess;
import exception.ResponseException;
import org.eclipse.jetty.websocket.api.Session;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketMessage;
import org.eclipse.jetty.websocket.api.annotations.WebSocket;
import server.Server;
import webSocketMessages.serverMessages.Notification;
import webSocketMessages.serverMessages.ServerMessage;
import webSocketMessages.userCommands.UserGameCommand;

import java.io.IOException;
import java.util.Timer;


@WebSocket
public class WebSocketHandler {

    private final ConnectionManager connections = new ConnectionManager();

    @OnWebSocketMessage
    public void onMessage(Session session, String message) throws IOException {
        System.out.printf("Received: %s", message);
        session.getRemote().sendString("WebSocket response: " + message);
        UserGameCommand action = new Gson().fromJson(message, UserGameCommand.class);
        switch (action.getCommandType()) {
            case JOIN_PLAYER -> { joinPlayer(action.getAuthString(), session);
            }
            case JOIN_OBSERVER -> {
            }
            case MAKE_MOVE -> {
            }
            case LEAVE -> {
            }
            case RESIGN -> {
            }
        }
    }

    private void joinPlayer(String playerName, Session session) throws IOException {
        connections.add(playerName, session);
        var message = String.format("%s has joined the game", playerName);
        var notification = new Notification(message);
        connections.broadcast(playerName, notification);
    }

    private void joinObserver(String visitorName) throws IOException {
        connections.remove(visitorName);
        var message = String.format("%s left the game", visitorName);
        var notification = new Notification(Notification.Type.DEPARTURE, message);
        connections.broadcast(visitorName, notification);
    }

    public void makeMove(String petName, String sound) throws ResponseException {
        try {
            var message = String.format("%s says %s", petName, sound);
            var notification = new Notification(Notification.Type.NOISE, message);
            connections.broadcast("", notification);
        } catch (Exception ex) {
            throw new ResponseException(500, ex.getMessage());
        }
    }

    public void leave(String petName, String sound) throws ResponseException {
        try {
            var message = String.format("%s says %s", petName, sound);
            var notification = new Notification(Notification.Type.NOISE, message);
            connections.broadcast("", notification);
        } catch (Exception ex) {
            throw new ResponseException(500, ex.getMessage());
        }
    }

    public void resign(String petName, String sound) throws ResponseException {
        try {
            var message = String.format("%s says %s", petName, sound);
            var notification = new Notification(Notification.Type.NOISE, message);
            connections.broadcast("", notification);
        } catch (Exception ex) {
            throw new ResponseException(500, ex.getMessage());
        }
    }
}