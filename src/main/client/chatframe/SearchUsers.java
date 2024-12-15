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
    public ArrayList<String> searchUser(String query) {
        ArrayList<String> userList = new ArrayList<>();
        try {
            // Build the request JSON
            JsonObject request = new JsonObject();
            request.addProperty("action", "SEARCH_USERS");
            JsonObject data = new JsonObject();
            data.addProperty("query", query);
            request.add("data",data);

            // Send the request to the server and receive the response
            ClientUtil client = new ClientUtil();
           JsonObject response = client.sendRequest(request);


            if (ResponeHandler.checkResponse(response)) {
                // Ensure the "searchedUsers" key exists
                if (response.has("searchedUsers") && !response.get("searchedUsers").isJsonNull()) {
                    userList = new Gson().fromJson(response.get("searchedUsers").getAsJsonArray(), new TypeToken<ArrayList<String>>() {}.getType());
                } else {
                    System.out.println("No users found for the query: " + query);
                }
            } else {
                JOptionPane.showMessageDialog(null,"An error occurred while searching for users.", "Search", JOptionPane.ERROR_MESSAGE);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return userList;
    }

}
