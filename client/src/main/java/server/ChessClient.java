package server;

import ui.*;

import java.util.ArrayList;
import java.util.Scanner;

public class ChessClient {


    public static enum ProgramState {
        PRE_LOGIN,
        POST_LOGIN,
        GAMEPLAY;
    }

    public static ProgramState state;

    public ChessClient() {
        state = ProgramState.PRE_LOGIN;
    }


    public void run() {
        Scanner scanner = new Scanner(System.in);

        System.out.println("Welcome to ChessGame, enter \"Help\" to start");


        while (true) {
            try {
                System.out.print(">> " + EscapeSequences.moveCursorToLocation(3, 0));
                var input = scanner.nextLine();

                var parsedInput = StringUtils.parseCommand(input);
                var cmd = StringUtils.getCommand(parsedInput);
                var params = parsedInput.size() > 1 ? StringUtils.getParameters(parsedInput) : new ArrayList<String>();

                if (state == ProgramState.PRE_LOGIN) {
                    PreLogin.eval(cmd, params);
                } else if (state == ProgramState.POST_LOGIN) {
                    PostLogin.eval(cmd, params);
                } else if (state == ProgramState.GAMEPLAY) {


                }

            } catch (UIException e) {
                System.out.println(EscapeSequences.SET_TEXT_COLOR_RED + e.getMessage());
            } catch (Exception e) {

                System.out.println(EscapeSequences.SET_TEXT_COLOR_RED + "error");

            }
        }


    }
}
