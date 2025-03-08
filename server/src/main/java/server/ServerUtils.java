package server;

import dao.AuthDAO;
import dao.MemoryAuthDAO;
import dao.SQLAuthDAO;
import dataaccess.ServiceException;
import spark.Request;
import spark.Response;

public class ServerUtils {
   private static final AuthDAO AUTH_DAO = SQLAuthDAO.getInstance();



   static public void authUser(Request req, Response res) throws ServiceException {
       String token = req.headers("authorization");


      if(token == null){
          throw new ServiceException(400, "Token does not exist");
      }

     if(!AUTH_DAO.validateAuth(token)){
        throw new ServiceException(401, "Token not valid");
     }

   }
}
