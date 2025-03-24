import passoff.server.TestServerFacade;

public class ChessClient {


    private enum ProgramState {
        PRE_LOGIN,
        POST_LOGIN,
        GAMEPLAY;
    }

    public static ProgramState state;

    public ChessClient(String serverUrl, String port) {
        state = ProgramState.PRE_LOGIN;
    }


    public void run() {

        System.out.println("Welcome to ChessGame, enter \"Help\" to start");
        if (state == ProgramState.PRE_LOGIN) {
            PreLogin.eval()
        } else if (state == ProgramState.POST_LOGIN) {

        } else if (state == ProgramState.GAMEPLAY) {


        }


    }


}
