package main.server;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import main.server.database.ConDB;

import java.io.*;
import java.net.*;
import java.sql.*;
import java.util.HashMap;
import java.util.Map;

public class LinkUpServer {
    private static final int PORT = 4242;
    protected static Connection connection;
    private static Map<Integer, PrintWriter> onlineUsers = new HashMap<>(); // user_id -> output stream

    public static void main(String[] args) {

        // Connect to MySQL database
       connection =  new ConDB().connectToDatabase();

        try (ServerSocket serverSocket = new ServerSocket(PORT)) {
            System.out.println("Server is running on port " + PORT);

            while (true) {
                Socket clientSocket = serverSocket.accept();
                new ClientHandler(clientSocket).start();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    static class ClientHandler extends Thread {
        private Socket socket;
        private BufferedReader in;
        private PrintWriter out;
        private Gson gson = new Gson();
        private int userId = -1;

        public ClientHandler(Socket socket) {
            this.socket = socket;
        }
        public void run() {
            try {
                in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                out = new PrintWriter(socket.getOutputStream(), true);

                while (true) {
                    String request = in.readLine();
                    if (request == null) break;

                    System.out.println("Received request: " + request);
                    handleRequest(request);
                }
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                //use to show online users; when they leave the application, remove them from
                if (userId != -1) {
                    onlineUsers.remove(userId);
                    System.out.println("User " + userId + " disconnected.");

                }
                try {
                    socket.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        private void handleRequest(String request) {
            UserAuth userAuth = new UserAuth(connection);
            ResponseHandler responseHandler = new ResponseHandler(out);

            // used for parsing the json string to json object
            JsonObject jsonRequest = gson.fromJson(request, JsonObject.class);
            String action = jsonRequest.get("action").getAsString();
            JsonObject data = jsonRequest.getAsJsonObject("data");

            switch (action) {
                case "LOGIN":
                    int idOfUser = userAuth.authenticateUser(data.get("username").getAsString(), data.get("password").getAsString());
                    //add idOfUser to online users
                    onlineUsers.put(idOfUser, out);

                    if(idOfUser != -1) {
                        userId = idOfUser;
                        onlineUsers.put(idOfUser, out);
                        responseHandler.sendSuccessResponse("Login Successful");
                    }

                    break;
                case "REGISTER":
                    boolean statusReg =  userAuth.registerUser(data.get("username").getAsString(), data.get("password").getAsString());
                    // will send success message
                    if(statusReg) {
                        responseHandler.sendSuccessResponse("Registered Successfully");
                    }else{
                        responseHandler.sendErrorResponse("User already exists");
                    }
                    break;
//                case "FETCH_CHATS":
//                    handleFetchChats();
//                    break;
//                case "FETCH_MESSAGES":
//                    handleFetchMessages(data.get("chatId").getAsInt());
//                    break;
//                case "SEARCH_USER":
//                    handleSearchUser(data.get("username").getAsString());
//                    break;
//                case "FRIENDS_LIST":
//                    handleFriendsList();
//                    break;
//                case "CHECK_ONLINE":
//                    handleCheckOnline();
//                    break;
                default:
//                    send errorResponse("Unknown action");
                    responseHandler.sendErrorResponse("Invalid action");
                    System.out.println("Unknown action: " + action);
                    break;
            }



        }


    }
}
