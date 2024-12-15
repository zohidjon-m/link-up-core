package main.server.auth;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;


public class UserAuth {
    Connection connection;
    public UserAuth(Connection con) {
        this.connection = con;
    }

    public boolean registerUser(String username, String password) {
       boolean result = false;
        try {

            // Check if the username already exists
            String checkQuery = "SELECT COUNT(*) FROM Users WHERE username = ?";
            PreparedStatement checkStmt = connection.prepareStatement(checkQuery);
            checkStmt.setString(1, username);
            ResultSet resultSet = checkStmt.executeQuery();

            if (resultSet.next() && resultSet.getInt(1) > 0) {
                // Username already exists
                System.out.println("User is already registered.");
                return false;
            }else{
                String hashedPassword = hashPassword(password);
                String query = "INSERT INTO Users (username, password) VALUES (?, ?)";
                PreparedStatement stmt = connection.prepareStatement(query);
                stmt.setString(1, username);
                stmt.setString(2, hashedPassword); // Use hashing in real applications!
                stmt.executeUpdate();
                result =  true;
            }



        } catch (SQLException e) {
            e.printStackTrace();

        }
        return result;
    }

    public int authenticateUser(String username, String password) {
        int userId = -1;
        try {
            String hashedPassword = hashPassword(password);
            String query = "SELECT * FROM Users WHERE username = ? AND password = ?";
            PreparedStatement stmt = connection.prepareStatement(query);
            stmt.setString(1, username);
            stmt.setString(2, hashedPassword);
            ResultSet rs = stmt.executeQuery();
            if(rs.next()) {
                userId = rs.getInt("user_id");
            }

       } catch (SQLException e) {
            e.printStackTrace();

            //send ERROR message
        }
        return userId;
    }

    public static String hashPassword(String password) {
        try {
            // Get an instance of SHA-256
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hashedBytes = digest.digest(password.getBytes());

            // Convert bytes to hexadecimal format
            StringBuilder hexString = new StringBuilder();
            for (byte b : hashedBytes) {
                String hex = Integer.toHexString(0xff & b);
                if (hex.length() == 1) hexString.append('0');
                hexString.append(hex);
            }
            return hexString.toString();
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("Error: Hashing algorithm not found.", e);
        }
    }
}

