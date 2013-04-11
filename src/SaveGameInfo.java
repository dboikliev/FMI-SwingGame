import java.io.Serializable;
import java.util.ArrayList;

public class SaveGameInfo implements Serializable {
    public Player craft;
    public ArrayList<Enemy> enemies;
    public ArrayList<Enemy> visibleEnemies;
    public ArrayList<Missile> enemyMissiles;
}
