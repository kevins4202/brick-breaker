package org.cis1200.brickbreaker;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;

/**
 * GameCourt
 * This class holds the primary game logic for how different objects interact
 * with one another. Take time to understand how the timer interacts with the
 * different methods and how it repaints the GUI on every tick().
 */
public class GameCourt extends JPanel {

    // the state of the game logic
    private Brick[][] bricks;
    private Player player;
    private final ArrayList<Ball> balls = new ArrayList<>();

    private GameState playing = GameState.STOPPED; // whether the game is running
    private final JLabel status; // Current status text, i.e. "Running..."

    Timer timer;

    private int bricksToDestroy = -1;

    // Game constants
    public static final int COURT_WIDTH = 600;
    public static final int COURT_HEIGHT = 400;
    public static final int PLAYER_VELOCITY = 10;
    public static final int ROWS = 5;

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
        timer = new Timer(INTERVAL, e -> tick());

        setFocusable(true);
        requestFocusInWindow();

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
        // reset();
    }

    public GameState getPlaying() {
        return playing;
    }

    public void setPlaying(GameState playing) {
        this.playing = playing;
    }

    /**
     * (Re-)set the game to its initial state.
     */
    public void reset(String saved) {
        playing = GameState.PLAYING;

        bricksToDestroy = ROWS * (COURT_WIDTH / Brick.WIDTH - 4) + 3;
        bricks = new Brick[ROWS + 2][COURT_WIDTH / Brick.WIDTH];

        System.out.println("cleaer balls");
        balls.clear();

        if (saved != null) {
            System.out.println("load");
            loadGame(saved);
        } else {
            for (int i = 0; i < bricks.length; i++) {
                for (int j = 0; j < bricks[0].length; j++) {
                    bricks[i][j] = new StandardBrick(
                            COURT_WIDTH, COURT_HEIGHT,
                            new Color(
                                    (int) (Math.random() * 150), (int) (Math.random() * 150),
                                    (int) (Math.random() * 150) + 100
                            ),
                            j * Brick.WIDTH, i * Brick.HEIGHT + 30
                    );

                    if (j >= bricks[0].length / 2 - 2 && j <= bricks[0].length / 2 + 1) {
                        bricks[i][j].setShow(false);
                    }

                    if (Math.random() * 5 > 4.5) {
                        bricks[i][j].setSpawn(true);
                        bricks[i][j].setColor(new Color(210, 210, 0));
                    }
                }
            }

            for (int i = 1; i < bricks[0].length; i++) {
                bricks[0][i].setShow(false);
                bricks[ROWS + 1][i].setShow(false);
            }

            bricks[0][0] = new SlidingBrick(
                    COURT_WIDTH, COURT_HEIGHT,
                    new Color(
                            (int) (Math.random() * 150), (int) (Math.random() * 150),
                            (int) (Math.random() * 150) + 100
                    ),
                    0, 30, 4
            );
            bricks[ROWS + 1][0] = new SlidingBrick(
                    COURT_WIDTH, COURT_HEIGHT,
                    new Color(
                            (int) (Math.random() * 150), (int) (Math.random() * 150),
                            (int) (Math.random() * 150) + 100
                    ),
                    0, 30 + (ROWS + 1) * Brick.HEIGHT, 4
            );

            bricks[0][1] = new RotatingBrick(
                    COURT_WIDTH, COURT_HEIGHT,
                    new Color(
                            (int) (Math.random() * 150), (int) (Math.random() * 150),
                            (int) (Math.random() * 150) + 100
                    ),
                    270, 50, 90
            );

            balls.add(new Ball(COURT_WIDTH, COURT_HEIGHT, Color.YELLOW, 20, 5));
            balls.add(new Ball(COURT_WIDTH, COURT_HEIGHT, Color.YELLOW, 570, -6));
            System.out.println("reset add balls");
            player = new Player(COURT_WIDTH, COURT_HEIGHT, Color.BLACK);
        }

        status.setText("Destroy blocks. Yellow blocks generate a new ball upon destruction.");
        // Make sure that this component has the keyboard focus
        requestFocusInWindow();
        // timer = new Timer(INTERVAL, e -> tick());
        tick();
        playing = GameState.RESET;
    }

    public void startGame() {
        playing = GameState.PLAYING;
        timer.start();
        status.setText("Game Running");
    }

    public void saveGame() {
        /*
         * Bricks: color, show, spawn
         * Player: position
         * balls: pos, speed
         */

        int randomNumber;
        File file;

        do {
            // Generate a random number between 10000 and 99999
            randomNumber = 10000 + (int) (Math.random() * 90000);
            file = new File(
                    "src/main/java/org/cis1200/brickbreaker/saves/" + randomNumber + ".txt"
            );
        } while (file.exists()); // Repeat until a unique file name is found

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(file))) {
            // Save Bricks
            writer.write("Bricks\n");
            for (Brick[] row : bricks) {
                for (Brick brick : row) {
                    writer.write(
                            brick.getColor().getRed() + "," + brick.getColor().getGreen() + ","
                                    + brick.getColor().getBlue() + "," + brick.getShow() + ","
                                    + brick.getSpawn() + "," + brick.getVx() + " "
                    );
                }
                writer.newLine();
            }
            writer.write(
                    bricks[0][1].getColor().getRed() + "," + bricks[0][1].getColor().getGreen()
                            + "," + bricks[0][1].getColor().getBlue() + "," + bricks[0][1].getShow()
                            + "," + bricks[0][1].getSpawn() + ","
                            + ((RotatingBrick) bricks[0][1]).getAngle() + "\n"
            );

            // Save Player
            writer.write("Player\n");
            writer.write(player.getPx() + "\n");

            // Save Balls
            writer.write("Balls\n");
            for (Ball ball : balls) {
                writer.write(
                        ball.getPx() + "," + ball.getPy() + "," + ball.getVx() + "," + ball.getVy()
                                + " "
                );
            }
            writer.newLine();
            writer.write(bricksToDestroy + "\n");
        } catch (IOException e) {
            System.err.println("Error writing to file: " + e.getMessage());
        }

        status.setText("Game Saved, Save Number: " + randomNumber);
    }

    public void loadGame(String fileName) {
        File file = new File(fileName);
        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            if ("Bricks".equals(reader.readLine())) {
                String line;
                int i = 0;
                while (i < 7 && !(line = reader.readLine()).equals("Player")) {
                    // System.out.println(line);
                    String[] bricksData = line.strip().split(" ");
                    int j = 0;
                    for (String brickData : bricksData) {
                        String[] parts = brickData.split(",");
                        System.out.println(Arrays.toString(parts));
                        Color c = new Color(
                                Integer.parseInt(parts[0]), Integer.parseInt(parts[1]),
                                Integer.parseInt(parts[2])
                        );
                        // System.out.println(c);

                        if (j == 0 && (i == 0 || i == ROWS + 1)) {
                            bricks[i][j] = new SlidingBrick(
                                    COURT_WIDTH, COURT_HEIGHT, c, 0, i * Brick.HEIGHT + 30,
                                    Integer.parseInt(parts[5])
                            );
                        } else {
                            bricks[i][j] = new StandardBrick(
                                    COURT_WIDTH, COURT_HEIGHT, c, j * Brick.WIDTH,
                                    i * Brick.HEIGHT + 30
                            );
                        }

                        bricks[i][j].setShow(Boolean.parseBoolean(parts[3]));
                        bricks[i][j].setSpawn(Boolean.parseBoolean(parts[4]));
                        bricks[i][j].setVx(Integer.parseInt(parts[5]));
                        j++;
                    }
                    i++;
                }

                line = reader.readLine();
                String[] parts = line.split(",");
                // System.out.println(Arrays.toString(parts));
                Color c = new Color(
                        Integer.parseInt(parts[0]), Integer.parseInt(parts[1]),
                        Integer.parseInt(parts[2])
                );
                bricks[0][1] = new RotatingBrick(
                        COURT_WIDTH, COURT_HEIGHT, c, 270, 50, Double.parseDouble(parts[5])
                );
                bricks[0][1].setShow(Boolean.parseBoolean(parts[3]));
                bricks[0][1].setSpawn(Boolean.parseBoolean(parts[4]));
            }

            // System.out.println("Decoding Player:");
            reader.readLine();
            // System.out.println("Player: Px=" + playerData[0] + ", Py=" + playerData[1]);
            player.setPx(Integer.parseInt(reader.readLine().strip()));
            // System.out.println("Decoding Balls:");
            if ("Balls".equals(reader.readLine())) {
                String[] ballsData = reader.readLine().split(" ");
                for (String ballData : ballsData) {
                    String[] parts = ballData.split(",");
                    // System.out.println("Ball: Px=" + parts[0] + ", Py=" + parts[1] + ", Vx=" +
                    // parts[2] + ", Vy=" + parts[3]);
                    Ball ball = new Ball(
                            COURT_WIDTH, COURT_HEIGHT, Color.YELLOW, Integer.parseInt(parts[0]),
                            Integer.parseInt(parts[2])
                    );
                    ball.setPy(Integer.parseInt(parts[1]));
                    ball.setVy(Integer.parseInt(parts[3]));
                    balls.add(ball);
                    System.out.println("add ball");
                }
            }

            String line = reader.readLine();
            bricksToDestroy = Integer.parseInt(line);
        } catch (IOException e) {
            System.err.println("Error reading the file: " + e.getMessage());
        } catch (RuntimeException e) {
            JOptionPane.showMessageDialog(
                    null,
                    "Error reading the file",
                    "Could not read the file",
                    JOptionPane.ERROR_MESSAGE
            );
            reset(null);
        }
    }

    public int getBricksToDestroy() {
        return bricksToDestroy;
    }

    /**
     * This method is called every time the timer defined in the constructor
     * triggers.
     */
    void tick() {
        if (playing.equals(GameState.PLAYING)) {
            requestFocusInWindow();

            // advance the square and snitch in their current direction.
            if (balls.isEmpty()) {
                playing = GameState.GAMEOVER;
                status.setText("Game Over");
            }

            if (bricksToDestroy == 0) {
                playing = GameState.GAMEOVER;
                status.setText("You Win!");
            }

            player.move();

            for (Ball b : balls) {
                b.move();
            }

            for (Brick[] row : bricks) {
                for (Brick b : row) {
                    b.move();
                }
            }

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

            for (Ball b : ballsToRemove) {
                balls.remove(b);
            }
            for (Ball b : balls) {
                b.bounce(b.hitPlayer(player));
            }
            for (int i = 0; i < balls.size(); i++) {
                for (int j = i + 1; j < balls.size(); j++) {
                    Ball b1 = balls.get(i);
                    Ball b2 = balls.get(j);

                    b1.bounce(b1.hitObj(b2));
                    b2.bounce(b2.hitObj(b1));
                }
            }

            LinkedList<Ball> ballsToAdd = new LinkedList<>();

            for (Ball b : balls) {
                for (Brick[] barr : bricks) {
                    for (Brick brick : barr) {
                        Direction dir = b.hitObj(brick);
                        if (dir != null) {
                            brick.setShow(false);
                            bricksToDestroy--;
                            System.out.println(bricksToDestroy);
                            if (brick.isSpawn()) {
                                ballsToAdd.add(
                                        new Ball(COURT_WIDTH, COURT_HEIGHT, Color.YELLOW, 50, 5)
                                );
                            }
                        }
                        b.bounce(dir);
                    }
                }
            }

            balls.addAll(ballsToAdd);
            if (!ballsToAdd.isEmpty()) {
                System.out.println("break add ball");
            }
            // update the display
            repaint();
        }
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        player.draw(g);
        for (Ball b : balls) {
            b.draw(g);
        }

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