
public class Main {


    public static void main(String[] args) {

        var serverUrl = "localhost";
        var port = "8080";
        new ServerFacade(serverUrl, port);
        if (args.length == 1) {
            serverUrl = args[0];
        }
    }

}