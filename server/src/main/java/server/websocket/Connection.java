package server.websocket;

import org.eclipse.jetty.websocket.api.Session;

import java.io.IOException;

public class Connection {
    public String playerName;
    public Integer gameID;
    public Session session;

    public Connection(String playerName, Integer gameID, Session session) {
        this.playerName = playerName;
        this.session = session;
        this.gameID = gameID;
    }

    public void send(String msg) throws IOException {
        session.getRemote().sendString(msg);
    }
}