package org.cis1200.brickbreaker;

import java.awt.*;

/**
 * A basic game object starting in the upper left corner of the game court. It
 * is displayed as a circle of a specified color.
 */
public class Ball extends GameObj {
    public static final int SIZE = 20;
    private final int initPosX;
    private static final int INIT_POS_Y = 325;
    private final int initVelX;
    public static final int INIT_VEL_Y = -8;

    private final Color color;

    public Ball(int courtWidth, int courtHeight, Color color, int initPosX, int initVelX) {
        super(initVelX, INIT_VEL_Y, initPosX, INIT_POS_Y, SIZE, SIZE, courtWidth, courtHeight);
        this.initVelX = initVelX;
        this.initPosX = initPosX;
        this.color = color;
    }

    public int getInitPosX() {
        return initPosX;
    }

    public int getInitVelX() {
        return initVelX;
    }

    @Override
    public void draw(Graphics g) {
        g.setColor(this.color);
        g.fillOval(this.getPx(), this.getPy(), this.getWidth(), this.getHeight());
    }
}