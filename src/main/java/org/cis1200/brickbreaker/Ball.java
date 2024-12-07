package org.cis1200.brickbreaker;

import java.awt.*;

/**
 * A basic game object starting in the upper left corner of the game court. It
 * is displayed as a circle of a specified color.
 */
public class Ball extends GameObj {
    public static final int SIZE = 20;
    public final int INIT_POS_X;
    public static final int INIT_POS_Y = 325;
    public final int INIT_VEL_X;
    public static final int INIT_VEL_Y = -8;

    private final Color color;

    public Ball(int courtWidth, int courtHeight, Color color, int INIT_POS_X, int INIT_VEL_X) {
        super(INIT_VEL_X, INIT_VEL_Y, INIT_POS_X, INIT_POS_Y, SIZE, SIZE, courtWidth, courtHeight);
        this.INIT_VEL_X = INIT_VEL_X;
        this.INIT_POS_X = INIT_POS_X;
        this.color = color;
    }

    @Override
    public void draw(Graphics g) {
        g.setColor(this.color);
        g.fillOval(this.getPx(), this.getPy(), this.getWidth(), this.getHeight());
    }
}