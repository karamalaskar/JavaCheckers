package JavaCheckers.logic;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Egy dáma táblát reprezentáló osztály.
 */

public class CheckersBoard implements Serializable {
    /**
     * A tábla egy 8x8-as mátrixként van reprezentálva,
     */
    private List<List<CheckersPiece>> board;

    /**
     * Konstruktor, amely létrehozza a táblát.
     */

    public CheckersBoard() {
        initializeBoard();
    }

    /**
     * Táblát inicializáló metódus.
     */

    public void initializeBoard() {
        board = new ArrayList<>(8);

        for (int i = 0; i < 8; i++) {
            List<CheckersPiece> row = new ArrayList<>(8);
            for (int j = 0; j < 8; j++) {
                if ((i + j) % 2 != 0) {
                    if (i < 3) {
                        row.add(new CheckersPiece(CheckersPiece.pieceColor.BLACK));
                    } else if (i > 4) {
                        row.add(new CheckersPiece(CheckersPiece.pieceColor.WHITE));
                    } else {
                        row.add(null);
                    }
                } else {
                    row.add(null);
                }
            }
            board.add(row);
        }
    }

    /**
     * A tábla egy adott sorának és oszlopának lekérdezése.
     *
     * @param row A sor indexe.
     * @param col Az oszlop indexe.
     * @return Az adott sor és oszlop által meghatározott mezőn lévő bábu.
     */
    public CheckersPiece getPiece(int row, int col) {
        if (isValidIndex(row, col)) {
            return board.get(row).get(col);
        }
        return null; // or throw an exception
    }

    /**
     * Bábu elhelyezése a táblán.
     * 
     * @param row A sor indexe.
     * @param col Az oszlop indexe.
     */
    public void setPiece(int row, int col, CheckersPiece piece) {
        if (isValidIndex(row, col)) {
            board.get(row).set(col, piece);
        }
    }

    /**
     * Bábu eltávolítása a tábláról.
     * 
     * @param row A sor indexe.
     * @param col Az oszlop indexe.
     */
    public void removePiece(int row, int col) {
        if (isValidIndex(row, col)) {
            board.get(row).set(col, null);
        }
    }

    /**
     * Megadja, hogy egy adott sor és oszlop indexe érvényes-e.
     * 
     * @param row
     * @param col
     * @return
     */
    private boolean isValidIndex(int row, int col) {
        return row >= 0 && row < 8 && col >= 0 && col < 8;
    }
}