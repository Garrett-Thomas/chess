package ui;

import passoff.model.TestCreateRequest;
import server.ServerFacade;

import java.util.ArrayList;

public class PostLogin {

    private static final ServerFacade server = ServerFacade.getInstance();

    public static void eval(String cmd, ArrayList<String> params) throws Exception {

        switch (cmd) {
            case "logout" -> logout();
            case "create" -> createGame(params);
            case "list" -> listGames();
            default -> System.out.println(help());
        }

    }


    private static void logout() throws Exception {
        var res = server.logout(ServerFacade.getToken());
        if (res.getMessage() != null) {
            throw new UIException(res.getMessage());
        }
        ServerFacade.setToken(null);
        System.out.println(EscapeSequences.SET_TEXT_COLOR_GREEN + "Successfully logged out");
    }

    private static void createGame(ArrayList<String> params) throws Exception {
        var res = server.createGame(new TestCreateRequest(params.getFirst()), ServerFacade.getToken());
        if (res.getMessage() != null) {
            throw new UIException(res.getMessage());
        }

        System.out.println(EscapeSequences.SET_TEXT_COLOR_GREEN + "Successfully create a game with the name " + params.getFirst());
    }

    private static void listGames() throws Exception {
        var token = ServerFacade.getToken();
        var res = server.listGames(ServerFacade.getToken());
        if (res.getMessage() != null) {
            throw new UIException(res.getMessage());
        }

        for (var game : res.getGames()) {
            System.out.println(game);
        }
    }

    private static void playGame(ArrayList<String> params) {

    }

    private static void observeGame(ArrayList<String> params) {
    }

    private static String help() {
        return ("""
                help -> display this message
                logout -> logout current user
                join <ID> [WHITE|BLACK] -> join game with given id and as given color
                observe <ID> -> observe game with given id
                quit -> quit program 
                """);
    }
}
