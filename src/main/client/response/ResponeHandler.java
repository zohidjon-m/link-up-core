package main.client.response;

import com.google.gson.JsonObject;

import javax.swing.*;

public class ResponeHandler {

    public static boolean checkResponse(JsonObject response){
        if(response!=null) {
            if (response.get("status").getAsString().equals("SUCCESS")) {
                if (response.has("message") && !response.get("message").isJsonNull()) {
//                    JOptionPane.showMessageDialog(null, response.get("message").getAsString());
                    System.out.println("response message: "+response.get("message").getAsString());
                    return true;
                } else {
//                    JOptionPane.showMessageDialog(null, "There is ");
                    System.out.println("response message: "+response.get("message").getAsString());

                }


            } else if (response.get("status").getAsString().equals("ERROR")) {
                JOptionPane.showMessageDialog(null, response.get("message").getAsString());
                return false;
            }
        }else{
            System.out.println("in response handler, the response is null");
        }
        return false;
    }
}
