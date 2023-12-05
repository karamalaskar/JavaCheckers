package JavaCheckersTests;

import JavaCheckers.logic.GameController;

import org.junit.Before;
import org.junit.Test;

import JavaCheckers.logic.CheckersPiece;

import static org.junit.Assert.*;

/**
 * A játékvezérlő tesztelésére szolgáló osztály.
 */

public class GameControllerTest {
    private GameController gameController;

    /**
     * A játékvezérlő inicializálása.
     */
    @Before
    public void setUp() {
        gameController = new GameController();
    }

    /**
     * A játékvezérlő konstruktorának tesztelése.
     */
    @Test
    public void testIsValidMove() {
        assertFalse(gameController.isValidMove(0, 0, 3, 3));
    }

    /**
     * A játékvezérlő táblahatárok ellenőrzésének tesztelése.
     */
    @Test
    public void testIsWithinBoard() {
        assertTrue(gameController.isWithinBoard(0, 0));
        assertFalse(gameController.isWithinBoard(8, 8));
    }

    /**
     * Ütés tesztelése.
     */
    @Test
    public void testIsValidCapture() {
        assertFalse(gameController.isValidCapture(0, 0, 2, 2));
    }

    /**
     * Ugrás tesztelése.
     */
    @Test
    public void testIsJump() {
        assertFalse(gameController.isJump(0, 0, 2, 2));
    }

    /**
     * Tud-e ugrani tesztelése.
     */
    @Test
    public void testCanJump() {
        assertFalse(gameController.canJump(0, 0));

    }

    @Test
    public void testIsGameOver() {
        assertFalse(gameController.isGameOver());
    }

    @Test
    public void testGetTurn() {
        assertEquals(CheckersPiece.pieceColor.BLACK, gameController.getTurn());
    }

    @Test
    public void testNextTurn() {
        gameController.nextTurn();
        assertEquals(CheckersPiece.pieceColor.WHITE, gameController.getTurn());
    }

    @Test
    public void testUpdateScores() {
        gameController.updateScores();
        assertEquals(1200, gameController.getBlackScore());
        assertEquals(1200, gameController.getWhiteScore());

    }

}
