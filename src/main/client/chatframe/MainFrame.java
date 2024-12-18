
package main.client.chatframe;

import com.google.gson.JsonArray;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.*;
import java.awt.event.*;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Objects;
import java.util.stream.Collectors;

public class MainFrame extends JFrame {

    // Components for the left and right panels
    private JPanel leftPanel, rightPanel;
    private JButton allButton, chatsButton, groupsButton;
    private JTextField searchField;
    private JList<String> chatList;
    private DefaultListModel<String> chatListModel;

    private ArrayList<String> allChats;
    private ArrayList<String> oneToOneChats ;
    private ArrayList<String> groupChats;
    private ArrayList<String> copyOfAll;

    public MainFrame() {
        setTitle("LinkUp");
        setSize(800, 600);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());
        setBackground(new Color(204, 232, 215));

        // Initialize sample chat data
        FetchChats fetch = new FetchChats();
        allChats = fetch.getAllChats();
        copyOfAll = (ArrayList<String>) allChats.clone();
        oneToOneChats = fetch.getOneToOneChats();
        groupChats = fetch.getGroupChats();

        // Create left and right panels
        createLeftPanel();
        createRightPanel();

        // Add panels to the frame
        add(leftPanel, BorderLayout.WEST);
        add(rightPanel, BorderLayout.CENTER);

        setVisible(true);
    }

    private void createLeftPanel() {
        leftPanel = new JPanel();
        leftPanel.setLayout(new GridLayout(8,1,5,5));
        leftPanel.setPreferredSize(new Dimension(150, getHeight()));
        leftPanel.setBackground(new Color(173, 216, 230));

        // Create buttons
        allButton = new JButton("All");
        chatsButton = new JButton("Chats");
        groupsButton = new JButton("Groups");

        JButton createGroup = new JButton("Create Group");
        createGroup.setBounds(10,500,150,30);

        allButton.setForeground(Color.BLACK);

        allButton.setBackground(new Color(173, 230, 187));
        chatsButton.setBackground(new Color(173, 230, 187));
        groupsButton.setBackground(new Color(173, 230, 187));
        createGroup.setBackground(new Color(173, 188, 230));
        // Add action listeners
        allButton.addActionListener(e -> updateChatList(allChats));
        chatsButton.addActionListener(e -> updateChatList(oneToOneChats));
        groupsButton.addActionListener(e -> updateChatList(groupChats));
        createGroup.addActionListener(e -> new CreateGroup(chatList));

        // Add buttons to the panel
        leftPanel.add(allButton);
        leftPanel.add(chatsButton);
        leftPanel.add(groupsButton);
        leftPanel.add(createGroup);
    }

    private void createRightPanel() {
        rightPanel = new JPanel();
        rightPanel.setLayout(new BorderLayout());

        // Create search bar
        searchField = new JTextField(" /search your friend ...");
        searchField.setPreferredSize(new Dimension(0, 30));
        searchField.addFocusListener(new FocusListener() {
            @Override
            public void focusGained(FocusEvent e) {
                if (searchField.getText().equals(" /search your friend ...")) {
                    searchField.setText("");
                }
            }

            @Override
            public void focusLost(FocusEvent e) {
                if (searchField.getText().trim().isEmpty()) {
                    searchField.setText(" /search your friend ...");
                }
            }
        });
        searchField.addKeyListener(new KeyAdapter() {
            @Override
            public void keyReleased(KeyEvent e) {
                filterChatList(searchField.getText());
            }
        });
        searchField.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
                updateSearchResult();
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                updateSearchResult();
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
                updateSearchResult();
            }
            Timer searchDelayTimer;
            public void updateSearchResult(){
                if (searchDelayTimer != null && searchDelayTimer.isRunning()) {
                    searchDelayTimer.stop();
                }

                searchDelayTimer = new Timer(300, e -> { // Delay of 300 ms
                    String query = searchField.getText().trim();
                    if (!query.isEmpty()) {
                        ArrayList<String> searchResults = new SearchUsers().searchUser(query);
                        System.out.println("Search Query: " + query + ", Results: " + searchResults);
                        updateChatList(searchResults != null ? searchResults : new ArrayList<>());
                    } else {
                        updateChatList(allChats); // Reset to all chats
                    }
                });
                searchDelayTimer.setRepeats(false);
                searchDelayTimer.start();
            }

        });

        // Create chat list
        chatListModel = new DefaultListModel<>();
        chatList = new JList<>(chatListModel);
        chatList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        chatList.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {

                int index = chatList.locationToIndex(e.getPoint());

                if (e.getClickCount() == 2) {
                    String selectedChat = chatList.getSelectedValue();
                    if (selectedChat != null) {
                        try {
                            openChatFrame(selectedChat);
                        } catch (IOException ex) {
                            throw new RuntimeException(ex);
                        }
                    }
                }else if(e.getClickCount()==1){
                    if(index == chatList.getSelectedIndex()){

                        chatList.clearSelection();
                    }
                }
            }
        });

        chatList.setCellRenderer(new MainListCellRenderer());

        // Update the chat list with all chats initially
        updateChatList(allChats);

        // Add components to the right panel
        rightPanel.add(searchField, BorderLayout.NORTH);
        rightPanel.add(new JScrollPane(chatList), BorderLayout.CENTER);
    }

    private void updateChatList(ArrayList<String> chats) {
        chatListModel.clear();
        if(chats.isEmpty()){
            chatListModel.addElement("No Chats are available. Start a conversation!");
        }else {
            for (String chat : chats) {
                chatListModel.addElement(chat);
            }
        }
    }

    private void filterChatList(String query) {
        ArrayList<String> filteredChats = (ArrayList<String>) allChats.stream()
                .filter(chat -> chat.toLowerCase().contains(query.toLowerCase()))
                .collect(Collectors.toList());
        updateChatList(filteredChats);
    }

    private void openChatFrame(String chatName) throws IOException {
//        JOptionPane.showMessageDialog(this, "Opening chat with: " + chatName);
        new OneToOneChatFrame().OneToOneChat(chatName);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(MainFrame::new);
    }
}
