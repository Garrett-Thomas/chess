package model;

public record RegisterRequest (String username, String password, String email){
    public static record LoginRequest(String username, String password){}
}

