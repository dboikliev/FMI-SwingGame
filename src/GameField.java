import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.io.*;
import java.util.ArrayList;
import java.util.Random;

class GameField extends JPanel implements ActionListener {
    public static int fieldWidth = Constants.FIELD_WIDTH;
    public static int fieldHeight = Constants.FIELD_HEIGHT;

    private Timer timer;

    private boolean isGameRunning;
    private boolean isGameStarted;
    private boolean isGameOver;
    
    private ArrayList<Missile> enemyMissiles = new ArrayList<Missile>();
    private ArrayList<Bonus> bonuses = new ArrayList<Bonus>();
    private ArrayList<Enemy> enemies;
    private ArrayList<Enemy> visibleEnemies = new ArrayList<Enemy>();

    private int framesCount;
    private int score;

    private Player craft;

    private Image background;
    private int backgroundHeight;
    private int firstBackgroundY;
    private int secondBackgroundY;

    private long lastTimeFpsCounted;
    private long lasTimeBonusAppeared;
    private long lastTimeEnemiesShot;


    private MainMenu menu;
    private JFrame mainFrame;
    private JProgressBar healthBar;
    
    public GameField(int width, int height, MainMenu menu, JFrame mainFrame) {
        super.addKeyListener(new KeyListener());
        super.setFocusable(true);
        super.setBackground(Color.BLACK);
        super.setDoubleBuffered(true);
        super.setSize(width, height);
        this.menu = menu;
        this.healthBar = new JProgressBar(0, 100);
        super.add(healthBar);
        this.mainFrame = mainFrame;
        this.framesCount = 0;
        this.score = 0;

        this.isGameRunning = true;
        this.isGameStarted = true;
        this.isGameOver = false;

        this.craft = new Player();
        ImageIcon ii = new ImageIcon("images/spaceVertical.png");
        this.background = ii.getImage();

        this.backgroundHeight = this.background.getHeight(null);
        this.secondBackgroundY = -this.backgroundHeight;
        this.firstBackgroundY = 0;
        Random rand = new Random();
        this.enemies = generateEnemies();

        this.timer = new Timer(5, this);
        this.timer.start();
        
        this.lastTimeFpsCounted = System.currentTimeMillis();
        this.lastTimeEnemiesShot = System.currentTimeMillis();
    }

    private static ArrayList<Enemy> generateEnemies() {
        ArrayList<Enemy> enemies = new ArrayList<Enemy>();
        for (int i = 0; i < 10; i++) {
            enemies.add(new Enemy(rand.nextInt(GameField.fieldWidth), -(GameField.fieldHeight + (rand.nextInt() % GameField.fieldHeight))));
        }
        return enemies;
    }

    public void paint(Graphics g) {
        super.paint(g);
        Graphics2D g2d = (Graphics2D)g;
        this.drawBackground(g2d);
        this.drawPlayer(g2d);
        this.drawMissiles(g2d);
        this.drawBonuses(g2d);
        drawEnemies(g2d);
        g.dispose();
    }

    private void drawBonuses(Graphics2D g2d) {
        for (int i = 0; i < this.bonuses.size(); i++) {
            Bonus bonus = this.bonuses.get(i);
            if (bonus.isVisible()) {
                g2d.drawImage(bonus.getImage(), bonus.getX(), bonus.getY(), this);
            }
        }
    }

    private void drawEnemies(Graphics2D g2d) {
        for (int i = 0; i < this.enemies.size(); i++) {
            Enemy enemy = this.enemies.get(i);
            if (enemy.isVisible()) {
                g2d.drawImage(enemy.getImage(), enemy.getX(), enemy.getY(), this);
            }
         }
    }

    private void drawBackground(Graphics2D g2d) {
        g2d.drawImage(this.background, 0, this.firstBackgroundY, this);
        g2d.drawImage(this.background, 0, this.secondBackgroundY, this);
    }

    private void moveBackground() {
        this.firstBackgroundY = this.firstBackgroundY % this.backgroundHeight;
        this.secondBackgroundY = - this.backgroundHeight + this.firstBackgroundY + 1;
        this.firstBackgroundY++;
    }

    private void drawPlayer(Graphics2D g2d) {
        if (this.craft.isVisible()) {
            g2d.drawImage(this.craft.getImage(), craft.getX(), craft.getY(), this);
        }
    }

