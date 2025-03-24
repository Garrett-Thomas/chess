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


}
