package ui;

import passoff.model.TestCreateRequest;
import passoff.model.TestUser;
import server.ChessClient;
import server.ServerFacade;

import java.util.ArrayList;

public class PreLogin {

    private static final ServerFacade server = ServerFacade.getInstance();

    public static void eval(String cmd, ArrayList<String> params) throws Exception {

        switch (cmd) {
            case "login" -> login(params);
            case "register" -> register(params);
            case "quit" -> quit();
            default -> help();

        }

    }

    private static void login(ArrayList<String> params) throws Exception {
        var res = server.login(new TestUser(params.get(0), params.get(1)));
        if (res.getMessage() != null) {
            throw new UIException(res.getMessage());
        }

        LocalStorage.setToken(res.getAuthToken());
        System.out.println(StringUtils.getPositiveString("Successfully logged in " + res.getUsername()));
        ChessClient.state = ChessClient.ProgramState.POST_LOGIN;
    }

    private static void register(ArrayList<String> params) throws Exception {
        var res = server.register(new TestUser(params.get(0), params.get(1), params.get(2)));
        if (res.getMessage() != null) {
            throw new UIException(res.getMessage());
        }

        LocalStorage.setToken(res.getAuthToken());
        var msg = "Successfully registered " + res.getUsername();
        System.out.println(StringUtils.getPositiveString(msg));
        ChessClient.state = ChessClient.ProgramState.POST_LOGIN;


    }

    private static void quit() {
        System.out.println("Goodbye");
        System.exit(0);
    }

    private static void help() {
        System.out.println("""
                help -> prints out this message;
                login <USERNAME> <PASSWORD> - to play chess
                register <USERNAME> <PASSWORD> <EMAIL> - register an account
                quit - exits program 
                """);
    }


}
