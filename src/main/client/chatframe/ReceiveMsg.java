package main.client.chatframe;

import com.google.gson.JsonObject;

import java.sql.Timestamp;

public class ReceiveMsg{

    public void getReceiverMsg(JsonObject response){
        if(response.isJsonNull()&&response.has("action")){
            if(response.get("action").getAsString().equals("NEW_MESSAGE")){
                int senderId = response.get("sender_id").getAsInt();
                String message = response.get("message").getAsString();
                System.out.println("New_MESSAGE: "+message);
                new OneToOneChatFrame().addMessageToPanel(message,true,String.valueOf(new Timestamp(System.currentTimeMillis())));
            }
        }
    }
}
