package ui;

import ui.websocket.NotificationHandler;
import webSocketMessages.serverMessages.Notification;
import webSocketMessages.serverMessages.ServerMessage;

import java.util.Scanner;

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
                System.out.print(SET_TEXT_COLOR_BLUE + result);
            } catch (Throwable e) {
                var msg = e.toString();
                System.out.print(msg);
            }
        }
        System.out.println();
    }

    public void notify(String notification) {
        //deserialize notification as serverMessage
        //switch statement based on load, notification, error
        //notification and error just print out
        //load type takes game and prints it out
        //System.out.println(SET_TEXT_COLOR_RED + notification);
        printPrompt();
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
