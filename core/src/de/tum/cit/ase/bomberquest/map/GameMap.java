package de.tum.cit.ase.bomberquest.map;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.World;
import de.tum.cit.ase.bomberquest.Actors.Player;
import de.tum.cit.ase.bomberquest.Actors.Santa;
import de.tum.cit.ase.bomberquest.BomberQuestGame;
import de.tum.cit.ase.bomberquest.screen.Hud;

import java.util.Arrays;
import java.util.List;

import java.util.ArrayList;
import java.util.Random;

import static java.lang.Math.min;


/**
 * Represents the game map.
 * Combines player, map size, enemies, flowers, and indestructible walls functionality.
 */
public class GameMap {

    // Box2D physics simulation parameters
    private static final float TIME_STEP = 1f / Gdx.graphics.getDisplayMode().refreshRate;
    private static final int VELOCITY_ITERATIONS = 6;
    private static final int POSITION_ITERATIONS = 2;

    private final BomberQuestGame game; // Reference to the main game class
    private final World world; // Box2D world for physics simulation

    private final Player player; // The player character
    private final Chest chest; // The chest object on the map
    private final Flowers[][] flowers; // Decorative flowers
    private final List<Enemy> enemies; // List of enemies
    private final Santa santa;
    public static int enemiesGenerated=10;

    // private final List<IndestructibleWalls> indestructibleWalls; // Boundary walls
    private List<Tile> tiles;

    private static int mapWidth=21; // Map width in tiles
    private static int mapHeight=21; // Map height in tiles

    private final Random random = new Random(); // Random number generator
    private float physicsTime = 0; // Accumulated time since the last physics step
   ;

    /**
     * Constructor for GameMap.
     *

     */
    public GameMap(BomberQuestGame game, String mapFilePath) {
        this.game = game;
        this.world = new World(Vector2.Zero, true);
        // Load map tiles
        this.tiles = MapLoader.loadMap(this.world, mapFilePath);
        // Set map dimensions

        // Determine grid dimensions based on viewport size and scale
        //this.mapWidth = (int) Math.ceil(width / scale); // Calculate map width
        //this.mapHeight = (int) Math.ceil(height / scale); // Calculate map height

        // Initialize map objects
        this.player = new Player(this.world, 10, 9); // Player starts at (1, 3)
        this.chest = new Chest(world, 3, 3);// Chest is placed at (3, 3)


        this.enemies = new ArrayList<>();
       // this.indestructibleWalls = new ArrayList<>();//
        this.flowers = new Flowers[mapWidth][mapHeight];
        initializeFlowers();
        int enemiesGenerated = generateEnemies(this.tiles);
        //addMapEdges();// Uses mapWidth and mapHeight
        // Uses mapWidth and mapHeight
        this.santa=new Santa(this.world, 10, 8);
        this.world.setContactListener(new CollisionDetector());
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

    /**
     * Dynamically generates enemies with random positions.
     *
     * @return
     */
    private int generateEnemies(List<Tile> tiles) {
        int countenemies=0;
        List<Tile> freetiles=new ArrayList<>();
       // Random number of enemies (3-7)
        //find suitable tiles for enemy generation on the map
        for(Tile tile: tiles){
            int type=tile.getTileType();
            if(type != 0 && type != 1 && type != 2 && type != 5 && type != 6){
                freetiles.add(tile);
            }
        }
        System.out.println("Free tiles available: " + freetiles.size());
        int numberOfEnemies = random.nextInt(Math.min(freetiles.size(), 10)) + 1;

        for (int i = 0; i < numberOfEnemies; i++) {
            int x = random.nextInt(freetiles.size()); // select number of enemies according to free tiles
            Tile y = freetiles.remove(x); //get the corresponding tiles from tiles table and remove sequentially to avoid position reuse
            enemies.add(new Enemy(this.world, y.getX() , y.getY(), random.nextBoolean()));
            countenemies++;
        }
        System.out.println("Number of enemies generated: " + countenemies);


        return countenemies;
    }

    //private boolean freetile()//

    /**
     * Adds indestructible walls around the map edges.
     *//* method removed
    private void addMapEdges() {
        // Iterate through all tiles in the grid
        for (int i = 0; i < mapWidth; i++) { // Horizontal edges (top and bottom)
            for (int j = 0; j < mapHeight; j++) { // Vertical edges (left and right)
                if (i == 0 || i == mapWidth-1|| j == 0 || j == mapHeight-1 ) {
                    indestructibleWalls.add(new IndestructibleWalls(world, i, j));
                }
            }
        }
    }
*/
    /**
     * Updates the game state. This is called once per frame.
     *
     * @param frameTime The time that has passed since the last update.
     */
    public void tick(float frameTime) {
        // Update player
        this.player.tick(frameTime);

        // Update all enemies
        for (Enemy enemy : enemies) {
            enemy.update(frameTime);
        }

        // Perform physics steps
        doPhysicsStep(frameTime);
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
     * Returns the chest on the map.
     */
    public Chest getChest() {
        return chest;
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
    /*
    public List<IndestructibleWalls> getIndestructibleWalls() {
        return indestructibleWalls;
    }*/

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
}
