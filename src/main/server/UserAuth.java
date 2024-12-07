package main.server;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import static main.server.LinkUpServer.connection;

public class UserAuth {

    protected boolean registerUser(String username, String password) {
        try {
            String hashedPassword = hashPassword(password);
            String query = "INSERT INTO Users (username, password) VALUES (?, ?)";
            PreparedStatement stmt = connection.prepareStatement(query);
            stmt.setString(1, username);
            stmt.setString(2, hashedPassword); // Use hashing in real applications!
            stmt.executeUpdate();
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    protected boolean authenticateUser(String username, String password) {
        try {
            String hashedPassword = hashPassword(password);
            String query = "SELECT * FROM Users WHERE username = ? AND password = ?";
            PreparedStatement stmt = connection.prepareStatement(query);
            stmt.setString(1, username);
            stmt.setString(2, hashedPassword);
            ResultSet rs = stmt.executeQuery();
            return rs.next();
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
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

