package server;

import spark.Request;
import spark.Response;

public class ServerUtils {
   private static AuthDAO authDAO = MemoryAuthDAO.getInstance();



   static public Object authUser(Request req, Response res){
       String token = req.body().
     if(authDAO.validateAuth())

   }
}
