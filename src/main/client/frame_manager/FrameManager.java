package main.client.frame_manager;

import javax.swing.*;
import java.util.Stack;

public class FrameManager {

    public static Stack<JFrame> frameStack = new Stack<>();

    //Add new frame

    public static void navigateTo(JFrame newFrame){
        if(!frameStack.isEmpty()) frameStack.peek().dispose();

        frameStack.push(newFrame);
        newFrame.setVisible(true);
    }

    //Back to the previous frame
    public static void goBack() {
        if (frameStack.size() > 1) { // Ensure there's a previous frame
            frameStack.pop().dispose(); // Dispose the current frame
            frameStack.peek().setVisible(true); // Show the previous frame
        } else {
            JOptionPane.showMessageDialog(null,"No previous frame go back to!", "Back",JOptionPane.WARNING_MESSAGE);
        }
    }

    // Close all frames and exit
    public static void exitApplication() {
        while (!frameStack.isEmpty()) {
            frameStack.pop().dispose();
        }
        System.exit(0);
    }
}
