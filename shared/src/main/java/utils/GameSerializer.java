package utils;

import chess.ChessGame;
import chess.PieceMovesCalculator;
import com.google.gson.*;

import java.lang.reflect.Type;

public class GameSerializer<T> implements JsonSerializer<T>, JsonDeserializer<T> {

    @Override
    public JsonElement serialize(T t, Type type, JsonSerializationContext jsonSerializationContext) {
        var className = t.getClass();
        return jsonSerializationContext.serialize(t, className);
    }


    @Override
    public T deserialize(JsonElement jsonElement, Type type, JsonDeserializationContext jsonDeserializationContext) throws JsonParseException {
        String className = ((JsonObject) jsonElement).get("className").getAsString();
        try {
            return jsonDeserializationContext.deserialize(jsonElement, Class.forName(className));
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }
}
