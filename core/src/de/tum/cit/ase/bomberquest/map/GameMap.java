package de.tum.cit.ase.bomberquest.map;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.World;
import de.tum.cit.ase.bomberquest.Actors.Bomb;
import de.tum.cit.ase.bomberquest.Actors.Enemy;
import de.tum.cit.ase.bomberquest.Actors.Player;
import de.tum.cit.ase.bomberquest.Actors.Santa;
import de.tum.cit.ase.bomberquest.BomberQuestGame;
import de.tum.cit.ase.bomberquest.Handlers.CollisionDetector;
import de.tum.cit.ase.bomberquest.Handlers.MapLoader;

import java.util.Arrays;
import java.util.List;
import java.util.ArrayList;
import java.util.Random;


/**
 * Represents the game map.
 * Combines player, map size, enemies, flowers, and indestructible walls functionality.
 * https://libgdx.com/wiki/
 */

public class GameMap {

    // Box2D physics simulation parameters
    private static final float TIME_STEP = 1f / Gdx.graphics.getDisplayMode().refreshRate;
    private static final int VELOCITY_ITERATIONS = 6;
    private static final int POSITION_ITERATIONS = 2;
    private final BomberQuestGame game;
    private final World world; // Box2D world for physics simulation
    private final Player player;

    private static List<Life> lives = new ArrayList<>();
    private final Flowers[][] flowers; // Decorative flowers
    private static  List<Enemy> enemies; // List of enemies
    private final Santa santa;
    public static int enemiesGenerated;
    private List<Tile> tiles;
    private final List<Bomb> bombs = new ArrayList<>();
    private static int mapWidth=21; // Map width in tiles
    private static int mapHeight=21; // Map height in tiles
    private static final Random random = new Random(); // Random number generator
    private float physicsTime = 0; // Accumulated time since the last physics step
    private static GameMap map;
    /**
     * Constructor for GameMap.
     *
     */
    public GameMap(BomberQuestGame game, String mapFilePath) {
        this.game = game;
        this.world = new World(Vector2.Zero, true);
        this.flowers = new Flowers[mapWidth][mapHeight];
        initializeFlowers();
        //load tiles
        this.tiles = MapLoader.loadMap(this.world, mapFilePath);
        updateLifeCounter();
        // Initialize map objects
        this.player = new Player(this.world, 10, 9); // Player starts at (1, 3)

        this.enemies = new ArrayList<>();

        int enemiesGenerated = generateEnemies(this.tiles);
        this.santa=new Santa(this.world, 10, 8);

        this.world.setContactListener(new CollisionDetector(map));
        GameMap.map = this;

    }

    public World getWorld() {
        return world;
    }

    /**
     * Initializes flowers in the map.
     */

    private void initializeFlowers() {

        for (int i = 0; i < mapWidth; i++) {
            for (int j = 0; j < mapHeight; j++) {
                this.flowers[i][j] = new Flowers(i, j);
            }
        }
    }

    //update lives counter
    public static void updateLifeCounter() {
        int plives = Player.getLifeCounter();
        // Only update if life counter is more than 1
        if (plives < 1) {
            return;
        }

        // Add lives if the player's life counter increases
        while (lives.size() < plives) {
            int x = lives.size() + 1;
            int y = 0;
            lives.add(new Life(x, y));
        }

        // Remove lives if the player's life counter decreases
        while (lives.size() > plives) {
            lives.remove(lives.size() - 1);
        }
    }

    /**
     * Dynamically generates enemies with random positions.
     * checks the available free tiles, and avoids spawn positions of Santa and Player,
     * to spawn enemies only on free tiles
     * number of generated enemies is randomly selected
     *
     */
   public int generateEnemies(List<Tile> tiles) {
        int countenemies=0;
        List<Tile> freetiles=new ArrayList<>();
       // Random number of enemies (3-7)
        //find suitable tiles for enemy generation on the map
        for(Tile tile: tiles){
            int type=tile.getTileType();
            if(type != 0 && type != 1 && type != 2 && type != 5 && type != 6 && tile.getX()!=2 && tile.getY()!=2 && tile.getX()!=10 && tile.getY()!=10){
                freetiles.add(tile);
            }
        }
        System.out.println("Free tiles available: " + freetiles.size());
       int numberOfEnemies = random.nextInt(Math.min(freetiles.size(), 10)) + 1; //Maximum 10, minimum 1

        for (int i = 0; i < numberOfEnemies; i++) {
            int x = random.nextInt(freetiles.size()); // select number of enemies according to free tiles
            Tile y = freetiles.remove(x); //get the corresponding tiles from tiles table and remove sequentially to avoid position reuse
            enemies.add(new Enemy(this.world, y.getX() , y.getY()));
            countenemies++;
        }
        System.out.println("Number of enemies generated: " + countenemies);
        setEnemiesGenerated(countenemies);


        return countenemies;
    }

