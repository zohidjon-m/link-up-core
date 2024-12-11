package main.client.utility;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import main.client.server_info_inClient.ServerInfoInClient;

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
    ServerInfoInClient serverInfoInClient = new ServerInfoInClient();
    String SERVER_ADDRESS = serverInfoInClient.getServer_address();
    int SERVER_PORT = serverInfoInClient.getServer_port();

    public ClientUtil() throws IOException {
        socket = new Socket(SERVER_ADDRESS, SERVER_PORT);
        out = new PrintWriter(socket.getOutputStream(), true);
        in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
    }


    public JsonObject sendRequest(JsonObject request) throws IOException {

        // Send the json request to server
        out.println(request.toString());


        // Read the json response from server
        String response = in.readLine();


        return gson.fromJson(response, JsonObject.class);
    }

    public void closeConnection() throws IOException {
        socket.close();
    }

}
