package main.client.auth;

import javax.swing.*;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class HandleAuth {
   String SERVER_ADDRESS = "127.0.0.1";
   int SERVER_PORT = 4242;

    protected void handleRequest(String requestType, String username, String password) {
        try (Socket socket = new Socket(SERVER_ADDRESS, SERVER_PORT);
             PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
            BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()))) {

            // Send request type, username, and password
            out.println(requestType);
            out.println(username);
            out.println(password);

            // Read server response
            String response = in.readLine();
            if ("REGISTER_SUCCESS".equalsIgnoreCase(response)) {
                JOptionPane.showMessageDialog(null, "Registration Successful!");
            } else if ("REGISTER_FAIL".equalsIgnoreCase(response)) {
                JOptionPane.showMessageDialog(null, "Registration Failed!");
            } else if ("LOGIN_SUCCESS".equalsIgnoreCase(response)) {
                JOptionPane.showMessageDialog(null, "Login Successful!");
            } else if ("LOGIN_FAIL".equalsIgnoreCase(response)) {
                JOptionPane.showMessageDialog(null, "Invalid Credentials!");
            }
        } catch (IOException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Server Error!");
        }
    }
}
