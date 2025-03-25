package ui;

import chess.ChessGame;

import java.util.HashMap;

public class LocalStorage {
    private static String authToken = null;
    private static HashMap<String, Integer> gameNumToID = new HashMap<>();
    private static Integer currGameID = null;
    private static ChessGame.TeamColor playerColor;

    public static String getToken() {
        return authToken;
    }


    public static void setToken(String token) {
        authToken = token;
    }

    public static void setGameMap(HashMap<String, Integer> map) {
        gameNumToID = map;
    }

    public static Integer getGame(String number) {
        return gameNumToID.get(number);
    }

    public static void setCurrGameID(Integer gameID) {
        currGameID = gameID;
    }

    public static Integer getCurrGameID() {
        return currGameID;
    }

    public static ChessGame.TeamColor getTeamColor() {
        return playerColor;
    }

    public static void setTeamColor(ChessGame.TeamColor color) {
        playerColor = color;
    }
}
