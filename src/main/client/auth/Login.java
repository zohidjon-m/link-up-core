package main.client.auth;

import com.google.gson.JsonObject;
import main.client.utility.ClientUtil;

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

            //construct the login request using json
            try {
                ClientUtil client = new ClientUtil();
                JsonObject request = new JsonObject();
                request.addProperty("action","LOGIN");

                JsonObject data = new JsonObject();
                data.addProperty("username",username);
                data.addProperty("password",password);
                request.add("data",data);

                //Send the request and receive the response
                JsonObject response = client.sendRequest(request);
                if(response.get("status").getAsString().equals("SUCCESS")){
                    JOptionPane.showMessageDialog(null, response.get("message").getAsString());

                }else if (response.get("status").getAsString().equals("ERROR")) {
                    JOptionPane.showMessageDialog(null, response.get("message").getAsString());
                }

            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }
        });
    }
}
