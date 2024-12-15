package main.server.response;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class FetchChats {
    private Connection con;
    private String username;

    public FetchChats(Connection conn){
        this.con=conn;
    }
    public int getUserIdByUserName(String userName){
        int  userID = -1;
        try{
            String toGetUserID  = "SELECT user_id FROM users WHERE username = ?";
            PreparedStatement getUserIDstmt = con.prepareStatement(toGetUserID);
            getUserIDstmt.setString(1,userName);

            ResultSet userIdRs = getUserIDstmt.executeQuery();
            if(userIdRs.next()){
                userID= userIdRs.getInt("user_id");
            }


        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return userID;
    }
    public JsonObject fetchChats(int currentUserId){
        JsonObject data = new JsonObject();
        JsonArray oneToOneChats = new JsonArray();
        JsonArray groupChats = new JsonArray();

        try{
            String oneToOneQuery = "SELECT c.chat_id, " +
                    "IF(c.user1_id = ?, u2.username, u1.username) AS chat_partner_name, " +
                    "IF(c.user1_id = ?, u2.user_id, u1.user_id) AS chat_partner_id " +
                    "FROM chats c " +
                    "JOIN users u1 ON c.user1_id = u1.user_id " +
                    "JOIN users u2 ON c.user2_id = u2.user_id " +
                    "WHERE c.user1_id = ? OR c.user2_id = ?";

            PreparedStatement oneToOneStmt = con.prepareStatement(oneToOneQuery);

            oneToOneStmt.setInt(1, currentUserId);
            oneToOneStmt.setInt(2, currentUserId);
            oneToOneStmt.setInt(3, currentUserId);
            oneToOneStmt.setInt(4, currentUserId);

            ResultSet rsOneToOne = oneToOneStmt.executeQuery();
            while(rsOneToOne.next()){
                JsonObject chat = new JsonObject();
                chat.addProperty("chat_id", rsOneToOne.getInt("chat_id"));
                chat.addProperty("chat_partner_id", rsOneToOne.getInt("chat_partner_id"));
                chat.addProperty("chat_partner_name", rsOneToOne.getString("chat_partner_name"));
                oneToOneChats.add(chat);
            }

            // Fetch Group Chats
            String groupQuery = "SELECT g.group_id, g.group_name " +
                    "FROM groupchats g " +
                    "JOIN groupmembers gm ON g.group_id = gm.group_id " +
                    "WHERE gm.user_id = ?";

            PreparedStatement groupStmt = con.prepareStatement(groupQuery);
            groupStmt.setInt(1, currentUserId);

            ResultSet rsGroups = groupStmt.executeQuery();

            while(rsGroups.next()){
                JsonObject group = new JsonObject();
                group.addProperty("group_id", rsGroups.getInt("group_id"));
                group.addProperty("group_name", rsGroups.getString("group_name"));
                groupChats.add(group);
            }

            // Build Response JSON
            data.add("oneToOneChats",oneToOneChats);
            data.add("groupChats",groupChats);

            return data;

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
