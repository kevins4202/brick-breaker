package org.cis1200.brickbreaker;

import java.awt.*;

/**
 * A basic game object starting in the upper left corner of the game court. It
 * is displayed as a square of a specified color.
 */
public class Player extends GameObj {
    public static final int HEIGHT = 10;
    public static final int WIDTH = 70;
    public static final int INIT_POS_X = 300;
    public static final int INIT_POS_Y = 350;
    public static final int INIT_VEL_X = 0;
    public static final int INIT_VEL_Y = 0;

    private final Color color;

    /**
     * Note that, because we don't need to do anything special when constructing
     * a Square, we simply use the superclass constructor called with the
     * correct parameters.
     */
    public Player(int courtWidth, int courtHeight, Color color) {
        super(
                INIT_VEL_X, INIT_VEL_Y, INIT_POS_X, INIT_POS_Y, WIDTH, HEIGHT, courtWidth,
                courtHeight
        );

        this.color = color;
    }

    @Override
    public void draw(Graphics g) {
        g.setColor(this.color);
        g.fillRect(this.getPx(), this.getPy(), this.getWidth(), this.getHeight());
    }
}