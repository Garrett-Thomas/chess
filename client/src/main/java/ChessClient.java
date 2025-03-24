import passoff.server.TestServerFacade;

public class ChessClient {

private final TestServerFacade server;

public ChessClient(String serverUrl, String port){
this.server = new TestServerFacade(serverUrl, port);

}



}
