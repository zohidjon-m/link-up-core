package main.server.response;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class SearchUsers {


    public static JsonArray searchUser(String query, Connection conn){
        try {
           PreparedStatement stmt = conn.prepareStatement(
                    "SELECT username FROM users WHERE username LIKE ? LIMIT 20"
            );
            stmt.setString(1, "%" + query + "%");
            ResultSet rs = stmt.executeQuery();

            JsonArray users = new JsonArray();
            while (rs.next()) {
                users.add(rs.getString("username"));
            }
            return users;
        } catch (Exception e) {
            e.printStackTrace();

        }
        return null;
    }
}
