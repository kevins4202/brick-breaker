package org.cis1200.brickbreaker;

import java.awt.*;

public class SlidingBrick extends Brick {
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
    public SlidingBrick(int courtWidth, int courtHeight, Color color, int posX, int posY, int vx) {
        super(courtWidth, courtHeight, color, posX, posY);
        super.setVx(vx);
    }

    public void move() {
        if (!super.getShow()) {
            return;
        }
        int currX = super.getPx();
        if (currX + super.getVx() >= super.getMaxX() ||
                currX + super.getVx() <= 0) {
            super.setVx(-super.getVx());
        }
        super.setPx(currX + super.getVx());
    }
}
