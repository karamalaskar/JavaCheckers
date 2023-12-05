package JavaCheckers.logic;

import java.io.Serializable;

/**
 * Egy dáma bábut reprezentáló osztály.
 */
public class CheckersPiece implements Serializable {
    private boolean isKing;
    private pieceColor color;

    /**
     * A bábu színe.
     */
    public enum pieceColor {
        BLACK, WHITE
    }

    /**
     * Konstruktor, amely létrehozza a bábut.
     *
     * @param color A bábu színe.
     */
    public CheckersPiece(pieceColor color) {
        this.color = color;
        this.isKing = false;
    }

    /**
     * Bábu dámává alakítása.
     */
    public void makeKing() {
        System.out.println("making piece a king");
        this.isKing = true;
    }

    /**
     * Bábu dámává alakításának lekérdezése.
     *
     * @return Igaz, ha a bábu dáma, hamis, ha nem.
     */
    public boolean isKing() {
        return this.isKing;
    }

    /**
     * Bábu színének lekérdezése.
     *
     * @return A bábu színe.
     */
    public pieceColor getColor() {
        return this.color;
    }

    /**
     * Bábu lekérdezése.
     *
     * @return A bábu.
     */
    public CheckersPiece getPiece() {
        return this;
    }

}
