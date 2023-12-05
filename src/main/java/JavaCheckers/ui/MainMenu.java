package JavaCheckers.ui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.Serializable;

/*
 * A játék főmenüjét reprezentáló osztály.
 *
 */
public class MainMenu implements Serializable {

    /**
     * Konstruktor, amely létrehozza a főmenüt.
     */
    public MainMenu() {
        createAndShowGUI();
    }

    /*
     * A főmenü grafikus felületének létrehozása.
     */
    private void createAndShowGUI() {
        // Create the main frame
        JFrame frame = new JFrame("JavaCheckers ⚪️⚫️");

        // add label to top center of frame

        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(300, 200);
        frame.setLayout(new GridLayout(3, 1));
        frame.setLocationRelativeTo(null);

        // Create buttons
        JButton newGameButton = new JButton("New Game");
        JButton loadGameButton = new JButton("Load Game");
        JButton quitButton = new JButton("Quit");

        // Add ActionListener to buttons
        newGameButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Implement the action for starting a new game
                new CheckersGUI(false);
                frame.dispose();
            }
        });

        loadGameButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Implement the action for loading a game
                new CheckersGUI(true);
                frame.dispose();

            }
        });

        quitButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Implement the action for quitting the game
                System.exit(0);
            }
        });

        // Add buttons to the frame
        frame.add(newGameButton);
        frame.add(loadGameButton);
        frame.add(quitButton);

        // Set the frame to be visible
        frame.setVisible(true);
    }

    public static void main(String[] args) {
        // Run the main menu
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new MainMenu();
            }
        });

    }
}
