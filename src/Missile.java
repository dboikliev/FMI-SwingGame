import javax.swing.*;
import java.awt.*;
import java.io.Serializable;

class Missile implements Serializable {

    private int x;
    private int y;
    private transient Image image;
    private String imageLocation;
    boolean isVisible;

    public int delay = 50;

    private double verticalSpeed ;
    private double horizontalSpeed;

    public Missile(int x, int y, int horizontalSpeed, int verticalSpeed) {
        this.imageLocation = "images/missile1.png";
        ImageIcon ii = new ImageIcon(this.imageLocation);
//        ImageIcon ii = new ImageIcon("C:\\Users\\Deyan\\Desktop\\Fireball.png");
        this.image = ii.getImage();

        this.horizontalSpeed = horizontalSpeed;
        this.verticalSpeed = verticalSpeed;

        this.isVisible = true;
        this.x = x;
        this.y = y;
    }

    public Missile(int x, int y, int horizontalSpeed, int verticalSpeed, int delay, String imageLocation) {
        ImageIcon ii = new ImageIcon(("images/missile1.png"));
//        ImageIcon ii = new ImageIcon("C:\\Users\\Deyan\\Desktop\\Fireball.png");
        this.image = ii.getImage();

        this.horizontalSpeed = horizontalSpeed;
        this.verticalSpeed = verticalSpeed;

        this.isVisible = true;
        this.x = x;
        this.y = y;
    }


    public Image getImage() {
        return image;
    }

    public void setImage(String imageLocation) {
        this.imageLocation = imageLocation;
        ImageIcon ii = new ImageIcon(this.imageLocation);
        this.image = ii.getImage();
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public boolean isVisible() {
        return this.isVisible;
    }

    public Rectangle getBounds() {
//          Polygon p = new Polygon(new int[] { this.getX(), this.getY() + this.image.getWidth(null), this.getX(), this.getY() + this.image.getWidth(null) },
//                                  new int[] { this.getY(), this.getY(), this.getY() + this.image.getHeight(null), this.getY() + this.image.getHeight(null)},
//                                  3);
        return new Rectangle(this.getX(), this.getY(), this.image.getWidth(null), this.image.getHeight(null));
    }

    public void move() {


        if (this.y < 0 || this.x < 0 || this.x > GameField.fieldWidth + 45 || this.y > GameField.fieldHeight + 70) {
            this.setVisible(false);
        }
        this.x += this.horizontalSpeed;
        this.y -= this.verticalSpeed;
    }

    public void setVisible(boolean isVisible) {
        this.isVisible = isVisible;
    }

    public String getImageLocation() {
        return this.imageLocation;
    }
}
