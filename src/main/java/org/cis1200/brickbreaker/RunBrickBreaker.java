package org.cis1200.brickbreaker;

import javax.swing.*;
import java.awt.*;

public class RunBrickBreaker implements Runnable {
    /**
     *
     */

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
        final GameCourt court = new GameCourt(status);
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
                court.setPlaying(true);
                status.setText("Running...");
            } else {
                start.setText("Start");
                court.setPlaying(false);
                status.setText("Paused...");
            }
        });
        control_panel.add(start);

        final JButton saveGameButton = new JButton("Save Game");
        saveGameButton.addActionListener(e -> {
            court.saveGame();
        });
        control_panel.add(saveGameButton);

//        // Timer to check the game state and update the Save Game button
        Timer gameStateChecker = new Timer(100, e -> {
            boolean playing = court.getPlaying(); // Replace with your actual GameCourt method

            if (!playing) {
                start.setText("Start");
                saveGameButton.setVisible(true);
            } else {
                start.setText("Pause");
                saveGameButton.setVisible(false);
            }
//            if (playing && !saveGameButton.isVisible()) {
//                saveGameButton.setVisible(true); // Show the button
//            } else if (!playing && saveGameButton.isVisible()) {
//                saveGameButton.setVisible(false); // Hide the button
//            }
            control_panel.revalidate(); // Refresh the layout
            control_panel.repaint();   // Redraw the panel

            System.out.println(court.getPlaying());
            System.out.println("\r");
        });
        gameStateChecker.start();

        // Put the frame on the screen
        frame.pack();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);

        court.reset(null);
    }
}
