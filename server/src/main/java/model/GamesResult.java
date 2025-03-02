package model;

import java.util.ArrayList;

public record GamesResult(String gameID, ArrayList<GameData> games) {
}
