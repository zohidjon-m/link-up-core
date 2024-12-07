package main.client.auth;

import javax.swing.*;

import static javax.swing.WindowConstants.DISPOSE_ON_CLOSE;


public class Register {
    public Register() {
        JFrame frame = new JFrame();
        frame.setTitle("Register");
        frame.setSize(600, 600);
        frame.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        frame.setLocationRelativeTo(null);

        JPanel panel = new JPanel();
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
        frame.add(panel);
        frame.setVisible(true);

        registerButton.addActionListener(e -> {
            String username = usernameField.getText();
            String password = new String(passwordField.getPassword());
           new HandleAuth().handleRequest("REGISTER",username, password);});
    }


}
