package main.client.chatframe;

import com.google.gson.Gson;
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
            if(new ServerInfoInClient().getUserId()!=0) {
                data.addProperty("currentUserID", new ServerInfoInClient().getUserId());
            }else{
                data.addProperty("currentUserName",new ServerInfoInClient().getUserNameOfCurrent());
            }
            request.add("data",data);

            ClientUtil client = new ClientUtil();

            response = client.sendRequest(request);
            if(ResponeHandler.checkResponse(response)){
                responseData = response.get("data").getAsJsonObject();
            }


        }catch (IOException e){
            e.printStackTrace();
        }
    }

    public ArrayList<String> getAllChats(){
        allChats.addAll(getGroupChats());
        allChats.addAll(getOneToOneChats());

        return allChats;
    }
    public ArrayList<String> getOneToOneChats(){
        JsonObject directChatData = responseData.get("oneToOneChats").getAsJsonObject();
        oneToOneChats = new Gson().fromJson(directChatData.get("chat_partner_name").getAsJsonArray(),new TypeToken<ArrayList<String>>() {}.getType());

        return oneToOneChats;
    }
    public ArrayList<String>getGroupChats(){
        JsonObject groupChatData = responseData.get("groupChats").getAsJsonObject();
        groupChats = new Gson().fromJson(groupChatData.get("group_name").getAsJsonArray(),new TypeToken<ArrayList<String>>() {}.getType());
        return groupChats;
    }
}
