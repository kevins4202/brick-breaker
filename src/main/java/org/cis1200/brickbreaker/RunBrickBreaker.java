package org.cis1200.brickbreaker;

import javax.swing.*;
import java.awt.*;
import java.io.File;

public class RunBrickBreaker implements Runnable {
    private GameCourt court;

    @Override
    public void run() {
        final JFrame frame = new JFrame("TOP LEVEL FRAME");
        frame.setLocation(300, 300);

        // Status panel
        final JPanel status_panel = new JPanel();
        frame.add(status_panel, BorderLayout.SOUTH);
        final JLabel status = new JLabel("Running...");
        status_panel.add(status);

        // Main playing area
        court = new GameCourt(status);
        frame.add(court, BorderLayout.CENTER);

        // Reset button
        final JPanel control_panel = new JPanel();
        frame.add(control_panel, BorderLayout.NORTH);

        // Note here that when we add an action listener to the reset button, we
        // define it as an anonymous inner class that is an instance of
        // ActionListener with its actionPerformed() method overridden. When the
        // button is pressed, actionPerformed() will be called.
        final JButton reset = new JButton("Reset");
        reset.addActionListener(e -> court.reset(null));
        control_panel.add(reset);

        final JButton start = new JButton("Start");
        start.addActionListener(e -> {
            if (start.getText().equals("Start")) {
                start.setText("Pause");
                court.startGame();
                court.setPlaying(GameState.PLAYING);
                status.setText("Running... Bricks left: " + court.getBricksToDestroy());
            } else {
                start.setText("Start");
                court.setPlaying(GameState.STOPPED);
                status.setText("Paused...");
            }
        });
        control_panel.add(start);

        final JButton saveGameButton = new JButton("Save Game");
        saveGameButton.addActionListener(e -> {
            court.saveGame();
        });
        control_panel.add(saveGameButton);

        final JButton loadGameButton = getLoadGameButton();
        control_panel.add(loadGameButton);

        JOptionPane.showMessageDialog(
                null, // Use null instead of popup
                "Brickbreaker:\nUse your paddle (arrow keys) to bounce the balls against the blocks to destroy them.\n Destroy all blocks to win. Each block gives one point.\nYellow blocks spawn another ball on destruction. \nBalls disappear if they go past your paddle, and if you run out of balls you will lose.",
                "Instructions", // Added a title to the dialog
                JOptionPane.INFORMATION_MESSAGE
        );

        // // Timer to check the game state and update the Save Game button
        Timer gameStateChecker = new Timer(100, e -> {
            GameState playing = court.getPlaying(); // Replace with your actual GameCourt method

            if (playing.equals(GameState.RESET)) {
                loadGameButton.setVisible(true);
            } else {
                loadGameButton.setVisible(false);
            }

            if (playing.equals(GameState.STOPPED) || playing.equals(GameState.RESET)) {
                start.setText("Start");
                start.setVisible(true);
                saveGameButton.setVisible(true);
            } else if (playing.equals(GameState.PLAYING)) {
                start.setText("Pause");
                saveGameButton.setVisible(false);
                status.setText("Running... Bricks left: " + court.getBricksToDestroy());
            } else {
                start.setVisible(false);
                saveGameButton.setVisible(false);
            }
            // if (playing && !saveGameButton.isVisible()) {
            // saveGameButton.setVisible(true); // Show the button
            // } else if (!playing && saveGameButton.isVisible()) {
            // saveGameButton.setVisible(false); // Hide the button
            // }
            control_panel.revalidate(); // Refresh the layout
            control_panel.repaint(); // Redraw the panel

            // System.out.println(court.getPlaying());
            // System.out.println("\r");
        });
        gameStateChecker.start();

        // Put the frame on the screen
        frame.pack();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);

        court.reset(null);
        System.out.println("Brick Breaker run");
    }

    private JButton getLoadGameButton() {
        final JButton loadGameButton = new JButton("Load Game");
        loadGameButton.addActionListener(e -> {
            String fileName = JOptionPane.showInputDialog(
                    null,
                    "Enter the Save Number to load:",
                    "Load Game",
                    JOptionPane.PLAIN_MESSAGE
            );

            if (fileName != null && !fileName.trim().isEmpty()) {
                if (isValidFileName(
                        "src/main/java/org/cis1200/brickbreaker/saves/" + fileName + ".txt"
                )) { // Assuming a validation method
                    court.reset(
                            "src/main/java/org/cis1200/brickbreaker/saves/" + fileName + ".txt"
                    );
                } else {
                    // Display error message for invalid file name
                    JOptionPane.showMessageDialog(
                            null,
                            "The save number is invalid. Please try again.",
                            "Invalid Save Number",
                            JOptionPane.ERROR_MESSAGE
                    );
                }
            } else {
                // Handle the case where the user cancels or enters an empty name
                JOptionPane.showMessageDialog(
                        null,
                        "No file name entered.",
                        "Error",
                        JOptionPane.WARNING_MESSAGE
                );
            }
        });
        return loadGameButton;
    }

    private boolean isValidFileName(String fileName) {
        File file = new File(fileName);
        return file.exists() && !file.isDirectory();
    }

}
