import javax.swing.JFrame;

public class MainFrame extends JFrame {
    public MainFrame() {
        int width = Constants.FRAME_WIDTH;
        int height = Constants.FRAME_HEIGHT;

        super.setSize(width, height);

        MainMenu menu = new MainMenu(width, height, this);
        super.add(menu);

        super.setTitle("Alien Trouble");

        super.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        boolean isVisible = true;
        super.setVisible(isVisible);

        boolean isResizable = false;
        super.setResizable(isResizable);
    }

    public static void main(String[] args) {
        new MainFrame();
    }
}

