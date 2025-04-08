package websocket.messages;

public class NotificationMessage extends ServerMessage {
    public String message;

    public NotificationMessage(ServerMessageType type, String msg) {
        super(type);
        this.message = msg;
    }
}
