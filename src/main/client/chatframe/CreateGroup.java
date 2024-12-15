package main.client.chatframe;

import javax.swing.*;
import java.io.IOException;
import java.util.List;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import main.client.response.ResponeHandler;
import main.client.utility.ClientUtil;
public class CreateGroup {

    JList<String> chatList;
    public CreateGroup(JList<String> chatList){
        this.chatList = chatList;
        openCreateGroupDialog();
    }

    private void openCreateGroupDialog() {

        JFrame createGroupFrame = new JFrame("Create Group");
        createGroupFrame.setSize(400, 300);
        createGroupFrame.setLayout(null);
        createGroupFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        // Group Name Input
        JLabel groupNameLabel = new JLabel("Group Name:");
        groupNameLabel.setBounds(20, 20, 100, 25);
        createGroupFrame.add(groupNameLabel);

        JTextField groupNameField = new JTextField();
        groupNameField.setBounds(130, 20, 200, 25);
        createGroupFrame.add(groupNameField);

        // User Selection
        JLabel selectUsersLabel = new JLabel("Select Users:");
        selectUsersLabel.setBounds(20, 60, 100, 25);
        createGroupFrame.add(selectUsersLabel);

        JList<String> userList = new JList<>();
        userList = chatList;
        userList.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
        userList.setBounds(130, 60, 200, 100);
        createGroupFrame.add(userList);

        // Create Group Button
        JButton createButton = new JButton("Create Group");
        createButton.setBounds(130, 200, 120, 30);
        createGroupFrame.add(createButton);

        JList<String> finalUserList = userList;
        createButton.addActionListener(e -> {
            String groupName = groupNameField.getText();
            List<String> selectedUsers = finalUserList.getSelectedValuesList();
            if (!groupName.isEmpty() && !selectedUsers.isEmpty()) {
                createGroupOnServer(groupName, selectedUsers);
                createGroupFrame.dispose();
            } else {
                JOptionPane.showMessageDialog(createGroupFrame, "Please fill all fields!");
            }
        });

        createGroupFrame.setVisible(true);
    }

    private void createGroupOnServer(String groupName, List<String> members) {
        try {
            ClientUtil client = new ClientUtil();
            JsonObject request = new JsonObject();
            Gson gson = new Gson();
            request.addProperty("action", "CREATE_GROUP");
            JsonObject data = new JsonObject();
            data.addProperty("group_name", groupName);
            data.add("members", gson.toJsonTree(members).getAsJsonArray());

            request.add("data",data);
            // Send the request
            JsonObject response = client.sendRequest(request);

            // Handle response (you can add a listener to update the main frame dynamically)

            //for now, creating group will not check the status of the action
            JOptionPane.showMessageDialog(null,"Successfully created!!!");



        } catch (IOException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Error communicating with server!");
        }
    }


}