    private void drawMissiles(Graphics2D g2d) {
        ArrayList<Missile> ms = this.craft.getMissiles();

        for (int i = 0; i < ms.size(); i++ ) {
            Missile m = ms.get(i);
            g2d.drawImage(m.getImage(), m.getX(), m.getY(), this);
        }

        for (int i = 0; i < this.enemyMissiles.size(); i++) {
            Missile m = this.enemyMissiles.get(i);
            g2d.drawImage(m.getImage(), m.getX(), m.getY(), this);
        }
    }

    public void actionPerformed(ActionEvent e) {

        this.framesCount++;
        if (this.isGameRunning) {
            this.moveBackground();
            this.enemiesShoot();
            this.craft.decreaseShotDelay();
            this.craft.fire();
            this.craft.move(fieldWidth, fieldHeight - this.craft.getImage().getHeight(null));
            this.moveEnemyMissiles();
            this.moveMissiles();
            this.moveEnemies();
            this.moveBonuses();
            this.detectCollisions();

            super.repaint();
        }
        long currentTimeInMilliseconds = System.currentTimeMillis();
        if (currentTimeInMilliseconds - this.lastTimeFpsCounted >= 1000) {
            this.lastTimeFpsCounted = currentTimeInMilliseconds;
            this.mainFrame.setTitle(String.format("Alien Troubles (%d FPS)", this.framesCount));
            this.framesCount = 0;
        }
    }

    private void moveBonuses() {
        for (int i = 0; i < this.bonuses.size(); i++) {
            Bonus bonus = this.bonuses.get(i);
            bonus.moveDown();
        }
    }

    private static Random rand = new Random();

    private void enemiesShoot() {
        long currentTime = System.currentTimeMillis();
        if (this.visibleEnemies.size() > 0 && currentTime - this.lastTimeEnemiesShot > 500) {
            int randomIndex = rand.nextInt() % this.visibleEnemies.size();
            randomIndex = Math.abs(randomIndex);
            Enemy enemy = this.visibleEnemies.get(randomIndex);
            int shotDelay = enemy.getCurrentShotDelay();
            if (shotDelay == 0) {
                Missile enemyMissile = this.visibleEnemies.get(randomIndex).shoot();
                this.enemyMissiles.add(enemyMissile);
                enemy.setCurrentShotDelay(enemyMissile.delay);
            }
            else {
                enemy.setCurrentShotDelay(shotDelay - 1);
            }
            this.lastTimeEnemiesShot = currentTime;
        }
    }

    private void moveEnemies() {
        Random rand = new Random();
        for (int i = 0; i < this.enemies.size(); i++) {
            Enemy enemy = this.enemies.get(i);
            enemy.moveDown();
            if (enemy.isVisible() && !this.visibleEnemies.contains(enemy)) {
                this.visibleEnemies.add(enemy);
            }
            if (enemy.isKilled() || enemy.getY() > Constants.FIELD_HEIGHT) {
                enemy = new Enemy(rand.nextInt(GameField.fieldWidth), -(GameField.fieldHeight + (rand.nextInt() % GameField.fieldHeight)));

                    this.enemies.set(i, enemy);
            }
        }

        for (int i = 0; i < this.visibleEnemies.size(); i++) {
            if (this.visibleEnemies.get(i).isKilled() || !this.visibleEnemies.get(i).isVisible()) {
                this.visibleEnemies.remove(i);
            }
        }
    }

