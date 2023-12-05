package JavaCheckersTests;

import JavaCheckers.logic.CheckersBoard;

import org.junit.Before;
import org.junit.Test;

import JavaCheckers.logic.CheckersPiece;

import static org.junit.Assert.*;

/**
 * A tábla tesztelésére szolgáló osztály.
 */
public class CheckersBoardTest {
    private CheckersBoard checkersBoard;

    /**
     * A tábla inicializálása.
     */
    @Before
    public void setUp() {
        checkersBoard = new CheckersBoard();
        checkersBoard.initializeBoard();
    }

    /**
     * A tábla inicializálásának tesztelése.
     */
    @Test
    public void testSetPiece() {
        CheckersPiece piece = new CheckersPiece(CheckersPiece.pieceColor.WHITE);
        checkersBoard.setPiece(2, 2, piece);
        assertEquals(piece, checkersBoard.getPiece(2, 2));
    }

    /**
     * A tábla egy adott sorának és oszlopának törlésének tesztelése.
     */
    @Test
    public void testRemovePiece() {
        checkersBoard.removePiece(3, 3);
        assertNull(checkersBoard.getPiece(3, 3));
    }

}
