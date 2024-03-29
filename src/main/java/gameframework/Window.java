package gameframework;

import javax.swing.*;

/**
 * Creates frame and set its properties.
 */

public class Window extends JFrame {

    private Window() {
        // Sets the title for this frame.
        this.setTitle("survival of the fittest");

        // Sets size of the frame.
        if (true) // Full screen mode
        {
            // Disables decorations for this frame.
            this.setUndecorated(true);
            // Puts the frame to full screen.
            this.setExtendedState(MAXIMIZED_BOTH);
        } else // Window mode
        {
            // Size of the frame.
            this.setSize(1024, 768);
            // Puts frame to center of the screen.
            this.setLocationRelativeTo(null);
            // So that frame cannot be resizable by the user.
            this.setResizable(false);
        }

        // Exit the application when user close frame.
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // Creates the instance of the Framework.java that extends the Canvas.java and puts it on the frame.
        this.setContentPane(new Framework());

        this.setVisible(true);
    }

    public static void main(String[] args) {
        // Use the event dispatch thread to build the UI for thread-safety.
        SwingUtilities.invokeLater(Window::new);
    }
}
