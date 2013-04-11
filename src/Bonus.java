import javax.swing.*;
import java.awt.*;

public class Bonus {
    private int x;
    private int y;
    private String imageLocation = "images/moreShotsBonus.png";
    private Image image;

    private boolean isVisble;

    public Bonus(int x, int y) {
        this.x = x;
        this.y = y;
        this.setImage(this.imageLocation);
        this.setVisible(true);
    }

    public int getX() {
        return this.x;
    }

    public int getY() {
        return this.y;
    }

    public Image getImage() {
        return this.image;
    }

    public void setImage(String imageLocation) {
        this.imageLocation = imageLocation;
        ImageIcon ii = new ImageIcon(this.imageLocation);
        this.image = ii.getImage();
    }

    public String getImageLocation() {
        return this.imageLocation;
    }

    public Rectangle getBounds() {
        return new Rectangle(this.getX(), this.getY(), this.image.getWidth(null), this.image.getHeight(null));
    }

    public void setVisible(boolean visible) {
        this.isVisble = visible;
    }

    public boolean isVisible() {
        return this.isVisble;
    }

    public void moveDown() {
        this.y++;
    }
}
