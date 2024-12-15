package main.server.response;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class SearchUsers {


    public static JsonArray searchUsers(String query, Connection conn){
        JsonArray users = new JsonArray();
        try {
           PreparedStatement stmt = conn.prepareStatement(
                    "SELECT username FROM users WHERE username LIKE ? LIMIT 20"
            );
            stmt.setString(1, "%" + query + "%");
            ResultSet rs = stmt.executeQuery();


            while (rs.next()) {
                users.add(rs.getString("username"));
            }
            return users;
        } catch (Exception e) {
            e.printStackTrace();

        }
        return users;
    }

    public static int getIdOfUser(String username, Connection conn){
        int userId = 0;
        try{
            PreparedStatement stmt = conn.prepareStatement(
                    "SELECT user_id FROM users WHERE username = ?"
            );
            stmt.setString(1,username);
            ResultSet rs = stmt.executeQuery();

            if(rs.next()){
                userId = rs.getInt("user_id");
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return userId;
    }
}
