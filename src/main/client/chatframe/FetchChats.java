package main.client.chatframe;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;
import main.client.response.ResponeHandler;
import main.client.server_info_inClient.ServerInfoInClient;
import main.client.utility.ClientUtil;

import javax.swing.*;
import java.io.IOException;
import java.util.ArrayList;
import java.util.concurrent.locks.ReentrantLock;

public class FetchChats {

    private ArrayList<String> allChats = new ArrayList<>();
    private ArrayList<String> oneToOneChats = new ArrayList<>();
    private ArrayList<String> groupChats = new ArrayList<>();
    JsonObject response;
    JsonObject responseData;
    public FetchChats(){
        initializeChats();
    }
    public void initializeChats(){
        try{
            JsonObject request = new JsonObject();
            request.addProperty("action","FETCH_CHATS");
            JsonObject data = new JsonObject();
            ServerInfoInClient serverInfo = ServerInfoInClient.getInstance();

            if(serverInfo.getUserId()>0) {
                data.addProperty("currentUserID", serverInfo.getUserId());
            }else if(serverInfo.getUserNameOfCurrent()!=null){
                data.addProperty("currentUserName",serverInfo.getUserNameOfCurrent());
            }else{
                System.out.println("in the client side, both the currentUserID and currentUserName are null");
            }

            request.add("data",data);

            ClientUtil client = new ClientUtil();

            response = client.sendRequest(request);

            // Check if the response is valid
            if (response != null && ResponeHandler.checkResponse(response)) {
                if (response.has("data") && !response.get("data").isJsonNull()) {
                    responseData = response.getAsJsonObject("data");
                } else {
                    responseData = new JsonObject(); // Initialize an empty JsonObject if "data" is missing
                }
            } else {
                System.out.println("Failed to fetch chats: Response is null or invalid.");
                responseData = new JsonObject(); // Fallback to an empty JsonObject
            }
        } catch (IOException e) {
            e.printStackTrace();
            responseData = new JsonObject(); // Ensure responseData is never null
        }
    }

    public ArrayList<String> getAllChats(){
        allChats.addAll(getGroupChats());
        allChats.addAll(getOneToOneChats());

        return allChats;
    }
    public ArrayList<String> getOneToOneChats(){
        if (responseData == null || !responseData.has("oneToOneChats")) {
            System.out.println("No one-to-one chats found.");
            return new ArrayList<>(); // Return an empty list
        }

        JsonArray oneToOneChatData = responseData.getAsJsonArray("oneToOneChats");
        return new Gson().fromJson(oneToOneChatData, new TypeToken<ArrayList<String>>() {}.getType());
    }
    public ArrayList<String>getGroupChats(){
        if (responseData == null || !responseData.has("groupChats")) {
            System.out.println("No group chats found.");
            return new ArrayList<>(); // Return an empty list
        }
        JsonArray groupChatData = responseData.getAsJsonArray("groupChats");
        return new Gson().fromJson(groupChatData, new TypeToken<ArrayList<String>>() {}.getType());
    }


}
