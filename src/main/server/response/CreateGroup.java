package main.server.response;

import java.sql.Connection;
import java.sql.*;
import java.util.*;



public class CreateGroup {
    Connection connection;
    public CreateGroup(Connection con){
        this.connection = con;
    }
    public void createGroups(String groupName, List<String> groupMembers) throws SQLException {

        PreparedStatement groupStmt = connection.prepareStatement(
                "INSERT INTO groupchats (group_name) VALUES (?)",
                Statement.RETURN_GENERATED_KEYS
        );
        groupStmt.setString(1, groupName);
        groupStmt.executeUpdate();
        ResultSet rs = groupStmt.getGeneratedKeys();

        int groupId = 0;
        if (rs.next()) {
            groupId = rs.getInt(1);
        }

        PreparedStatement memberStmt = connection.prepareStatement(
                "INSERT INTO groupmembers (group_id, user_id) VALUES (?, ?)"
        );
        for (String groupMember : groupMembers) {
            int memberId = getUserIdByUsername(groupMember); // Implement this method
            memberStmt.setInt(1, groupId);
            memberStmt.setInt(2, memberId);
            memberStmt.addBatch();
        }
        int[] resultSet = memberStmt.executeBatch();

    }
    private int getUserIdByUsername(String username) throws SQLException {
        // Query the 'users' table to get user_id for the given username
        PreparedStatement stmt = connection.prepareStatement("SELECT user_id FROM users WHERE username = ?");
        stmt.setString(1, username);
        ResultSet rs = stmt.executeQuery();
        if (rs.next()) {
            return rs.getInt("user_id");
        }
        return -1; // or throw an exception if user not found
    }


}