    /**
     * Removes dead enemies from the map after they are dead, it awaits end of death animation
     * to destroy body from game world.
     **/

    public void removeDeadEnemy(){


        for (int i = 0; i < enemies.size(); i++) {
            Enemy enemy=enemies.get(i);
            if (enemy.isDeathAnimationFinished()) {
                world.destroyBody(enemy.getHitbox());
                enemies.remove(i);

                i--;
            }
        }
    }

    /**
     * add enemy relative to powerdown, that adds 3 more enemies on the map
     **/
    public static void addEnemy() {
        map=GameMap.getMap();
        map.addDeferredAction(() -> { //3 selected positions that are always empty
            Enemy enemy1 = new Enemy(GameMap.map.getWorld(), 5, 6);
            Enemy enemy2= new Enemy(GameMap.map.getWorld(), 2, 18);
        Enemy enemy3= new Enemy(GameMap.map.getWorld(), 18, 18);
        enemies.add(enemy1);
        enemies.add(enemy2);
        enemies.add(enemy3);
                });
        setEnemiesGenerated(getEnemiesGenerated()+3);
        System.out.println("Enemy added at position");
    }





    /**
     * Updates the game state. This is called once per frame.
     *
     * @param frameTime The time that has passed since the last update.
     */
    public void tick(float frameTime, GameMap map) {
        doPhysicsStep(frameTime);
        // Physics step
        applyDeferredActions();     // Apply actions after the physics step
        // Update player
        this.player.tick(frameTime, map);
        for (Tile tile : tiles) {
            tile.tick(frameTime);
        }

        for (Life life : lives) {
            life.tick(frameTime);
        }

        // Update all enemies
        for (Enemy enemy : enemies) {
            enemy.update(frameTime);
        }

        for (Bomb bomb : bombs) {
            bomb.tick(frameTime);
        }

        bombs.removeIf(Bomb::isExpired); // Clean up expired bombs

        removeDeadEnemy();
        // Perform physics steps

    }

    /**
     * Performs as many physics steps as necessary to catch up to the given frame time.
     *
     * @param frameTime Time since last frame in seconds.
     */
    private void doPhysicsStep(float frameTime) {
        this.physicsTime += frameTime;
        while (this.physicsTime >= TIME_STEP) {
            this.world.step(TIME_STEP, VELOCITY_ITERATIONS, POSITION_ITERATIONS);
            this.physicsTime -= TIME_STEP;
        }
    }

    /**
     * Returns the player on the map.
     */
    public Player getPlayer() {
        return player;
    }


    /**
     * Returns the flowers on the map.
     */

    public List<Flowers> getFlowers() {
        return Arrays.stream(flowers).flatMap(Arrays::stream).toList();
    }

    /**
     * Returns the enemies on the map.
     */
    public List<Enemy> getEnemies() {
        return enemies;
    }

    /**
     * Returns the indestructible walls on the map.
     */

    /** Returns the tiles of a specific type. */
    public List<Tile> getTilesByType(int type) {
        return tiles.stream().filter(tile -> tile.getTileType() == type).toList();
    }

    /** Returns the width of the map. */
    public int getWidth() {
        return tiles.stream()
                .mapToInt(tile -> (int) tile.getX()) // Cast float to int
                .max()
                .orElse(0) + 1;
    }

    /** Returns the height of the map. */
    public int getHeight() {
        return tiles.stream()
                .mapToInt(tile -> (int) tile.getY()) // Cast float to int
                .max()
                .orElse(0) + 1;
    }
    public List<Tile> getTiles() {
        return tiles;
    }

    public Santa getSanta() {
        return santa;
    }

    public static int getEnemiesGenerated() {
        return enemiesGenerated;
    }

    public static void setEnemiesGenerated(int enemiesGenerated) {
        GameMap.enemiesGenerated = enemiesGenerated;
    }
    public Tile getTileAt(float x,float y) {
        if (x < 0 || x >= getWidth() || y < 0 || y >= getHeight()) {
            return null; // Return null for out-of-bounds requests
        }
        for (Tile tile : tiles) {
            if (tile.getX() == x && tile.getY() == y) {
                return tile;
            }
        }
        return null;
    }

  public static List<Life> getLives() {
        return lives;
    }

    private final List<Runnable> deferredActions = new ArrayList<>();



    public void addDeferredAction(Runnable action) {
        deferredActions.add(action);
    }

    public void applyDeferredActions() {
        for (Runnable action : deferredActions) {
            action.run();
        }
        deferredActions.clear();
    }

    public static GameMap getMap() {
        return map;
    }
}
