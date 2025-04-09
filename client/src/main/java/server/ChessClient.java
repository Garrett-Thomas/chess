package server;

import ui.*;

import java.util.ArrayList;
import java.util.Scanner;

public class ChessClient {


    public enum ProgramState {
        PRE_LOGIN,
        POST_LOGIN,
        GAMEPLAY;
    }

    public enum ClientType {
        PLAYER,
        OBSERVER,
        NONE
    }

    public static ProgramState state;
    public static ClientType clientType;

    public ChessClient() {
        state = ProgramState.PRE_LOGIN;
        clientType = ClientType.NONE;
    }


    public void run() {
        Scanner scanner = new Scanner(System.in);

        System.out.println("Welcome to ChessGame, enter \"Help\" to start");


        while (true) {
            try {
                if (state == ProgramState.PRE_LOGIN || state == ProgramState.POST_LOGIN) {
                    System.out.print(">> " + EscapeSequences.moveCursorToLocation(3, 0));
                }

                var input = scanner.nextLine();

                var parsedInput = StringUtils.parseCommand(input);
                var cmd = StringUtils.getCommand(parsedInput);
                var params = parsedInput.size() > 1 ? StringUtils.getParameters(parsedInput) : new ArrayList<String>();
                if (cmd.equals("quit")) {
                    System.exit(0);
                }
                if (state == ProgramState.PRE_LOGIN) {
                    PreLogin.eval(cmd, params);
                } else if (state == ProgramState.POST_LOGIN) {
                    PostLogin.eval(cmd, params);
                } else if (state == ProgramState.GAMEPLAY) {
                    GamePlay.eval(cmd, params);

                }

            } catch (Exception e) {

                System.out.println(StringUtils.getNegativeString(e.getMessage()));

            }
        }


    }
}
