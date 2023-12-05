package JavaCheckers.logic;

import javax.sound.sampled.*;
import JavaCheckers.utils.*;

import JavaCheckers.logic.CheckersPiece.pieceColor;

import java.io.*;

/**
 * A játékot irányító osztály.
 */
public class GameController implements Serializable {
    private CheckersBoard board;
    private pieceColor turn;

    private int blackScore;
    private int whiteScore;

    public GameController() {
        board = new CheckersBoard();
        turn = pieceColor.BLACK;
    }

    /**
     * Bábu léptetése a táblán.
     * 
     * @param startRow bábu kiinduló sor
     * @param startCol bábu kiinduló oszlop
     * @param endRow   bábu cél sor
     * @param endCol   bábu cél oszlop
     */
    public void makeMove(int startRow, int startCol, int endRow, int endCol) {
        CheckersPiece currentPiece = board.getPiece(startRow, startCol);

        if (!isValidMove(startRow, startCol, endRow, endCol)) {
            return;
        }

        boolean wasJump = isJump(startRow, startCol, endRow, endCol);
        if (wasJump) {
            board.removePiece((startRow + endRow) / 2, (startCol + endCol) / 2);
        }

        board.setPiece(endRow, endCol, currentPiece);
        board.removePiece(startRow, startCol);

        if (!currentPiece.isKing() && ((currentPiece.getColor() == pieceColor.BLACK && endRow == 7)
                || (currentPiece.getColor() == pieceColor.WHITE && endRow == 0))) {
            currentPiece.makeKing();
        }

        boolean jumpPossible = canJump(endRow, endCol);

        System.out.println("Move made: " + startRow + "," + startCol + " to " + endRow + "," + endCol + "");
        if (!wasJump || !jumpPossible) {
            nextTurn();
        } else {
            System.out.println("You must complete the jump before switching turns.");
        }
        playMoveSound();
        updateScores();
        SaveGame saveGame = new SaveGame();
        saveGame.saveGameState("savedGame.ser", this);

    }

    /**
     * Ellenőrzi, hogy a lépés érvényes-e.
     * 
     * @param startRow bábu kiinduló sor
     * @param startCol bábu kiinduló oszlop
     * @param endRow   bábu cél sor
     * @param endCol   bábu cél oszlop
     * @return érvényes-e a lépés
     */
    public boolean isValidMove(int startRow, int startCol, int endRow, int endCol) {
        CheckersPiece currentPiece = board.getPiece(startRow, startCol);
        int rowDifference = Math.abs(endRow - startRow);
        int colDifference = Math.abs(endCol - startCol);
        boolean captureAvailable = hasCaptureOption();

        // Check for general conditions
        if (currentPiece == null || currentPiece.getColor() != turn || endRow < 0 || endRow >= 8 || endCol < 0
                || endCol >= 8 || board.getPiece(endRow, endCol) != null || rowDifference > 2) {
            System.out.println("Invalid move: General conditions not met");
            return false;
        }

        // Check if capturing move is available
        if (captureAvailable) {
            // If capturing move is available, disallow non-capturing moves
            if (rowDifference != 2 || colDifference != 2) {
                System.out.println("Invalid move: Non-capturing move not allowed when a capture is available.");
                return false;
            }
        } else {
            // If no capturing move is available, regular move is allowed
            if (rowDifference != 1 || colDifference != 1) {
                System.out.println("Invalid move: Non-capturing move not allowed when a capture is available.");
                return false;
            }
        }

        // Check if a non-capturing move with distance > 1 is attempted
        if (rowDifference == 2 && !isValidCapture(startRow, startCol, endRow, endCol)) {
            System.out.println("Invalid move: Piece attempted a non-capturing move with distance > 1");
            return false;
        }

        // Check if a non-king piece attempted to move backward
        if (!currentPiece.isKing() && ((currentPiece.getColor() == pieceColor.BLACK && endRow < startRow)
                || (currentPiece.getColor() == pieceColor.WHITE && endRow > startRow))) {
            System.out.println("Invalid move: Non-king piece attempted to move backward");
            return false;
        }

        return true;
    }

    /**
     * Ellenőrzi, hogy a sor és oszlop indexek érvényesek-e.
     * 
     * @param row sor index
     * @param col oszlop index
     * @return érvényes-e a sor és oszlop index
     */
    public boolean isWithinBoard(int row, int col) {
        return row >= 0 && row < 8 && col >= 0 && col < 8;
    }

    public boolean isValidCapture(int startRow, int startCol, int endRow, int endCol) {
        CheckersPiece currentPiece = board.getPiece(startRow, startCol);
        CheckersPiece midPiece = board.getPiece((startRow + endRow) / 2, (startCol + endCol) / 2);

        if (!isWithinBoard(endRow, endCol)) {
            return false;
        }

        return !(board.getPiece(endRow, endCol) != null || midPiece == null
                || midPiece.getColor() == currentPiece.getColor());
    }

