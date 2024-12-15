package main.client.chatframe;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;
import main.client.response.ResponeHandler;
import main.client.utility.ClientUtil;

import javax.swing.*;
import java.util.ArrayList;

public class SearchUsers {
    public static ArrayList<String> searchUser(String query) {
        try {
            // Build the request JSON
            JsonObject request = new JsonObject();
            request.addProperty("action", "SEARCH_USER");
            JsonObject data = new JsonObject();
            data.addProperty("query", query);
            request.add("data",data);

            // Send the request to the server and receive the response
            ClientUtil client = new ClientUtil();
           JsonObject response = client.sendRequest(request);


            if (ResponeHandler.checkResponse(response)) {

                return new Gson().fromJson(response.get("searchedUsers").getAsJsonArray(),new TypeToken<ArrayList<String>>() {}.getType());
            } else {
                JOptionPane.showMessageDialog(null,"Error searching users: " + response.get("error").getAsString(),"search",JOptionPane.WARNING_MESSAGE);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return null;
    }

}
