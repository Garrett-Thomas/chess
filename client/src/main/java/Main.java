
public class Main {

    private enum ProgramState {
        PRE_LOGIN,
        POST_LOGIN,
        GAMEPLAY;
    }
    public static void main(String[] args) {

        var gameState = ProgramState.PRE_LOGIN;
        var serverUrl = "http://localhost:8080";
        if (args.length == 1) {
            serverUrl = args[0];
        }

        var client = new
    }

}