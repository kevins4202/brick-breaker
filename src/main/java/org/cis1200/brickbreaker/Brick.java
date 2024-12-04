package org.cis1200.brickbreaker;

import java.awt.*;

/**
 * A basic game object starting in the upper left corner of the game court. It
 * is displayed as a square of a specified color.
 */
public class Brick extends GameObj {
    public static final int HEIGHT = 20;
    public static final int WIDTH = 100;
    public final int INIT_POS_X;
    public final int INIT_POS_Y;
    public static final int INIT_VEL_X = 0;
    public static final int INIT_VEL_Y = 0;
    private boolean show = true;
    private boolean spawn = false;

    private Color color;

    /**
     * Note that, because we don't need to do anything special when constructing
     * a Square, we simply use the superclass constructor called with the
     * correct parameters.
     */
    public Brick(int courtWidth, int courtHeight, Color color, int posX, int posY) {
        super(INIT_VEL_X, INIT_VEL_Y, posX, posY, WIDTH, HEIGHT, courtWidth, courtHeight);

        this.INIT_POS_X = posX;
        this.INIT_POS_Y = posY;
        this.color = color;
    }

    @Override
    public void draw(Graphics g) {
        if (!show) {
            return;
        }
        g.setColor(this.color);
        g.fillRect(this.getPx(), this.getPy(), this.getWidth(), this.getHeight());
    }

    public void setShow(boolean show) {
        this.show = show;
    }

    public boolean getShow() {
        return show;
    }

    public boolean isSpawn() {
        return spawn;
    }

    public void setSpawn(boolean spawn) {
        this.spawn = spawn;
    }

    public boolean getSpawn() {
        return spawn;
    }

    public void setColor(Color color) {
        this.color = color;
    }

    public Color getColor() {
        return color;
    }
}