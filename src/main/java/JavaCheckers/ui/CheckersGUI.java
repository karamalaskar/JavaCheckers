package JavaCheckers.ui;

import JavaCheckers.logic.*;
import JavaCheckers.utils.*;
import JavaCheckers.logic.CheckersPiece.pieceColor;

import javax.swing.*;
import javax.swing.event.MouseInputAdapter;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;

public class CheckersGUI extends JFrame {
    private GameController controller;
    private CheckersBoard board;
    private JButton[][] buttons;
    private SaveGame savedGame;

    /**
     * Konstruktor, amely létrehozza a játék grafikus felületét.
     *
     * @param loadGame Igaz, ha folytatjuk az előző játékot, hamis, ha nem.
     */
    public CheckersGUI(boolean loadGame) {

        initializeComponents();
        createBoard();
        createMenuBar();
        updateBoard();
        finalizeLayout();
        if (loadGame) {
            loadSavedGame("savedGame.ser");
        }
        DragAndDropHandler dragAndDropHandler = new DragAndDropHandler();
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                buttons[i][j].addMouseListener(dragAndDropHandler);
            }
        }
    }

    /*
     * A játék grafikus felületének komponenseinek inicializálása.
     */

    private void initializeComponents() {
        controller = new GameController();
        board = new CheckersBoard();
        buttons = new JButton[8][8];

        setLayout(new GridLayout(8, 8));
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setSize(600, 600);

        setTitle("JavaCheckers ⚪️⚫️");
        setLocationRelativeTo(null);
    }

    private void createBoard() {
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                buttons[i][j] = createButton(i, j);
                add(buttons[i][j]);
            }
        }
    }

    private int startRow = -1;
    private int startCol = -1;

    /**
     * Gomb létrehozása, ami egy bábut reprezentál.
     * 
     * @param i sor
     * @param j oszlop
     * @return gomb
     */
    private JButton createButton(int i, int j) {
        JButton button = new JButton();
        button.setFont(new Font("Arial", Font.PLAIN, 30));
        button.setOpaque(true);
        button.setBorderPainted(false);
        button.setBackground((i + j) % 2 == 0 ? Color.WHITE : Color.BLACK);
        button.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.out.println("GUI clicked: " + i + ", " + j);
                if (startRow == -1 && startCol == -1) {
                    // First click, select the piece to move
                    startRow = i;
                    startCol = j;
                } else {
                    // Second click, make the move
                    try {
                        controller.makeMove(startRow, startCol, i, j);
                        startRow = -1;
                        startCol = -1;
                        board = controller.getBoard();
                        updateBoard();
                    } catch (IllegalArgumentException ex) {
                        System.out.println(ex.getMessage());
                        // Handle the exception (e.g., show an error message to the user)
                        startRow = -1;
                        startCol = -1;
                    }
                }
            }

        });

        return button;
    }

    /*
     * A játék grafikus felületének frissítése.
     */
    void updateBoard() {
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                updateButton(i, j);
            }
        }
        // check if game is over
        if (controller.isGameOver()) {
            System.out.println("Game over!");
            gameOver();
            // Handle the end of the game (e.g., show a message to the user)
        }
    }

    /**
     * Gomb frissítése, ami egy bábut reprezentál.
     * 
     * @param i sor
     * @param j oszlop
     */
    private void updateButton(int i, int j) {
        CheckersPiece piece = board.getPiece(i, j);
        if (piece != null) {
            buttons[i][j].setText(piece.getColor() == pieceColor.BLACK ? "\u26AB" : "\u26AA");
        } else {
            buttons[i][j].setText("");
        }
    }

    /*
     * A játék grafikus felületének véglegesítése.
     */
    private void finalizeLayout() {

        revalidate();
        repaint();
        setVisible(true);
        requestFocusInWindow();
    }

    /*
     * A játék végét kezelő metódus.
     */
    void gameOver() {
        JFrame frame = new JFrame();
        pieceColor winner = controller.getWinner();
        if (winner == pieceColor.BLACK || winner == pieceColor.WHITE) {
            int option = JOptionPane.showConfirmDialog(frame, winner + " Wins! Do you want to restart the game?",
                    "Game Over", JOptionPane.YES_NO_OPTION);
            if (option == JOptionPane.YES_OPTION) {
                restartGame();
            } else {
                System.exit(0);
            }
        } else {
            JOptionPane.showMessageDialog(frame, "It's a draw!");
        }
    }

    /*
     * A játék újraindítását kezelő metódus.
     */
    private void restartGame() {
        controller = new GameController();
        board = controller.getBoard();
        updateBoard();
    }

    private void loadSavedGame(String fileName) {
        savedGame = new SaveGame();
        GameController loadedGame = savedGame.loadGameState(fileName);

        if (loadedGame != null) {
            controller = loadedGame;
            board = controller.getBoard();
            updateBoard();
        }
    }

    /**
     * Egérkattintások kezelésére szolgáló osztály.
     */
    class DragAndDropHandler extends MouseInputAdapter {
        private int startRow = -1;
        private int startCol = -1;

        @Override
        public void mousePressed(MouseEvent e) {
            JButton source = (JButton) e.getSource();
            Point point = SwingUtilities.convertPoint(source, e.getPoint(), source.getParent());
            int row = point.y / source.getHeight();
            int col = point.x / source.getWidth();

            if (startRow == -1 && startCol == -1) {
                // First click, select the piece to move
                startRow = row;
                startCol = col;
            }
        }

        @Override
        public void mouseReleased(MouseEvent e) {
            JButton source = (JButton) e.getSource();
            Point point = SwingUtilities.convertPoint(source, e.getPoint(), source.getParent());
            int row = point.y / source.getHeight();
            int col = point.x / source.getWidth();

            if (startRow != -1 && startCol != -1) {
                // Second click, make the move
                try {
                    controller.makeMove(startRow, startCol, row, col);
                    startRow = -1;
                    startCol = -1;
                    board = controller.getBoard();
                    updateBoard();
                } catch (IllegalArgumentException ex) {
                    System.out.println(ex.getMessage());
                    // Handle the exception (e.g., show an error message to the user)
                    startRow = -1;
                    startCol = -1;
                }
            }
        }
    }

    /*
     * A játék statisztikáját megjelenítő metódus.
     */
    private void viewStats() {
        JFrame statsFrame = new JFrame("Statistics");
        statsFrame.setSize(300, 150);
        statsFrame.setLocationRelativeTo(this);

        // Create column names
        String[] columnNames = { "Player", "Score" };

        // Create data
        Object[][] data = {
                { "Black", controller.getBlackScore() },
                { "White", controller.getWhiteScore() }
        };

        // Create table
        JTable table = new JTable(data, columnNames);

        // Add table to a scroll pane
        JScrollPane scrollPane = new JScrollPane(table);

        // Add scroll pane to the frame
        statsFrame.add(scrollPane, BorderLayout.CENTER);

        statsFrame.setVisible(true);
    }

    /*
     * Menüsor létrehozása.
     */
    private void createMenuBar() {
        JMenuBar menuBar = new JMenuBar();

        JMenu menuButton = new JMenu("Menu");

        JMenuItem newGameMenuItem = new JMenuItem("New Game");

        newGameMenuItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new CheckersGUI(false);
                dispose();
            }
        });

        JMenuItem loadGameMenuItem = new JMenuItem("Load Game");
        loadGameMenuItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new CheckersGUI(true);
                dispose();
            }
        });

        JMenuItem viewStatsMenuItem = new JMenuItem("View Stats");
        viewStatsMenuItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                viewStats();
            }
        });

        JMenuItem quitMenuItem = new JMenuItem("Quit");
        quitMenuItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.exit(0);
            }
        });

        menuButton.add(newGameMenuItem);
        menuButton.add(loadGameMenuItem);
        menuButton.add(viewStatsMenuItem);
        menuButton.add(quitMenuItem);

        menuBar.add(menuButton);

        setJMenuBar(menuBar);
    }

}