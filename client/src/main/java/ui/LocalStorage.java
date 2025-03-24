package ui;

import java.util.HashMap;

public class LocalStorage {
    private static String authToken = null;
    private static HashMap<String, Integer> gameNumToID = null;

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

}
