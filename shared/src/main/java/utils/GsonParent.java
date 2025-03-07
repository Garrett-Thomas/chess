package utils;

import chess.PieceMovesCalculator;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class GsonParent {
    private static Gson gson = null;

    public static Gson getInstance() {
        if (gson == null) {
            GsonBuilder gsonBuilder = new GsonBuilder();
            gsonBuilder.registerTypeAdapter(PieceMovesCalculator.class, new GameSerializer<PieceMovesCalculator>());
            gson = gsonBuilder.create();

        }
        return gson;
    }
}
