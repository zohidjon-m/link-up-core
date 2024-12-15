package main.client.auth;

import com.google.gson.JsonObject;
import main.client.chatframe.MainFrame;
import main.client.frame_manager.FrameManager;
import main.client.server_info_inClient.ServerInfoInClient;
import main.client.utility.ClientUtil;
import main.client.response.ResponeHandler;

import javax.swing.*;
import java.awt.*;
import java.io.*;


public class Login {
    String user;

    public JFrame createGUI() {
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
            //add user because if it fails to fetch this user's id, it will use username
            user = username;
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
                if(ResponeHandler.checkResponse(response)){

                    if(response.has("value")&&(response.get("value").getAsInt() != 0)) {
                        ServerInfoInClient.getInstance().setUserId(response.get("value").getAsInt());
                        System.out.println("userID: "+ServerInfoInClient.getInstance().getUserId());
                        FrameManager.navigateTo(new MainFrame());

                    }else{
                        ServerInfoInClient.getInstance().setUserNameOfCurrent(user);

                        FrameManager.navigateTo(new MainFrame());

                    }

                }

            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }
        });
        return frame;
    }
}
