import passoff.server.TestServerFacade;

public class ServerFacade extends TestServerFacade {

    private static ServerFacade server = null;

    public ServerFacade(String url, String port) {
        super(url, port);
        server = this;
    }

    public static ServerFacade getInstance() {
        return server;
    }

}
