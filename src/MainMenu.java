import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class MainMenu extends JPanel {

    private JButton btnExit;
    private JButton btnNewGame;
    private JButton btnResumeGame;
    private JButton btnLoadGame;
    private JButton btnSaveGame;
    private GameField gamePanel;
    private JFrame parent;

    private  int width;
    private  int height;

    private Image background;
    private String backgroundLocation;

    MainMenu(int width, int height, JFrame parent) {
//        super.setLayout();
        this.btnExit = new JButton("Exit");
        this.btnNewGame = new JButton("New Game");
        this.btnLoadGame = new JButton("Load Game");
        this.btnSaveGame = new JButton("Save Game");
        this.btnResumeGame = new JButton("Resume");
        this.btnSaveGame.setEnabled(false);

        this.parent = parent;
        this.gamePanel = new GameField(width, height, this, parent);
        this.gamePanel.setGameRunning(false);

        this.setBackground(Color.black);
        this.setFocusable(true);

        this.btnResumeGame.addActionListener(new resumeGameListener());
        this.btnNewGame.addActionListener(new newGameListener());
        this.btnLoadGame.addActionListener(new loadGameListener());
        this.btnSaveGame.addActionListener(new saveGameListener());
        this.btnExit.addActionListener(new exitListener());

        this.add(this.btnNewGame);
        this.add(this.btnLoadGame);
        this.add(this.btnSaveGame);
        this.add(this.btnExit);

        this.width = width;
        this.height = height;

        this.backgroundLocation = "images/menu_background.png";
        ImageIcon ii = new ImageIcon(this.backgroundLocation);
        this.background = ii.getImage();

        this.setVisible(true);

    }


    public void restore() {
        parent.remove(this.gamePanel);
        if (this.gamePanel.isGameOver()) {
           this.btnSaveGame.setEnabled(false);
        }
        else {
           this.btnSaveGame.setEnabled(true);
        }

        this.add(this.btnResumeGame);
        this.parent.add(this);
        this.parent.revalidate();
        this.parent.repaint();
    }

    private class saveGameListener implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            MainMenu.this.gamePanel.saveGame();
        }
    }

    private class loadGameListener implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            MainMenu.this.gamePanel.loadGame();
            if (MainMenu.this.gamePanel.isGameStarted()) {
            }
            else {
                MainMenu.this.gamePanel.loadGame();
            }
            MainMenu.this.gamePanel.setIsGameOver(false);
            MainMenu.this.parent.remove(MainMenu.this);
            MainMenu.this.parent.add(MainMenu.this.gamePanel);
            MainMenu.this.gamePanel.setGameRunning(true);
            MainMenu.this.gamePanel.requestFocus();
        }
    }

    private class resumeGameListener implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            MainMenu.this.gamePanel.setGameRunning(true);
            parent.remove(MainMenu.this);
            parent.add(gamePanel);

            gamePanel.setGameRunning(true);
            gamePanel.requestFocus();
        }
    }

    private class exitListener implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent arg0) {
            System.exit(0);
        }
    }

    private class newGameListener implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent arg0) {
            MainMenu.this.gamePanel.setIsGameOver(false);
            MainMenu.this.parent.remove(MainMenu.this);
            MainMenu.this.gamePanel = new GameField(width, height, MainMenu.this, MainMenu.this.parent);
            MainMenu.this.gamePanel.setGameRunning(true);
            MainMenu.this.parent.add(gamePanel);

            MainMenu.this.gamePanel.setGameRunning(true);
            MainMenu.this.gamePanel.requestFocus();

        }
    }
}
