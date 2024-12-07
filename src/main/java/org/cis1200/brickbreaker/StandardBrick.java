package org.cis1200.brickbreaker;

import java.awt.*;

public class StandardBrick extends Brick {
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
    public StandardBrick(int courtWidth, int courtHeight, Color color, int posX, int posY) {
        super(courtWidth, courtHeight, color, posX, posY);
    }
}
