package JavaCheckersTests;

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;
import java.io.*;

import JavaCheckers.utils.SaveGame;
import JavaCheckers.logic.GameController;

/**
 * A játékállás mentésének és betöltésének tesztelésére szolgáló osztály.
 */
public class SaveGameTest {
    private SaveGame saveGame;
    private GameController gameController;

    /**
     * A tesztelés inicializálása.
     */
    @Before
    public void setUp() {
        saveGame = new SaveGame();
        gameController = new GameController();
    }

    /**
     * Nem létező fájl betöltésének tesztelése.
     */
    @Test
    public void testLoadNonExistentFile() {
        GameController loadedGame = saveGame.loadGameState("nonexistentFile.ser");
        assertNull(loadedGame);
    }

    /**
     * Több játékállás mentése és betöltése.
     */
    @Test
    public void testSaveAndLoadMultipleGames() {
        GameController game1 = new GameController();
        GameController game2 = new GameController();

        saveGame.saveGameState("game1.ser", game1);
        saveGame.saveGameState("game2.ser", game2);

        GameController loadedGame1 = saveGame.loadGameState("game1.ser");
        GameController loadedGame2 = saveGame.loadGameState("game2.ser");

        assertNotNull(loadedGame1);
        assertNotNull(loadedGame2);

        File file1 = new File("game1.ser");
        File file2 = new File("game2.ser");
        file1.delete();
        file2.delete();

    }

    /**
     * Játékállás mentése és betöltése.
     */
    @Test
    public void testSaveAndLoadWithModifiedGame() {
        gameController.makeMove(0, 0, 1, 1);
        saveGame.saveGameState("modifiedGame.ser", gameController);
        GameController loadedModifiedGame = saveGame.loadGameState("modifiedGame.ser");
        assertNotNull(loadedModifiedGame);
        File file = new File("modifiedGame.ser");
        file.delete();
    }

}
