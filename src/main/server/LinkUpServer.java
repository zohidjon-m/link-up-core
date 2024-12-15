package main.server;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;
import main.server.auth.UserAuth;
import main.server.database.ConDB;
import main.server.response.*;


import java.io.*;
import java.net.*;
import java.sql.*;
import java.util.HashMap;
import java.util.List;
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
                   try {
                       handleRequest(request);
                   }catch (SQLException e){
                       e.printStackTrace();
                   }
                }
            } catch (IOException e) {
                // General I/O exception
                System.err.println("I/O error occurred: " + e.getMessage());


            } finally {
                //use to show online users; when they leave the application, remove them from
                if (userId != -1) {
                    onlineUsers.remove(userId);
                    System.out.println("UserId: " + userId + " disconnected.");

                }
                try {
                    if (socket != null) socket.close();
                    if (in != null) in.close();
                    if (out != null) out.close();
                } catch (IOException e) {
                    System.err.println("Error closing resources: " + e.getMessage());
                }
            }
        }
        private void handleRequest(String request) throws SQLException {
            UserAuth userAuth = new UserAuth(connection);
            ResponseHandler responseHandler = new ResponseHandler(out);

            // used for parsing the json string to json object
            JsonObject jsonRequest = gson.fromJson(request, JsonObject.class);
            String action = jsonRequest.get("action").getAsString();
            JsonObject data = jsonRequest.getAsJsonObject("data");

            switch (action) {
                case "LOGIN":
                    int idOfUser = userAuth.authenticateUser(data.get("username").getAsString(), data.get("password").getAsString());
                    System.out.println(idOfUser);
                    if(idOfUser != -1) {
                        userId = idOfUser;
                        onlineUsers.put(idOfUser, out);
                        responseHandler.sendSuccessResponse("Login Successful","value",userId);
                    }else{
                        responseHandler.sendErrorResponse("User doesn't exist");

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
                case "FETCH_CHATS":
                    JsonObject fetchedChats = new JsonObject();
                    fetchedChats =null;
                    //checking whether the currentUserId is exist or not
                    if(data.has("currentUserID") && !data.get("currentUserID").isJsonNull()){
                        System.out.println("in server, fetch_chats case, currentuserId is sent ot Fetch Class");
                         fetchedChats = new FetchChats(connection).fetchChats(data.get("currentUserID").getAsInt());
                    } else if(data.has("currentUserName") && !data.get("currentUserName").isJsonNull()){
                        //
                       int userid = new FetchChats(connection).getUserIdByUserName(data.get("currentUserName").getAsString());
                        if(userid!=-1){
                            fetchedChats = new FetchChats(connection).fetchChats(data.get("currentUserID").getAsInt());

                        }
                    }else{
                        System.out.println("Both username and userid are null");
                    }

                    if(fetchedChats!=null){
                        responseHandler.sendSuccessResponse("Chats are fetched successfully","data",fetchedChats);
                    }else{
                        responseHandler.sendErrorResponse("there is an error while fetching data from database");
                    }

                    break;
                case "FETCH_MESSAGES_ONE":
                    JsonArray fetchedMsgData = new JsonArray();
                    fetchedMsgData = new FetchMessages().fetchOneToOneMessage(data.get("senderId").getAsInt(),data.get("receiverId").getAsInt(),connection);
                    if(fetchedMsgData!=null){
                        responseHandler.sendSuccessResponse("Successfully fetched", "messageArray",fetchedMsgData);
                    }else{
                        responseHandler.sendErrorResponse("there were error in fetching oneToOneMessage data");
                    }

                    break;
                case "SEND_MESSAGE_ONE":
                    int senderId = data.get("senderId").getAsInt();
                    int receiverId =data.get("receiverId").getAsInt();
                    String message = data.get("messageContent").getAsString();
                    boolean saved = new SaveMessages().sendOneToOneMessage(senderId,receiverId,message,connection);
                    if(saved){
                        // Server: Send a new message to the recipient's socket
                        PrintWriter recipientOut = onlineUsers.get(receiverId); // Socket output stream of recipient
                        if (recipientOut != null) {
                            JsonObject newMessage = new JsonObject();
                            newMessage.addProperty("action", "NEW_MESSAGE");
                            newMessage.addProperty("sender_id", senderId);
                            newMessage.addProperty("message", message);

                            recipientOut.println(newMessage.toString()); // Send message to recipient
                            System.out.println("out : "+newMessage.toString());
                            recipientOut.flush();
                        }
                        responseHandler.sendSuccessResponse("Message saved in db successfully");

                    }else{
                        responseHandler.sendErrorResponse("Message didn't save in db");
                    }
                    break;
                case "SEARCH_USERS":
                    JsonArray searchedUsers;
                    searchedUsers = SearchUsers.searchUsers(data.get("query").getAsString(),connection);
                    if(searchedUsers!=null){
                        responseHandler.sendSuccessResponse("Keys are sent successful","searchedUsers",searchedUsers);
                    }else{
                        responseHandler.sendErrorResponse("there is an error during searching other users!");
                }
                    break;
                case "SEARCH_A_USER":
                    int searchedUserID = SearchUsers.getIdOfUser(data.get("userName").getAsString(),connection);
                    if(searchedUserID!=0){
                        responseHandler.sendSuccessResponse("User's id is found, sending back","receiverId",searchedUserID);
                    }else{
                        responseHandler.sendErrorResponse("Couldn't find the user's id. There are some problem in server!");
                    }
                    break;
                case "CREATE_GROUP":
                    //converting JsonArray to list
                    List<String> members = gson.fromJson(data.get("members"), new TypeToken<List<String>>() {}.getType());

                   new CreateGroup(connection).createGroups(data.get("group_name").getAsString(), members);
                    responseHandler.sendSuccessResponse("Successfully created the group");
                    break;

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
