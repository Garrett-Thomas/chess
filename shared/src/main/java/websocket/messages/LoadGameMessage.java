package websocket.messages;

import chess.ChessGame;

public class LoadGameMessage extends ServerMessage{

    private String game;
    public LoadGameMessage(ServerMessageType type, String game) {
        super(type);
        this.game = game;
    }

    public String getGame(){
        return this.game;
    }

}
