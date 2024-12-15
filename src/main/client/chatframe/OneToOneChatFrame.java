package main.client.chatframe;

import com.google.gson.JsonObject;
import main.client.server_info_inClient.ServerInfoInClient;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;
import java.util.ArrayList;

public class OneToOneChatFrame extends JFrame {

    private JPanel messagePanel; // Panel for messages
    private JTextField messageField; // Field to type messages
    private JButton sendButton; // Button to send messages
    private JScrollPane scrollPane; // Scroll pane for the messages
    private int currentUserId; // Current user ID (sender)
    private int receiverId = 0; // Receiver's user ID
    private String receiverName; // Receiver's username

    public OneToOneChatFrame(String receiverName) {
        this.currentUserId = ServerInfoInClient.getInstance().getUserId();
        this.receiverName = receiverName;

        setTitle("Chat with " + receiverName);
        setSize(600, 800);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout());

        // Message panel
        messagePanel = new JPanel();
        messagePanel.setLayout(new BoxLayout(messagePanel, BoxLayout.Y_AXIS));
        scrollPane = new JScrollPane(messagePanel);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);

        // Message input field and send button
        JPanel inputPanel = new JPanel();
        inputPanel.setLayout(new BorderLayout());
        messageField = new JTextField();
        sendButton = new JButton("Send");

        inputPanel.add(messageField, BorderLayout.CENTER);
        inputPanel.add(sendButton, BorderLayout.EAST);

        add(scrollPane, BorderLayout.CENTER);
        add(inputPanel, BorderLayout.SOUTH);

        // Load previous messages
        loadMessages();

        // Send button action listener
//        sendButton.addActionListener(e -> sendMessage());

        setVisible(true);
    }

    private int getReceiverId(String receiverName){
        JsonObject request = new JsonObject();


        // get user id by search user,
        return 0;
    }

    private void loadMessages(){
        JsonObject request = new JsonObject();
        request.addProperty("action","FETCH_MESSAGES");


    }



    // Message class to represent a chat message
    private static class Message {
        private int senderId;
        private String content;

        public Message(int senderId, String content) {
            this.senderId = senderId;
            this.content = content;
        }

        public int getSenderId() {
            return senderId;
        }

        public String getContent() {
            return content;
        }
    }
}