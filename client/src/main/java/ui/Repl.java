package ui;

import com.google.gson.Gson;
import ui.websocket.NotificationHandler;
import webSocketMessages.serverMessages.Error;
import webSocketMessages.serverMessages.LoadGame;
import webSocketMessages.serverMessages.Notification;
import webSocketMessages.serverMessages.ServerMessage;
import webSocketMessages.userCommands.JoinPlayer;
import webSocketMessages.userCommands.UserGameCommand;

import java.util.Scanner;
import java.util.concurrent.TimeUnit;

import static ui.EscapeSequences.*;

public class Repl implements NotificationHandler {
    private final ChessClient client;

    public Repl(String serverUrl) {
        client = new ChessClient(serverUrl, this);
    }

    public void run() {
        System.out.println(BLACK_KING + " Welcome to the chess game. Try Help to get started.");

        Scanner scanner = new Scanner(System.in);
        var result = "";
        while (!result.equals("quit")) {
            printPrompt();
            String line = scanner.nextLine();

            try {
                result = client.eval(line);
                TimeUnit.SECONDS.sleep(1);
                System.out.print(SET_TEXT_COLOR_BLUE + result);
            } catch (Throwable e) {
                var msg = e.toString();
                System.out.print(msg);
            }
        }
        System.out.println();
    }

    public void notify(String notification) {
        ServerMessage action = new Gson().fromJson(notification, ServerMessage.class);
        switch (action.getServerMessageType()) {
            case ERROR -> {
                errorMessage(new Gson().fromJson(notification, Error.class));
            }
            case LOAD_GAME -> {
                loadGame(new Gson().fromJson(notification, LoadGame.class));
            }
            case NOTIFICATION -> {
                notificationMessage(new Gson().fromJson(notification, Notification.class));
            }
        }
    }

    private void errorMessage(Error errorClass) {
        System.out.println("\n" + SET_TEXT_COLOR_RED + errorClass.errorMessage);
    }

    private void notificationMessage(Notification notification) {
        System.out.println("\n" + SET_TEXT_COLOR_RED + notification.message);
    }

    private void loadGame(LoadGame loadGame) {
        if (client.playerColor == null) {
            ChessGame.run("WHITE");
        }
        ChessGame.run(client.playerColor);
    }

    private void printPrompt() {
        if (client.state == State.LOGGED_OUT) {
            System.out.print("\n" + SET_TEXT_COLOR_WHITE + "[LOGGED_OUT]" + ">>> " + SET_TEXT_COLOR_GREEN);
        }
        else if (client.state == State.JOINED_GAME){
            System.out.print("\n" + SET_TEXT_COLOR_WHITE + "[JOINED_GAME]" + ">>> " + SET_TEXT_COLOR_GREEN);
        }
        else {
            System.out.print("\n" + SET_TEXT_COLOR_WHITE + "[LOGGED_IN]" + ">>> " + SET_TEXT_COLOR_GREEN);
        }
    }
}
