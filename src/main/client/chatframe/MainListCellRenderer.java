package main.client.chatframe;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.LineBorder;
import java.awt.*;

public class MainListCellRenderer extends DefaultListCellRenderer {

    @Override
    public Component getListCellRendererComponent(JList<?> list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
        // Get the default renderer component (JLabel)
        JLabel label = (JLabel) super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);

        // Make the text bigger
        label.setFont(new Font("Arial", Font.PLAIN, 16));

        // Add a border around each label
        Border border = new LineBorder(Color.LIGHT_GRAY, 1);
        label.setBorder(border);


        if (isSelected) {
            label.setBackground(Color.LIGHT_GRAY); // Background color for selected items
            label.setOpaque(true);
        }

        return label;
    }
}
