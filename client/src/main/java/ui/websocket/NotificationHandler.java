package ui.websocket;

import webSocketMessages.serverMessages.Notification;
import webSocketMessages.serverMessages.ServerMessage;

public interface NotificationHandler {
  void notify(String notification);
}
