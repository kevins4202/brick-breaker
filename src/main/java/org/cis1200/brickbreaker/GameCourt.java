package org.cis1200.brickbreaker;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Random;

/**
 * GameCourt
 *
 * This class holds the primary game logic for how different objects interact
 * with one another. Take time to understand how the timer interacts with the
 * different methods and how it repaints the GUI on every tick().
 */
public class GameCourt extends JPanel {

    // the state of the game logic
    private Brick[][] bricks;
    private Player player;
    private final ArrayList<Ball> balls = new ArrayList<>();
    private final Random rand = new Random();

    private boolean playing = false; // whether the game is running
    private final JLabel status; // Current status text, i.e. "Running..."

    private int bricksToDestroy = -1;

    // Game constants
    public static final int COURT_WIDTH = 600;
    public static final int COURT_HEIGHT = 400;
    public static final int PLAYER_VELOCITY = 6;
    public static final int rows = 5;

    // Update interval for timer, in milliseconds
    public static final int INTERVAL = 35;

    public GameCourt(JLabel status) {
        // creates border around the court area, JComponent method
        setBorder(BorderFactory.createLineBorder(Color.BLACK));

        // The timer is an object which triggers an action periodically with the
        // given INTERVAL. We register an ActionListener with this timer, whose
        // actionPerformed() method is called each time the timer triggers. We
        // define a helper method called tick() that actually does everything
        // that should be done in a single time step.
        Timer timer = new Timer(INTERVAL, e -> tick());
        timer.start(); // MAKE SURE TO START THE TIMER!

        // Enable keyboard focus on the court area. When this component has the
        // keyboard focus, key events are handled by its key listener.
        setFocusable(true);

        // This key listener allows the square to move as long as an arrow key
        // is pressed, by changing the square's velocity accordingly. (The tick
        // method below actually moves the square.)
        addKeyListener(new KeyAdapter() {
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_LEFT) {
                    player.setVx(-PLAYER_VELOCITY);
                } else if (e.getKeyCode() == KeyEvent.VK_RIGHT) {
                    player.setVx(PLAYER_VELOCITY);
                }
            }

            public void keyReleased(KeyEvent e) {
                player.setVx(0);
                player.setVy(0);
            }
        });

        this.status = status;
    }

    /**
     * (Re-)set the game to its initial state.
     */
    public void reset() {
        bricksToDestroy = rows * COURT_WIDTH / Brick.WIDTH;
        bricks = new Brick[COURT_WIDTH / Brick.WIDTH][rows];
        for (int i = 0; i < bricks.length; i++) {
            for (int j = 0; j < bricks[0].length; j++) {
                bricks[i][j] = new Brick(
                        COURT_WIDTH, COURT_HEIGHT,
                        new Color(rand.nextInt(256), rand.nextInt(256), rand.nextInt(256)),
                        i * Brick.WIDTH, j * Brick.HEIGHT + 50
                );
            }
        }

        player = new Player(COURT_WIDTH, COURT_HEIGHT, Color.BLACK);

        balls.add(new Ball(COURT_WIDTH, COURT_HEIGHT, Color.YELLOW, 50, 4));
        balls.add(new Ball(COURT_WIDTH, COURT_HEIGHT, Color.YELLOW, 350, -4));


        playing = true;
        status.setText("Running...");

        // Make sure that this component has the keyboard focus
        requestFocusInWindow();
    }

    /**
     * This method is called every time the timer defined in the constructor
     * triggers.
     */
    void tick() {
        if (playing) {
            // advance the square and snitch in their current direction.
            if (balls.isEmpty()) {
                playing = false;
                status.setText("Game Over");
            }

            if (bricksToDestroy == 0) {
                playing = false;
                status.setText("You Win!");
            }

            player.move();
            for (Ball b : balls)
                b.move();

            // make the snitch bounce off walls...
            LinkedList<Ball> ballsToRemove = new LinkedList<>();
            for (Ball b : balls) {
                Direction dir = b.hitWall();
                if (dir == Direction.FAIL) {
                    ballsToRemove.add(b);
                } else {
                    b.bounce(dir);
                }
            }

            for (Ball b: ballsToRemove)
                balls.remove(b);

            for (Ball b : balls)
                b.bounce(b.hitPlayer(player));

            for (int i = 0; i < balls.size(); i++) {
                for (int j = i + 1; j < balls.size(); j++) {
                    Ball b1 = balls.get(i);
                    Ball b2 = balls.get(j);

                    b1.bounce(b1.hitObj(b2));
                    b2.bounce(b2.hitObj(b1));
                }
            }

            for (Ball b : balls)
                for (Brick[] barr : bricks) {
                    for (Brick brick : barr) {
                        Direction dir = b.hitObj(brick);
                        if (dir != null) {
                            brick.setShow(false);
                            bricksToDestroy--;
                        }
                        b.bounce(dir);
                    }
                }

            // update the display
            repaint();
        }
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        player.draw(g);
        for (Ball b : balls)
            b.draw(g);
        for (Brick[] row : bricks) {
            for (Brick brick : row) {
                brick.draw(g);
            }
        }
    }

    @Override
    public Dimension getPreferredSize() {
        return new Dimension(COURT_WIDTH, COURT_HEIGHT);
    }
}