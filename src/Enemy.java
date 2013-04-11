import javax.swing.*;
import java.awt.*;
import java.io.Serializable;
import java.util.ArrayList;

public class Enemy implements Serializable {
    private transient Image image;
    private String location = "images/enemy2.png";

    private int x;
    private int y;

    private boolean isVisible;
    private boolean isKilled;

//    private final int SHOT_DELAY = 50;
    private int currentShotDelay;

    private int value;

    public ArrayList<Missile> missiles = new ArrayList<Missile>();

    public Enemy(int x, int y) {
        ImageIcon ii = new ImageIcon(this.location);
        this.image = ii.getImage();
        this.x = x;
        this.y = y;

        this.isVisible = false;
        this.isKilled = false;

        this.currentShotDelay = 0;
        this.value = 10;
    }

    public int getValue() {
        return this.value;
    }

    public int getX() {
        return this.x;
    }

    public int getY() {
        return this.y;
    }

    public void moveDown() {
        if (y > Constants.FIELD_HEIGHT) {
            this.setVisible(false);
        }
        this.y += 2;
    }

    public Missile shoot() {
//        if (currentShotDelay == 0) {
            Missile missile = new Missile(this.getX() + this.image.getWidth(null) / 2, this.getY() + this.image.getHeight(null), 0, -3);
            missiles.add(missile);
//            currentShotDelay = 50;
            return missile  ;
//        }
//        else {
//            currentShotDelay--;
//            return null;
//        }
    }

//    public void shoot() {
//        Missile missile = new Missile(this.getX() + this.image.getWidth(null) / 2, this.getY() + this.image.getHeight(null), 0, -3);
//        missiles.add(missile);
//    }

    public int getCurrentShotDelay() {
        return this.currentShotDelay;
    }

    public void setCurrentShotDelay(int delay) {
        this.currentShotDelay = delay;
    }

    public Rectangle getBounds() {
//        Polygon p = new Polygon(new int[] { this.getY(), this.getX() + this.image.getWidth(null)}, new int[] { this.getY(), this.getY(), this.getY() + this.image.getHeight(null) }, 3);
        return new Rectangle(this.getX(), this.getY(), this.image.getWidth(null), this.image.getHeight(null));
    }

    public boolean isKilled() {
        return this.isKilled;
    }

    public void die() {
        this.isKilled = true;
    }

    public boolean isVisible() {
        if (this.y + this.image.getHeight(null) < 0 || this.y > Constants.FIELD_HEIGHT) {
            this.setVisible(false);
        }
        else {
            this.setVisible(true);
        }
        return this.isVisible;
    }

    public Image getImage() {
        return this.image;
    }

    public void setVisible(boolean isVisible) {
        this.isVisible = isVisible;
    }

    public String getImageLocation() {
        return this.location;
    }

    public void setImage(String location) {
        ImageIcon ii = new ImageIcon(location);
        this.image = ii.getImage();
    }
}
