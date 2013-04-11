import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.io.Serializable;
import java.util.ArrayList;

class Player implements Serializable {
    private String imageLocation = "images/shooter5.gif";

    private boolean isVisible;

    private int x;
    private int y;

    public boolean isMovingLeft;
    public boolean isMovingRight;
    public boolean isMovingUp;
    public boolean isMovingDown;
    public boolean isFiring;

    public int INITIAL_DELAY = 25;
    private int maximumWeaponLevel = 5;
    public int delay;

    private transient Image image;

    private ArrayList<Missile> missiles;
    private int missileType;
    private int missilesCount;

    public Player() {
        this.setImage(this.imageLocation);
        this.delay = INITIAL_DELAY;
        this.missiles = new ArrayList<Missile>();
        this.x = 70;
        this.y = 400;
        this.missilesCount = 1;
        this.setVisible(true);
    }

    public String getImageLocation() {
        return this.imageLocation;
    }

    public Rectangle getBounds() {
        return new Rectangle(this.getX(), this.getY(), this.image.getWidth(null), this.image.getHeight(null));
    }

    public void setVisible(boolean isVisible) {
        this.isVisible = isVisible;
    }

    public boolean isVisible() {
        return this.isVisible;
    }

    public void move(int maxRight, int maxBottom) {
        if (this.isMovingLeft && this.getX() > 0) {
            this.moveLeft();
        }

        if (this.isMovingRight && this.getX() < maxRight) {
            this.moveRight();
        }

        if (this.isMovingUp && this.getY() > 0) {
            this.moveUp();
        }

        if (this.isMovingDown && this.getY() < maxBottom) {
            this.moveDown();
        }
    }

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

    public Image getImage() {
        return image;
    }

    public void setImage(String imageLocation) {
        ImageIcon ii = new ImageIcon(imageLocation);
        this.image = ii.getImage();
    }

    public void moveLeft() {
        this.x--;
    }

    public void moveRight() {
        this.x++;
    }

    public void moveUp() {
        this.y--;
    }

    public void moveDown() {
        this.y += 2;
    }

    public ArrayList<Missile> getMissiles() {
        return missiles;
    }


    public void upgradeWeapon() {
        if (this.missilesCount < this.maximumWeaponLevel) {
            this.missilesCount++;
        }
    }

    public void fire() {
        if (this.isFiring && this.delay == 0) {
            int offset = this.image.getWidth(null) / 2 - (this.image.getWidth(null) / 8);
            switch (this.missilesCount) {
                case 1:
                    this.missiles.add(new Missile(x + offset, y, 0, 3));
                    break;
                case 2:
                    this.missiles.add(new Missile(x + offset - 10, y, 0, 3));
                    this.missiles.add(new Missile(x + offset + 10, y, 0, 3));
                    break;
                case 3:
                    this.missiles.add(new Missile(x + offset, y, 3, 3));
                    this.missiles.add(new Missile(x + offset, y, 0, 3));
                    this.missiles.add(new Missile(x + offset, y, -3, 3));
                    break;
                case 4:
                    this.missiles.add(new Missile(x + offset + 10, y, 3, 3));
                    this.missiles.add(new Missile(x + offset - 10, y, 0, 3));
                    this.missiles.add(new Missile(x + offset + 10, y, 0, 3));
                    this.missiles.add(new Missile(x + offset -10, y, -3, 3));
                    break;
                case 5:
                    this.missiles.add(new Missile(x + offset, y, 3, 3));
                    this.missiles.add(new Missile(x + offset, y, -2, 3));
                    this.missiles.add(new Missile(x + offset - 10, y, 0, 3));
                    this.missiles.add(new Missile(x + offset + 10, y, 0, 3));
                    this.missiles.add(new Missile(x + offset, y, 2, 3));
                    this.missiles.add(new Missile(x + offset, y, -3, 3));
                default:
                    break;
            }
//            if (this.missileType == 0) {
//                this.missiles.add(new Missile(x + offset, y, 3, 3));
//                this.missiles.add(new Missile(x + offset, y, 0, 3));
//                this.missiles.add(new Missile(x + offset, y, -3, 3));
//            }
//            else if (this.missileType == 1) {
//                this.missiles.add(new AnotherMissile(x + offset, y, 0, 3));
//            }
            this.delay = this.INITIAL_DELAY;
        }
    }

    public void decreaseShotDelay() {
        if (this.delay > 0) {
            this.delay--;
        }
    }

    public void keyPressed(KeyEvent e) {
        int key = e.getKeyCode();

        if (key == KeyEvent.VK_0) {
            this.missileType = 0;
        }

        if (key == KeyEvent.VK_1) {
            this.missileType = 1;
        }

        if (key == KeyEvent.VK_SPACE) {
            this.isFiring = true;
        }

        if (key == KeyEvent.VK_A) {
            this.isMovingLeft = true;
        }

        if (key == KeyEvent.VK_D) {
            this.isMovingRight = true;
        }

        if (key == KeyEvent.VK_W) {
            this.isMovingUp = true;
        }

        if (key == KeyEvent.VK_S) {
            this.isMovingDown = true;
        }
    }

    public void keyReleased(KeyEvent e) {
        int key = e.getKeyCode();

        if (key == KeyEvent.VK_SPACE) {
            this.isFiring = false;
        }

        if (key == KeyEvent.VK_A) {
            this.isMovingLeft = false;
        }

        if (key == KeyEvent.VK_D) {
            this.isMovingRight = false;
        }

        if (key == KeyEvent.VK_W) {
            this.isMovingUp = false;
        }

        if (key == KeyEvent.VK_S) {
            this.isMovingDown = false;
        }
    }

}