    /*
     * Every object in the game
     * is surrounded by a rectangle.
     * If two rectangles intersect
     * the objects contained inside
     * them collide.
     */
    private void detectCollisions() {
        for (int i = 0; i < this.enemyMissiles.size(); i++) {
            Missile missile = this.enemyMissiles.get(i);
            Rectangle missileBounds = missile.getBounds();
            Rectangle craftBounds = this.craft.getBounds();
            if (missileBounds.intersects(craftBounds)) {
                this.craft.setVisible(false);
                this.isGameRunning = false;
                this.isGameOver = true;
            }
        }

        ArrayList<Missile> missiles = this.craft.getMissiles();
        ArrayList<Rectangle> missilesBounds = new ArrayList<Rectangle>();
        for (int i = 0; i < missiles.size(); i++) {
            Missile missile = missiles.get(i);
            Rectangle bounds = missile.getBounds();
            missilesBounds.add(bounds);
        }

        ArrayList<Rectangle> enemiesBounds = new ArrayList<Rectangle>();
        for (int i = 0; i < this.enemies.size(); i++) {
            Enemy enemy = this.enemies.get(i);
            Rectangle bounds = enemy.getBounds();
            enemiesBounds.add(bounds);
        }

        for (int i = 0; i < enemiesBounds.size(); i++) {
            Rectangle enemyBounds = enemiesBounds.get(i);
            Rectangle craftBounds = this.craft.getBounds();
            if (enemyBounds.intersects(craftBounds)) {
                this.craft.setVisible(false);
                this.isGameRunning = false;
                this.isGameOver = true;
            }
        }

        for (int i = 0; i < missilesBounds.size(); i++) {
            Missile missile = missiles.get(i);
            Rectangle missileBounds = missilesBounds.get(i);

            for (int j = 0; j < enemiesBounds.size(); j++) {
                Enemy enemy = this.enemies.get(j);
                Rectangle enemyBounds = enemiesBounds.get(j);
                if (missileBounds.intersects(enemyBounds) && enemy.isVisible()) {
                    this.score += enemy.getValue();
                    System.out.println(score);
                    enemy.die();
                    long currentTime = System.currentTimeMillis();
                    if (currentTime - this.lasTimeBonusAppeared >= 10000) {
                        this.bonuses.add(new Bonus(enemy.getX(), enemy.getY()));
                        this.lasTimeBonusAppeared = currentTime;
                    }
                    missile.setVisible(false);
                }
            }
        }

        for (int i = 0; i < this.bonuses.size(); i++) {
            Bonus bonus = this.bonuses.get(i);
            Rectangle bonusBounds = bonus.getBounds();
            if (bonusBounds.intersects(this.craft.getBounds())) {
                bonus.setVisible(false);
                bonuses.remove(i);
                this.craft.upgradeWeapon();
            }
        }
    }

    private void moveMissiles() {
        ArrayList<Missile> missiles = this.craft.getMissiles();
        for (int i = 0; i < missiles.size(); i++) {
            Missile missile = missiles.get(i);
            if (missile.isVisible()) {
                missile.move();
            }
            else {
                missiles.remove(i);
            }
        }
    }

    private void moveEnemyMissiles() {
        for (int i = 0; i < this.enemyMissiles.size(); i++) {
            Missile missile = this.enemyMissiles.get(i);
            if (missile.isVisible()) {
                missile.move();
            }
            else {
                this.enemyMissiles.remove(i);
            }
        }

    }
    public void setGameRunning(boolean isGameRunning) {
        this.isGameRunning = isGameRunning;
    }


    public boolean isGameStarted() {
        return this.isGameStarted;
    }


    public void saveGame() {
        FileOutputStream stream = null;
        try {
            stream = new FileOutputStream("savegame.save");
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        try {
            ObjectOutputStream ous = new ObjectOutputStream(stream);
            SaveGameInfo info = new SaveGameInfo();
            info.craft = this.craft;
            info.visibleEnemies = this.visibleEnemies;
            info.enemyMissiles = this.enemyMissiles;
            info.enemies = this.enemies;
            ous.writeObject(info);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void loadGame() {
        FileInputStream stream = null;
        try {
            stream = new FileInputStream("savegame.save");
        }
        catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        try {
            ObjectInputStream ois = new ObjectInputStream(stream);
            try {
                SaveGameInfo info = (SaveGameInfo)ois.readObject();
                this.craft = info.craft;
                this.craft.setImage(info.craft.getImageLocation());
                for (int i = 0; i < this.craft.getMissiles().size(); i++) {
                    this.craft.getMissiles().get(i).setImage(info.craft.getMissiles().get(i).getImageLocation());
                }
                this.visibleEnemies = info.visibleEnemies;
                this.enemyMissiles = new ArrayList<Missile>(info.enemyMissiles.size());
                for (int i = 0; i < this.enemyMissiles.size(); i++) {
                    this.enemyMissiles.get(i).setImage(info.enemyMissiles.get(i).getImageLocation());
                }
                this.enemies = info.enemies;
                for (int i = 0; i < this.enemies.size(); i++) {
                    this.enemies.get(i).setImage(info.enemies.get(i).getImageLocation());
                }
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }

    public boolean isGameOver() {
        return this.isGameOver;
    }

    public void setIsGameOver(boolean isGameOver) {
        this.isGameOver = isGameOver;
    }


    private class KeyListener extends KeyAdapter {

        public void keyReleased(KeyEvent e) {
            craft.keyReleased(e);
        }

        public void keyPressed(KeyEvent e) {
            if (e.getKeyCode() == KeyEvent.VK_ESCAPE) {

                GameField.this.setGameRunning(false);
                GameField.this.craft.isFiring = false;
                menu.restore();
            }
            craft.keyPressed(e);
        }
    }
}