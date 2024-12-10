package main.client.auth;

import javax.swing.*;
import java.awt.*;
import java.io.*;
import java.net.*;

public class Login {
//    private static final String SERVER_ADDRESS = "localhost";
//    private static final int SERVER_PORT = 4242;

    public Login() throws IOException {
        SwingUtilities.invokeLater(Login::createGUI);
    }

    private static void createGUI() {
        JFrame frame = new JFrame();
        JButton loginButton = new JButton("Login");
        JButton resetButton = new JButton("Reset");
        JTextField usernameField = new JTextField();
        JPasswordField userPasswordField = new JPasswordField();
        JLabel usernameLabel = new JLabel("Username ");
        JLabel userPasswordLabel = new JLabel("Password");
        JLabel messageLabel = new JLabel(" ");

        usernameLabel.setBounds(50,100,75,25);
        userPasswordLabel.setBounds(50,150,75,25);

        messageLabel.setBounds(125,250,250,35);
        messageLabel.setFont(new Font(null, Font.ITALIC, 25));

        usernameField.setBounds(125,100,200,25);
        userPasswordField.setBounds(125,150,200,25);

        loginButton.setBounds(125,200,100,25);
        loginButton.setFocusable(false);


        resetButton.setBounds(235,200,100,25);
        loginButton.setFocusable(false);


        frame.add(usernameLabel);
        frame.add(userPasswordLabel);
        frame.add(messageLabel);
        frame.add(usernameField);
        frame.add(userPasswordField);
        frame.add(loginButton);
        frame.add(resetButton);

        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(420,420);
        frame.setLayout(null);
        frame.setVisible(true);



        resetButton.addActionListener(e -> {
            usernameField.setText("");
            userPasswordField.setText("");
        });
        loginButton.addActionListener(e -> {
            String username = usernameField.getText();
            String password = new String(userPasswordField.getPassword());
            new HandleAuth().handleRequest("LOGIN", username, password);
        });
    }


//   protected static void handleRequest(String requestType, String username, String password) {
//        try (Socket socket = new Socket(SERVER_ADDRESS, SERVER_PORT);
//             PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
//             BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()))) {
//
//            // Send request type, username, and password
//            out.println(requestType);
//            out.println(username);
//            out.println(password);
//
//            // Read server response
//            String response = in.readLine();
//            if ("REGISTER_SUCCESS".equalsIgnoreCase(response)) {
//                JOptionPane.showMessageDialog(null, "Registration Successful!");
//            } else if ("REGISTER_FAIL".equalsIgnoreCase(response)) {
//                JOptionPane.showMessageDialog(null, "Registration Failed!");
//            } else if ("LOGIN_SUCCESS".equalsIgnoreCase(response)) {
//                JOptionPane.showMessageDialog(null, "Login Successful!");
//            } else if ("LOGIN_FAIL".equalsIgnoreCase(response)) {
//                JOptionPane.showMessageDialog(null, "Invalid Credentials!");
//            }
//        } catch (IOException e) {
//            e.printStackTrace();
//            JOptionPane.showMessageDialog(null, "Server Error!");
//        }
//    }
}
