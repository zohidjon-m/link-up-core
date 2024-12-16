package main.client.utility;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import main.client.chatframe.ReceiveMsg;
import main.client.server_info_inClient.ServerInfoInClient;

import javax.swing.*;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class ClientUtil {
    private Socket socket;
    private PrintWriter out;
    private BufferedReader in;
    private Gson gson = new Gson();
    String SERVER_ADDRESS = ServerInfoInClient.getInstance().getServer_address();
    int SERVER_PORT = ServerInfoInClient.getInstance().getServer_port();

    public ClientUtil(){
        try {
            socket = new Socket(SERVER_ADDRESS, SERVER_PORT);
            out = new PrintWriter(socket.getOutputStream(), true);
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));

        }catch (IOException e){
            JOptionPane.showMessageDialog(null, "Server didn't accept the connection! \nPleas try later on ...", "Connection", JOptionPane.WARNING_MESSAGE);
            e.printStackTrace();
        }
    }


    public JsonObject sendRequest(JsonObject request) throws IOException {

        // Send the json request to server
        System.out.println(request);
        out.println(request.toString());


        // Read the json response from server
        String response = in.readLine();
        System.out.println("response:"+response);

        JsonObject jsonResponse =  gson.fromJson(response, JsonObject.class);
        System.out.println(jsonResponse);
        if(jsonResponse.isJsonNull()&&jsonResponse.has("action")){
            new ReceiveMsg().getReceiverMsg(jsonResponse);
        }
        return jsonResponse;
    }

    public void closeConnection() throws IOException {
        socket.close();
    }
}
