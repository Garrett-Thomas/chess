package dataaccess;

import java.util.HashMap;
import java.util.Map;

public class ServiceException extends Exception {

    private final int statusCode;
    private final String message;
    private final static Map<Integer, String> codeToRes = Map.of(400, "Error: bad request", 401, "Error: unauthorized", 403, "Error: already taken");


    public ServiceException(int statusCode, String message) {
        super(message);
        this.message = message;
        this.statusCode = statusCode;
    }

    public int getStatusCode() {

        return this.statusCode;
    }

    public String getResponse() {
        if (this.statusCode == 500) {
            return String.format("Error: %s", this.message);
        }

        return codeToRes.get(this.statusCode);
    }

}
