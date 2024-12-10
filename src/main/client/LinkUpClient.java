package main.client;

import main.client.auth.Login;
import main.client.auth.Register;

import javax.swing.*;

import java.awt.*;
import java.io.IOException;

public class LinkUpClient{




    public LinkUpClient() {
        JFrame frame = new JFrame();
        frame.setTitle("Welcome to Link Up");
        frame.setSize(600, 500);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLocationRelativeTo(null);
        frame.setLayout(null); // Use absolute positioning for components

        // Welcome label
        JLabel label = new JLabel("Welcome to Link Up");
        label.setBounds(100, 100, 400, 40);// Adjust position and size
        label.setFont(new Font(null, Font.BOLD, 35)) ;
        frame.add(label);

        // Login button
        JButton loginButton = new JButton("Login");
        loginButton.setBounds(200, 200, 150, 30); // Adjust position and size
        frame.add(loginButton);

        // Register button
        JButton registerButton = new JButton("Register");
        registerButton.setBounds(200, 250, 150, 30); // Adjust position and size
        frame.add(registerButton);

        // Set frame visibility
        frame.setVisible(true);

        // Add action listeners for buttons
        loginButton.addActionListener(e -> {
            try {
                new Login();
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }
            frame.dispose();
        });

        registerButton.addActionListener(e -> {
            new Register();
            frame.dispose();
            // Open the register frame here
        });

    }

    public static void main(String[] args) {
        new LinkUpClient();
    }
}
