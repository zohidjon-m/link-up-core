package main.client.response;

import com.google.gson.JsonObject;

import javax.swing.*;

public class ResponeHandler {

    public static boolean checkResponse(JsonObject response){
        if(response.get("status").getAsString().equals("SUCCESS")){
            if(response.has("message") && !response.get("message").isJsonNull()){
                JOptionPane.showMessageDialog(null, response.get("message").getAsString());
                return true;
            }else{
                JOptionPane.showMessageDialog(null,"There is ");
            }


        }else if (response.get("status").getAsString().equals("ERROR")) {
            JOptionPane.showMessageDialog(null, response.get("message").getAsString());
            return false;
        }
        return false;
    }
}
