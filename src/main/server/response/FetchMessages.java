package main.server.response;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class FetchMessages {

    public JsonArray fetchOneToOneMessage(int senderId, int receiverId, Connection conn) {
        JsonArray messagesArray = new JsonArray();
        try {
            String query = "SELECT * FROM messages WHERE " +
                    "(sender_id = ? AND receiver_id = ?) OR " +
                    "(sender_id = ? AND receiver_id = ?) ORDER BY timestamp";
            PreparedStatement stmt = conn.prepareStatement(query);
            stmt.setInt(1, senderId);
            stmt.setInt(2, receiverId);
            stmt.setInt(3, receiverId);
            stmt.setInt(4, senderId);

            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                JsonObject messageJson = new JsonObject();
                messageJson.addProperty("senderId", rs.getInt("sender_id"));
                messageJson.addProperty("receiverId", rs.getInt("receiver_id"));
                messageJson.addProperty("content", rs.getString("message"));
                messageJson.addProperty("timestamp", rs.getTimestamp("timestamp").toString());
                messagesArray.add(messageJson);
            }

            //for debugging
            if(messagesArray.isEmpty()){
                System.out.println("there are no previous conversation between "+senderId+" and "+receiverId);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return messagesArray;
    }

}
