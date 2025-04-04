package ui;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class StringUtils {

    public static ArrayList<String> parseCommand(String input) {
        var tokens = input.toLowerCase().split(" ");
        var cmd = (tokens.length > 0) ? tokens[0] : "help";
        var params = Arrays.copyOfRange(tokens, 1, tokens.length);
        var ret = new ArrayList<String>();
        ret.add(cmd);
        ret.addAll(List.of(params));
        return ret;
    }

    public static String getCommand(ArrayList<String> input) {
        return input.getFirst();
    }

    public static ArrayList<String> getParameters(ArrayList<String> input) {
        return new ArrayList<String>(input.subList(1, input.size()));
    }

    public static String getPositiveString(String msg){
        return EscapeSequences.SET_TEXT_COLOR_GREEN + msg + EscapeSequences.RESET_TEXT_COLOR;
    }
    public static String getNegativeString(String msg){
        return EscapeSequences.SET_TEXT_COLOR_RED + msg + EscapeSequences.RESET_TEXT_COLOR;
    }

}
