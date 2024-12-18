package main.client.auth;

import com.google.gson.JsonObject;
import main.client.response.ResponeHandler;
import main.client.utility.ClientUtil;

import javax.swing.*;

import java.awt.*;
import java.io.IOException;

import static javax.swing.WindowConstants.DISPOSE_ON_CLOSE;

public class Register {

    public JFrame createGUI() {
        JFrame frame = new JFrame();
        frame.setTitle("Register");
        frame.setSize(600, 600);
        frame.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        frame.setLocationRelativeTo(null);
        frame.setLayout(new BorderLayout());

        JPanel panel = new JPanel();
        panel.setSize(500,400);
        JLabel usernameLabel = new JLabel("Username:");
        JTextField usernameField = new JTextField(20);
        JLabel passwordLabel = new JLabel("Password:");
        JPasswordField passwordField = new JPasswordField(20);
        JButton registerButton = new JButton("Register");


        panel.add(usernameLabel);
        panel.add(usernameField);
        panel.add(passwordLabel);
        panel.add(passwordField);
        panel.add(registerButton);
        frame.add(panel,BorderLayout.CENTER);
        frame.add(panel);
        frame.setVisible(true);

        registerButton.addActionListener(e -> {
            String username = usernameField.getText();
            String password = new String(passwordField.getPassword());

            //construct the login request using json
            try {
                ClientUtil client = new ClientUtil();
                JsonObject request = new JsonObject();
                request.addProperty("action","REGISTER");

                JsonObject data = new JsonObject();
                data.addProperty("username",username);
                data.addProperty("password",password);
                request.add("data",data);

                //Send the request and receive the response
                JsonObject response = client.sendRequest(request);
                JOptionPane.showMessageDialog(null, response.get("message").getAsString());
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }

        });

        return frame;
    }


}
