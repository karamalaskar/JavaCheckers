package JavaCheckersTests;

import org.junit.Test;

import JavaCheckers.logic.CheckersPiece;

import static org.junit.Assert.*;

/**
 * A bábu tesztelésére szolgáló osztály.
 */
public class CheckersPieceTest {

    /**
     * A bábu konstruktorának tesztelése.
     */
    @Test
    public void testConstructor() {
        CheckersPiece blackPiece = new CheckersPiece(CheckersPiece.pieceColor.BLACK);
        assertEquals(CheckersPiece.pieceColor.BLACK, blackPiece.getColor());
        assertFalse(blackPiece.isKing());

        CheckersPiece whitePiece = new CheckersPiece(CheckersPiece.pieceColor.WHITE);
        assertEquals(CheckersPiece.pieceColor.WHITE, whitePiece.getColor());
        assertFalse(whitePiece.isKing());
    }

    /**
     * A bábu dámává alakításának tesztelése.
     */
    @Test
    public void testMakeKing() {
        CheckersPiece piece = new CheckersPiece(CheckersPiece.pieceColor.BLACK);
        assertFalse(piece.isKing());

        piece.makeKing();
        assertTrue(piece.isKing());
    }

    /**
     * A bábu dámává alakításának tesztelése.
     */
    @Test
    public void testIsKing() {
        CheckersPiece piece = new CheckersPiece(CheckersPiece.pieceColor.WHITE);
        assertFalse(piece.isKing());

        piece.makeKing();
        assertTrue(piece.isKing());
    }

    /**
     * A bábu színének tesztelése.
     */
    @Test
    public void testGetColor() {
        CheckersPiece blackPiece = new CheckersPiece(CheckersPiece.pieceColor.BLACK);
        assertEquals(CheckersPiece.pieceColor.BLACK, blackPiece.getColor());

        CheckersPiece whitePiece = new CheckersPiece(CheckersPiece.pieceColor.WHITE);
        assertEquals(CheckersPiece.pieceColor.WHITE, whitePiece.getColor());
    }

    /**
     * A bábu lekérdezésének tesztelése.
     */
    @Test
    public void testGetPiece() {
        CheckersPiece piece = new CheckersPiece(CheckersPiece.pieceColor.BLACK);
        assertEquals(piece, piece.getPiece());
    }

}
