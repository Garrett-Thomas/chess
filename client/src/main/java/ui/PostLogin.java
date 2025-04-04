package ui;

import chess.ChessGame;
import passoff.model.TestCreateRequest;
import passoff.model.TestJoinRequest;
import server.ChessClient;
import server.ServerFacade;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Objects;

public class PostLogin {

    private static final ServerFacade SERVER = ServerFacade.getInstance();

    public static void eval(String cmd, ArrayList<String> params) throws Exception {

        switch (cmd) {
            case "logout" -> logout();
            case "create" -> createGame(params);
            case "list" -> listGames();
            case "join" -> playGame(params);
            case "observe" -> observeGame(params);
            default -> System.out.println(help());
        }

    }


    private static void logout() throws Exception {
        var res = SERVER.logout(LocalStorage.getToken());
        if (res.getMessage() != null) {
            throw new UIException(res.getMessage());
        }
        LocalStorage.setToken(null);
        ChessClient.state = ChessClient.ProgramState.PRE_LOGIN;
        var msg = EscapeSequences.SET_TEXT_COLOR_GREEN + "Successfully logged out" + EscapeSequences.RESET_TEXT_COLOR;
        System.out.println(msg);
    }

    private static void createGame(ArrayList<String> params) throws Exception {
        var res = SERVER.createGame(new TestCreateRequest(params.getFirst()), LocalStorage.getToken());
        if (params.size() != 1) {
            throw new UIException("Error: Invalid amount of parameters");
        }

        if (res.getMessage() != null) {
            throw new UIException(res.getMessage());
        }

        var msg = EscapeSequences.SET_TEXT_COLOR_GREEN + "Created game w/ name: " + params.getFirst() + EscapeSequences.RESET_TEXT_COLOR;
        System.out.println(msg);
    }

    private static String formatRow(String num, String name, String whiteName, String blackName) {
        return String.format("%-8s %-20s %-20s %-20s", num, name, whiteName, blackName);
    }

    private static void listGames() throws Exception {
        var res = SERVER.listGames(LocalStorage.getToken());
        if (res.getMessage() != null) {
            throw new UIException(res.getMessage());
        }

        HashMap<String, Integer> numToID = new HashMap<>();
        var games = res.getGames();
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(formatRow("Number", "Game Name", "White Player", "Black Player"));
        stringBuilder.append("\n");

        for (int i = 0; i < games.length; i++) {

            var whiteUsername = games[i].getWhiteUsername();
            var blackUsername = games[i].getBlackUsername();
            whiteUsername = whiteUsername == null ? "available" : whiteUsername;
            blackUsername = blackUsername == null ? "available" : blackUsername;

            var row = formatRow(i + "", games[i].getGameName(), whiteUsername, blackUsername);
            stringBuilder.append(row);
            stringBuilder.append("\n");
            numToID.put(i + "", games[i].getGameID());
        }

        LocalStorage.setGameMap(numToID);
        System.out.println(stringBuilder);
    }

    private static void playGame(ArrayList<String> params) throws Exception {
        if (params.size() != 2) {
            throw new UIException("Error: Invalid amount of parameters");
        }
        Integer gameID = LocalStorage.getGame(params.getFirst());
        String color = params.get(1);
        ChessGame.TeamColor playerColor;
        if (Objects.equals(color, "black")) {
            playerColor = ChessGame.TeamColor.BLACK;
        } else if (Objects.equals(color, "white")) {
            playerColor = ChessGame.TeamColor.WHITE;
        } else {
            throw new UIException("Error: Bad Color");
        }

        var joinReq = new TestJoinRequest(playerColor, gameID);

        var res = SERVER.joinPlayer(joinReq, LocalStorage.getToken());

        if (res.getMessage() != null) {
            throw new UIException(res.getMessage());
        }

        LocalStorage.setCurrGameID(gameID);

        System.out.println("Successfully joined game");

        LocalStorage.setTeamColor(playerColor);
        GamePlay.drawBoard();
//        ChessClient.state = ChessClient.ProgramState.GAMEPLAY;

    }

    private static void observeGame(ArrayList<String> params) throws Exception {
        if (params.size() != 1) {
            throw new UIException("Error: Invalid amount of parameters");
        }
        var gameID = LocalStorage.getGame(params.getFirst());

        if (gameID == null) {
            throw new UIException("Error: Invalid game number");
        }

        System.out.println("Observing game " + params.getFirst());
        LocalStorage.setTeamColor(ChessGame.TeamColor.WHITE);
//        ChessClient.state = ChessClient.ProgramState.GAMEPLAY;
        GamePlay.drawBoard();
    }

    private static String help() {
        return ("""
                help -> display this message
                logout -> logout current user
                create <GAME_NAME> -> create a game with the given name
                join <ID> [WHITE|BLACK] -> join game with given id and as given color
                observe <ID> -> observe game with given id
                quit -> quit program 
                """);
    }
}
