package org.cis1200.brickbreaker;

import org.junit.jupiter.api.*;
import java.awt.*;

import static org.junit.jupiter.api.Assertions.*;

/**
 * You can use this file (and others) to test your
 * implementation.
 */

public class BrickBreakerTest {
    @Test
    public void createPlayer() {
        Player player = new Player(600, 600, Color.BLACK);

        // square should start at (0, 0)
        assertEquals(300, player.getPx());
        assertEquals(350, player.getPy());

        // square should not be moving at the start
        assertEquals(0, player.getVx());
        assertEquals(0, player.getVy());
    }

    @Test
    public void playerVelocityUpdatesPosition() {
        Player player = new Player(600, 600, Color.BLACK);

        // update velocity to non-zero value
        player.setVx(10);
        player.setVy(20);
        assertEquals(10, player.getVx());
        assertEquals(20, player.getVy());

        // position should not have updated yet since we didn't call move()
        assertEquals(300, player.getPx());
        assertEquals(350, player.getPy());

        // move!
        player.move();

        // square should've moved
        assertEquals(310, player.getPx());
        assertEquals(370, player.getPy());
    }

    @Test
    public void twoObjectIntersection() {
        Player player = new Player(200, 200, Color.white);
        assertEquals(300, player.getPx());
        assertEquals(350, player.getPy());

        Ball ball = new Ball(
                GameCourt.COURT_WIDTH, GameCourt.COURT_HEIGHT, Color.YELLOW, 0, 0
        );
        assertEquals(0, ball.getPx());
        assertEquals(325, ball.getPy());

        ball.setPy(100);
        assertEquals(100, ball.getPy());

        // they're very far apart, so they should not be intersecting
        assertFalse(player.intersects(ball));

        player.setPx(0);
        player.setPy(100);
        System.out.println(player.getPy());

        assertEquals(0, player.getVx());
        assertEquals(0, player.getVy());

        assertEquals(0, player.getPx());
        assertEquals(100, player.getPy());

        // now, they're on top of one another! they should intersect
        assertTrue(player.intersects(ball));
    }
}
