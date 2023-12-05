package JavaCheckers.utils;

import JavaCheckers.logic.GameController;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;


public class SaveGame implements Serializable {

    // Serialize the game state to a file
    public void saveGameState(String fileName, GameController game) {
        try (ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(fileName))) {
            out.writeObject(game);
            System.out.println("Game state saved to " + fileName);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Deserialize the game state from a file
    public static GameController loadGameState(String fileName) {
        GameController loadedGame = null;
        try (ObjectInputStream in = new ObjectInputStream(new FileInputStream(fileName))) {
            loadedGame = (GameController) in.readObject();
            System.out.println("Game state loaded from " + fileName);
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        return loadedGame;
    }

}
