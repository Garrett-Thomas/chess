package ui;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class StringUtils {

   public ArrayList<String> parseCommand(String input){
       var tokens = input.toLowerCase().split(" ");
       var cmd = (tokens.length > 0) ? tokens[0] : "help";
       var params = Arrays.copyOfRange(tokens, 1, tokens.length);
       var ret = new ArrayList<String>();
       ret.add(cmd);
       ret.addAll(List.of(params));
       return ret;
   }



}
