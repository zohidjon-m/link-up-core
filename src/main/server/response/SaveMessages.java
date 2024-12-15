package main.server.response;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class SaveMessages {

    public boolean sendOneToOneMessage(int senderId, int receiverId, String messageContent, Connection conn) {
        boolean status = false;

        String query = "INSERT INTO messages (sender_id, receiver_id, group_id, message) VALUES (?, ?, NULL, ?)";
        try (PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, senderId);
            stmt.setInt(2, receiverId);
            stmt.setString(3, messageContent);

            int rowsAffected = stmt.executeUpdate(); // Execute INSERT query
            status = rowsAffected > 0;// Check if a row was inserted
            if(status){
                initiateChat(senderId,receiverId,conn);
            }
        } catch (SQLException e) {
            System.err.println("Error while sending message: " + e.getMessage());
            throw new RuntimeException("Failed to send message", e);
        }

        return status;
    }
    private void initiateChat(int senderId, int receiverId,Connection conn){
        String checkChatExistsQuery = "SELECT chat_id FROM chats WHERE (user1_id = ? AND user2_id = ?) OR (user1_id = ? AND user2_id = ?)";
        try (PreparedStatement checkChatStmt = conn.prepareStatement(checkChatExistsQuery)) {
            checkChatStmt.setInt(1, senderId);
            checkChatStmt.setInt(2, receiverId);
            checkChatStmt.setInt(3, receiverId);
            checkChatStmt.setInt(4, senderId);

            ResultSet rs = checkChatStmt.executeQuery();
                 // If a record exists, the chat already exists
            if(!rs.next()){
                String insertChatQuery = "INSERT INTO chats (user1_id, user2_id) VALUES (?, ?)";
                try (PreparedStatement insertChatStmt = conn.prepareStatement(insertChatQuery)) {
                    insertChatStmt.setInt(1, senderId);
                    insertChatStmt.setInt(2, receiverId);
                    insertChatStmt.executeUpdate(); // Execute the INSERT query for the chat
                }

            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    // TODO: Implement sendGroupMessage method here
}
