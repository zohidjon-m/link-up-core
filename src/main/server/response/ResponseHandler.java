package main.server.response;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import main.server.LinkUpServer;

import java.io.PrintWriter;

public class ResponseHandler extends LinkUpServer {
    Gson gson = new Gson();
    PrintWriter out;

    public ResponseHandler(PrintWriter out) {
        this.out = out;
    }
    public void sendSuccessResponse(String message, String key, Object value) {
        JsonObject response = new JsonObject();
        response.addProperty("status", "SUCCESS");
        response.addProperty("message", message);
        if (key != null && value != null) {
//            JsonElement jsonElement = gson.toJsonTree(value);
            response.add(key, gson.toJsonTree(value));
        } else if (value != null) {
            response.add("value",gson.toJsonTree(value));
        }
        //send json response as a string
        out.println(response.toString());
    }

    public void sendSuccessResponse(String message) {
        sendSuccessResponse(message, null, null);
    }
    public void sendSuccessResponse() {
        sendSuccessResponse(null, null, null);
    }

    public void sendErrorResponse(String errorMessage) {
        JsonObject response = new JsonObject();
        response.addProperty("status", "ERROR");
        response.addProperty("message", errorMessage);
        out.println(response.toString());
    }
}

