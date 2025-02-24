package server;

public class LoginRequest {

    private String username;
    private String password;

    LoginRequest(String username, String password){
       this.username = username;
       this.password = password;

    }

    String getUsername(){
        return this.username;
    }
    String getPassword(){
        return this.password;
    }

}