    /**
     * Ellenőrzi, hogy van-e ütési lehetőség.
     * 
     * @return van-e ütési lehetőség
     */
    public boolean hasCaptureOption() {
        // Iterate through the board to check if any pieces can make a capture
        for (int row = 0; row < 8; row++) {
            for (int col = 0; col < 8; col++) {
                CheckersPiece currentPiece = board.getPiece(row, col);
                if (currentPiece != null && currentPiece.getColor() == turn && canJump(row, col)) {
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * Ellenőrzi, az adott mozgás ugrás-e.
     * 
     * @param startRow bábu kiinduló sor
     * @param startCol bábu kiinduló oszlop
     * @param endRow   bábu cél sor
     * @param endCol   bábu cél oszlop
     * @return ugrás-e az adott mozgás
     */
    public boolean isJump(int startRow, int startCol, int endRow, int endCol) {
        int midRow = (startRow + endRow) / 2;
        int midCol = (startCol + endCol) / 2;

        return isWithinBoard(endRow, endCol)
                && isWithinBoard(midRow, midCol)
                && board.getPiece(startRow, startCol) != null
                && board.getPiece(midRow, midCol) != null
                && board.getPiece(endRow, endCol) == null
                && board.getPiece(midRow, midCol).getColor() != board.getPiece(startRow, startCol).getColor();
    }

    /**
     * Ellenőrzi, hogy az adott bábu tud-e ugrani.
     * 
     * @param row bábu sora
     * @param col bábu oszlopa
     * @return tud-e ugrani az adott bábu
     */
    public boolean canJump(int row, int col) {
        CheckersPiece currentPiece = board.getPiece(row, col);

        if (currentPiece != null) {
            if (currentPiece.isKing()) {
                return isJump(row, col, row + 2, col + 2)
                        || isJump(row, col, row + 2, col - 2)
                        || isJump(row, col, row - 2, col + 2)
                        || isJump(row, col, row - 2, col - 2);
            } else if (currentPiece.getColor() == pieceColor.BLACK) {
                return isJump(row, col, row + 2, col + 2) || isJump(row, col, row + 2, col - 2);
            } else if (currentPiece.getColor() == pieceColor.WHITE) {
                return isJump(row, col, row - 2, col + 2) || isJump(row, col, row - 2, col - 2);
            }
        }

        return false;
    }

    /**
     * Ellenőrzi, hogy vége van-e a játéknak.
     * 
     * @return vége van-e a játéknak
     */
    public boolean isGameOver() {
        boolean blackPiecesLeft = false;
        boolean redPiecesLeft = false;

        for (int row = 0; row < 8; row++) {
            for (int col = 0; col < 8; col++) {
                CheckersPiece currentPiece = board.getPiece(row, col);

                if (currentPiece != null) {
                    if (currentPiece.getColor() == pieceColor.BLACK) {
                        blackPiecesLeft = true;
                    }

                    if (currentPiece.getColor() == pieceColor.WHITE) {
                        redPiecesLeft = true;
                    }
                }
            }
        }

        return !blackPiecesLeft || !redPiecesLeft;
    }

    /**
     * Melyik szín következik.
     * 
     * @return Szín
     */
    public pieceColor getTurn() {
        return this.turn;
    }

    /**
     * Következő játékos.
     */
    public void nextTurn() {
        turn = (turn == pieceColor.BLACK) ? pieceColor.WHITE : pieceColor.BLACK;
        System.out.println("Turn switched to " + turn + "");
    }

    /**
     * Visszaadja a táblát.
     * 
     * @return A tábla.
     */
    public CheckersBoard getBoard() {
        return board;
    }

    /**
     * Visszaadja a győztest.
     * 
     * @return
     */
    public pieceColor getWinner() {
        if (isGameOver()) {
            if (turn == pieceColor.BLACK) {
                return pieceColor.WHITE;
            } else {
                return pieceColor.BLACK;
            }
        } else {
            return null;
        }
    }

    /**
     * Frissíti a pontszámokat.
     */
    public void updateScores() {
        blackScore = 0;
        whiteScore = 0;

        for (int row = 0; row < 8; row++) {
            for (int col = 0; col < 8; col++) {
                CheckersPiece currentPiece = board.getPiece(row, col);
                if (currentPiece != null) {
                    if (currentPiece.getColor() == pieceColor.BLACK) {
                        blackScore += 100;
                    } else if (currentPiece.getColor() == pieceColor.WHITE) {
                        whiteScore += 100;
                    }
                }
            }
        }
        System.out.println("Black score: " + blackScore + " White score: " + whiteScore + "");
    }

    /*
     * Visszaadja a fekete pontszámát.
     */
    public int getBlackScore() {
        return blackScore;
    }

    /**
     * Visszaadja a fehér pontszámát.
     */
    public int getWhiteScore() {
        return whiteScore;
    }

    /**
     * Lépés hang lejátszása.
     */
    public void playMoveSound() {
        try {

            InputStream soundStream = getClass().getClassLoader().getResourceAsStream("click.wav");
            if (soundStream != null) {
                AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(soundStream);
                Clip clip = AudioSystem.getClip();

                clip.addLineListener(event -> {
                    if (event.getType() == LineEvent.Type.STOP) {
                        clip.close();
                    }
                });

                clip.open(audioInputStream);
                clip.start();
            } else {
                System.err.println("Sound file not found: click.wav");
            }
        } catch (UnsupportedAudioFileException | IOException | LineUnavailableException e) {
            e.printStackTrace();
        }
    }
}