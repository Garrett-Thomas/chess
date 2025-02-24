package service;

import dataaccess.DataAccessException;
import server.LoginRequest;
import server.LoginResponse;
import server.AuthDAO;
import server.MemoryAuthDAO;

import java.util.Objects;

public class LoginService {

   private AuthDAO authDAO;
   LoginService(){
     this.authDAO = new MemoryAuthDAO();
   }

public LoginResponse login(LoginRequest res) throws DataAccessException {

      var userData = this.authDAO.getUser(res.getUsername());

      if(!Objects.equals(userData.password(), res.getPassword())) throw new DataAccessException("Invalid username");

      var token = this.authDAO.createAuth(userData.username());

      return new LoginResponse(token, userData.username());
}


}
