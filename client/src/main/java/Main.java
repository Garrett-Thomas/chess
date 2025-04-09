import server.ChessClient;
import server.ServerFacade;

public class Main {


    public static void main(String[] args) {

        var serverUrl = "localhost";
        var port = "8000";
        new ServerFacade(serverUrl, port);
        if (args.length == 1) {
            serverUrl = args[0];
        }
        ChessClient client = new ChessClient();
        client.run();

    }

}