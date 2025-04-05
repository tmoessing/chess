package websocket;

import messages.Notification;

public interface NotificationHandler {
    void notify(Notification notification);
}
