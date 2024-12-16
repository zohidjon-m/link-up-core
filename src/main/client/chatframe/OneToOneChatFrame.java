package main.client.chatframe;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import main.client.response.ResponeHandler;
import main.client.server_info_inClient.ServerInfoInClient;
import main.client.utility.ClientUtil;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.BufferedReader;
import java.io.IOException;
import java.sql.Array;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class OneToOneChatFrame extends JFrame {

    private JPanel messagePanel; // Panel for messages
    private JTextField messageField; // Field to type messages
    private JButton sendButton; // Button to send messages
    private JScrollPane scrollPane; // Scroll pane for the messages
    private int currentUserId; // Current user ID (sender)
    private int receiverId = 0; // Receiver's user ID
    private String receiverName; // Receiver's username

    ClientUtil clientUtil = new ClientUtil();

    public void OneToOneChat(String receiverName) throws IOException {
        this.currentUserId = ServerInfoInClient.getInstance().getUserId();
        this.receiverName = receiverName;
        this.receiverId = getReceiverId(receiverName);
        if(receiverId==-1){
            throw new IOException("receiverID is -1");
        }

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
        messageField.addActionListener(e ->{
            try {
                handleSendMessage();
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }
        });
        sendButton = new JButton("Send");
        sendButton.addActionListener(e ->{
            try {
                handleSendMessage();
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }
        });

        inputPanel.add(messageField, BorderLayout.CENTER);
        inputPanel.add(sendButton, BorderLayout.EAST);

        add(scrollPane, BorderLayout.CENTER);
        add(inputPanel, BorderLayout.SOUTH);

        // Load previous messages
        loadMessages();




        setVisible(true);
    }

    private int getReceiverId(String receiverName) throws IOException {
        JsonObject request = new JsonObject();
        request.addProperty("action","SEARCH_A_USER");
        JsonObject data = new JsonObject();
        data.addProperty("userName",receiverName);
        request.add("data",data);

        JsonObject response = clientUtil.sendRequest(request);
        JsonObject responseData  = new JsonObject();
        int receiverID = -1;


        if(ResponeHandler.checkResponse(response)) {

            if (response.has("receiverId") && !response.get("receiverId").isJsonNull()) {
                receiverID = response.get("receiverId").getAsInt();
            } else {
                throw new IOException("Missing 'receiverId' in the response data.");
            }
        } else {
            throw new IOException("Missing 'data' field in the server response.");
        }


        // get user id by search user,
        return receiverID;
    }
    private void handleSendMessage() throws IOException {
        String messageContent = messageField.getText().trim();
        if(!messageContent.isEmpty()){
            sendMessage(messageContent);

            addMessageToPanel(messageContent, true, String.valueOf(new Timestamp(System.currentTimeMillis())));
            messageField.setText("");
        }

    }

    public void addMessageToPanel(String messageContent, boolean isSender, String timestamp) {
        // Outer container for the entire message
        JPanel messageContainer = new JPanel(new BorderLayout());
        messageContainer.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10)); // Add spacing between messages

        // Message bubble panel
        JPanel messageBubble = new JPanel();
        messageBubble.setLayout(new BoxLayout(messageBubble, BoxLayout.Y_AXIS)); // Stack message text and timestamp vertically
        messageBubble.setBorder(BorderFactory.createEmptyBorder(10, 15, 10, 15)); // Padding inside the bubble
        messageBubble.setOpaque(true);
        messageBubble.setBackground(isSender ? new Color(173, 216, 230) : new Color(240, 240, 240)); // Colors for sender/receiver
        messageBubble.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(200, 200, 200), 1), // Subtle border
                BorderFactory.createEmptyBorder(10, 10, 10, 10) // Inner padding
        ));
        messageBubble.setMaximumSize(new Dimension(100, Integer.MAX_VALUE)); // Limit bubble width for readability

        // Message text
        JLabel messageLabel = new JLabel("<html><p style='width: 300px; margin: 0;'>" + messageContent + "</p></html>");
        messageLabel.setFont(new Font("Arial", Font.PLAIN, 14));
        messageLabel.setForeground(Color.BLACK);

        // Timestamp
        JLabel timestampLabel = new JLabel(timestamp);
        timestampLabel.setFont(new Font("Arial", Font.ITALIC, 10));
        timestampLabel.setForeground(Color.GRAY);
        timestampLabel.setAlignmentX(isSender ? JLabel.RIGHT_ALIGNMENT : JLabel.LEFT_ALIGNMENT); // Align timestamp with the message

        // Add text and timestamp to the bubble
        messageBubble.add(messageLabel);
        messageBubble.add(Box.createVerticalStrut(5)); // Spacer between text and timestamp
        messageBubble.add(timestampLabel);

        // Avatar
        JLabel avatarLabel = new JLabel();
        avatarLabel.setPreferredSize(new Dimension(40, 40));
        avatarLabel.setOpaque(false);

        if (isSender) {
            avatarLabel.setIcon(new ImageIcon("path_to_sender_avatar.png")); // Set sender's avatar
            messageContainer.add(avatarLabel, BorderLayout.EAST);
            messageContainer.add(messageBubble, BorderLayout.CENTER);
        } else {
            avatarLabel.setIcon(new ImageIcon("path_to_receiver_avatar.png")); // Set receiver's avatar
            messageContainer.add(avatarLabel, BorderLayout.WEST);
            messageContainer.add(messageBubble, BorderLayout.CENTER);
        }

        // Add the container to the message panel
        messagePanel.add(messageContainer);
        messagePanel.revalidate();
        scrollPane.getVerticalScrollBar().setValue(scrollPane.getVerticalScrollBar().getMaximum());



    }


    private void sendMessage(String message) throws IOException {
        JsonObject request = new JsonObject();
        request.addProperty("action","SEND_MESSAGE_ONE");
        JsonObject data = new JsonObject();
        data.addProperty("senderId",currentUserId);
        data.addProperty("receiverId",receiverId);
        data.addProperty("messageContent",message);
        request.add("data",data);

        JsonObject response =  clientUtil.sendRequest(request);
        // we can use this part later to notify a user whether the message has sent or not.
        ResponeHandler.checkResponse(response);
    }

    private void loadMessages() throws IOException {
        JsonObject request = new JsonObject();
        request.addProperty("action","FETCH_MESSAGES_ONE");
        JsonObject data = new JsonObject();
        data.addProperty("senderId",currentUserId);
        data.addProperty("receiverId",receiverId);
        request.add("data",data);

        JsonObject response = clientUtil.sendRequest(request);
        if(!response.get("messageArray").isJsonNull()){
            JsonArray messagesArray = response.getAsJsonArray("messageArray");
            if (messagesArray.size() > 0) {
                // Create a list to store messages with timestamps
                ArrayList<JsonObject> messageList = new ArrayList<>();

                // Populate the list
                for (int i = 0; i < messagesArray.size(); i++) {
                    messageList.add(messagesArray.get(i).getAsJsonObject());
                }

                // Sort messages by timestamp
                messageList.sort((m1, m2) -> {
                    String timestamp1 = m1.get("timestamp").getAsString();
                    String timestamp2 = m2.get("timestamp").getAsString();
                    return Timestamp.valueOf(timestamp1).compareTo(Timestamp.valueOf(timestamp2));
                });

                // Display sorted messages
                for (JsonObject msg : messageList) {
                    int senderId = msg.get("senderId").getAsInt();
                    String content = msg.get("content").getAsString();
                    String timestamp = msg.get("timestamp").getAsString();


                    boolean isSender = (senderId == currentUserId);
                    addMessageToPanel(content, isSender,timestamp);
                }

            }else{
                // No messages, display a placeholder message
                addPlaceholderMessage();
            }
        }

    }

    private void addPlaceholderMessage() {
        JLabel placeholderLabel = new JLabel("Start your first conversation!", SwingConstants.CENTER);
        placeholderLabel.setForeground(Color.GRAY);
        placeholderLabel.setBorder(BorderFactory.createEmptyBorder(20, 10, 20, 10));
        placeholderLabel.setFont(new Font("Arial", Font.ITALIC, 14));

        messagePanel.add(placeholderLabel);
        messagePanel.revalidate();
        messagePanel.repaint();
    }
}