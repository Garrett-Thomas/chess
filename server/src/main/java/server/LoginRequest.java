package server;

public class LoginRequest {

    private String username;
    private String password;

    LoginRequest(String username, String password){
       this.username = username;
       this.password = password;

    }

    public String getUsername(){
        return this.username;
    }
    public String getPassword(){
        return this.password;
    }

}
