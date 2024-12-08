package org.cis1200.brickbreaker;

import java.awt.*;

public class RotatingBrick extends Brick {
    private double angle;

    /**
     * Note that, because we don't need to do anything special when constructing
     * a Square, we simply use the superclass constructor called with the
     * correct parameters.
     *
     * @param courtWidth
     * @param courtHeight
     * @param color
     * @param posX
     * @param posY
     */
    public RotatingBrick(
            int courtWidth, int courtHeight, Color color, int posX, int posY, double angle
    ) {
        super(courtWidth, courtHeight, color, posX, posY);
        this.angle = angle;
    }

    public void move() {
        if (!super.getShow())
            return;
        angle = (angle + 5) % 360;
        super.setPx(270 + (int) (50 * Math.cos(Math.toRadians(angle))));
        super.setPy(90 + (int) (50 * Math.sin(Math.toRadians(angle))));
    }

    public double getAngle() {
        return angle;
    }

    public void setAngle(double angle) {
        this.angle = angle;
    }
}
