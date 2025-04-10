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
        if (params.size() != 1) {
            throw new UIException("Error: Invalid amount of parameters");
        }

        var res = SERVER.createGame(new TestCreateRequest(params.getFirst()), LocalStorage.getToken());
        if (res.getMessage() != null) {
            throw new UIException(res.getMessage());
        }

        updateGamMap();
        var msg = EscapeSequences.SET_TEXT_COLOR_GREEN + "Created game w/ name: " + params.getFirst() + EscapeSequences.RESET_TEXT_COLOR;
        System.out.println(msg);
    }

    private static String formatRow(String num, String name, String whiteName, String blackName) {
        return String.format("%-8s %-20s %-20s %-20s", num, name, whiteName, blackName);
    }

    private static String updateGamMap() throws UIException {
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
        return stringBuilder.toString();
    }

    private static void listGames() throws Exception {
        System.out.println(updateGamMap());
    }

    private static void playGame(ArrayList<String> params) throws Exception {
        if (params.size() != 2) {
            throw new UIException("Error: Invalid amount of parameters");
        }
        Integer gameID = LocalStorage.getGame(params.getFirst());
        String color = params.get(1);

        var playerColor = StringUtils.getColorFromInput(color);
        var joinReq = new TestJoinRequest(playerColor, gameID);

        var res = SERVER.joinPlayer(joinReq, LocalStorage.getToken());

        if (res.getMessage() != null) {
            throw new UIException(res.getMessage());
        }

        LocalStorage.setCurrGameID(gameID);

        System.out.println("Successfully joined game");

        LocalStorage.setTeamColor(playerColor);
        ChessClient.state = ChessClient.ProgramState.GAMEPLAY;
        ChessClient.clientType = ChessClient.ClientType.PLAYER;
        ServerFacade.connectSocket();
    }

    private static void observeGame(ArrayList<String> params) throws Exception {
        if (params.size() != 2) {
            throw new UIException("Error: Invalid amount of parameters");
        }
        var gameID = LocalStorage.getGame(params.getFirst());

        if (gameID == null) {
            throw new UIException("Error: Invalid game number");
        }

        var playerColor = StringUtils.getColorFromInput(params.getLast());
        LocalStorage.setTeamColor(playerColor);
        LocalStorage.setCurrGameID(gameID);
        System.out.println("Observing game " + params.getFirst());
        ChessClient.state = ChessClient.ProgramState.GAMEPLAY;
        ChessClient.clientType = ChessClient.ClientType.OBSERVER;

        ServerFacade.connectSocket();
    }

    private static String help() {
        return ("""
                help -> display this message
                logout -> logout current user
                list -> list all games
                create <GAME_NAME> -> create a game with the given name
                join <ID> [WHITE|BLACK] -> join game with given id and as given color
                observe <ID> -> observe game with given id
                quit -> quit program 
                """);
    }
}
