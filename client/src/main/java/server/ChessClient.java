package server;

import ui.PreLogin;

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

        try {
            while (true) {
                var input = scanner.nextLine();
                if (state == ProgramState.PRE_LOGIN) {
                    PreLogin.eval(input);
                } else if (state == ProgramState.POST_LOGIN) {

                } else if (state == ProgramState.GAMEPLAY) {


                }

            }

        } catch (Exception e) {
            System.out.println(e.getMessage());
        }

    }
}
