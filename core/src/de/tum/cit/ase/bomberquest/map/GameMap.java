package de.tum.cit.ase.bomberquest.map;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.World;
import de.tum.cit.ase.bomberquest.Actors.Player;
import de.tum.cit.ase.bomberquest.BomberQuestGame;
import de.tum.cit.ase.bomberquest.Actors.Player;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

/**
 * Represents the game map.
 * Combines player, map size, enemies, flowers, and indestructible walls functionality.
 */
public class GameMap {

    // A static block is executed once when the class is referenced for the first time.
    static {
        // Initialize the Box2D physics engine.
        com.badlogic.gdx.physics.box2d.Box2D.init();
    }

    // Box2D physics simulation parameters (you can experiment with these if you want, but they work well as they are)
    /**
     * The time step for the physics simulation.
     * This is the amount of time that the physics simulation advances by in each frame.
     * It is set to 1/refreshRate, where refreshRate is the refresh rate of the monitor, e.g., 1/60 for 60 Hz.
     */
    private static final float TIME_STEP = 1f / Gdx.graphics.getDisplayMode().refreshRate;
    /** The number of velocity iterations for the physics simulation. */
    private static final int VELOCITY_ITERATIONS = 6;
    /** The number of position iterations for the physics simulation. */
    private static final int POSITION_ITERATIONS = 2;

    private final BomberQuestGame game; // Reference to the main game class
    private final World world; // Box2D world for physics simulation

    private final Player player; // The player character
    private final Chest chest; // The chest object on the map
    private final Flowers[][] flowers; // Decorative flowers
    private final List<Enemy> enemies; // List of enemies
    private final List<IndestructibleWalls> indestructibleWalls; // Boundary walls

    private static int mapWidth; // Map width in tiles
    private static int mapHeight; // Map height in tiles

    private final Random random = new Random(); // Random number generator
    private float physicsTime = 0; // Accumulated time since the last physics step
    /**
     * The accumulated time since the last physics step.
     * We use this to keep the physics simulation at a constant rate even if the frame rate is variable.
     */
    private float physicsTime = 0;

    /** The game, in case the map needs to access it. */
    private final BomberQuestGame game;
    /** The Box2D world for physics simulation. */
    private final World world;

    // Game objects
    private final Player player;

    private final Chest chest;

    private final Flowers[][] flowers;

    private List<Tile> tiles;


    public GameMap(BomberQuestGame game , String mapFilePath ) {
    /**
     * Constructor for GameMap.
     *
     * @param game  The main game class.
     * @param viewportWidth The width of the map in pixels.//
     * @param viewportHeight The height of the map in pixels.
     * @param tileSize scale factor for rendering.
     */
    public GameMap(BomberQuestGame game, float viewportWidth, float viewportHeight, float tileSize) {
        this.game = game;
        this.world = new World(Vector2.Zero, true);

        this.tiles = MapLoader.loadMap(mapFilePath);


        // Determine grid dimensions based on the viewport size

        // Create a player with initial position (1, 3)
        this.player = new Player(this.world, 1, 3);
        // Create a chest in the middle of the map
        this.chest = new Chest(world, 3, 3);
        // Create flowers in a 7x7 grid
        this.flowers = new Flowers[7][7];

        for (int i = 0; i < flowers.length; i++) {
            for (int j = 0; j < flowers[i].length; j++) {

        // Set map dimensions
        setMapDimensions(viewportWidth, viewportHeight, tileSize);
        // Determine grid dimensions based on viewport size and scale
        //this.mapWidth = (int) Math.ceil(width / scale); // Calculate map width
        //this.mapHeight = (int) Math.ceil(height / scale); // Calculate map height

        // Initialize map objects
        this.player = new Player(this.world, 1, 3); // Player starts at (1, 3)
        this.chest = new Chest(world, 3, 3); // Chest is placed at (3, 3)

        this.flowers = new Flowers[mapWidth][mapHeight];
        this.enemies = new ArrayList<>();
        this.indestructibleWalls = new ArrayList<>();

        addMapEdges();
        initializeFlowers();
        generateEnemies(); // Uses mapWidth and mapHeight
        // Uses mapWidth and mapHeight
    }
    private void setMapDimensions(float viewportWidth, float viewportHeight, float tileSize) {
        mapWidth = (int) Math.ceil(viewportWidth / tileSize); // Calculate number of tiles horizontally
        mapHeight = (int) Math.ceil(viewportHeight / tileSize);
        System.out.println("Map dimensions: Width = " + mapWidth + ", Height = " + mapHeight);
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

    /**
     * Dynamically generates enemies with random positions.
     */
    private void generateEnemies() {

        int numberOfEnemies = random.nextInt(5) + 3; // Random number of enemies (3-7)
        for (int i = 0; i < numberOfEnemies; i++) {
            int x = random.nextInt(mapWidth); // Use the calculated mapWidth
            int y = random.nextInt(mapHeight); // Use the calculated mapHeight
            enemies.add(new Enemy(this.world, x, y, random.nextBoolean()));
        }
    }
    }

    /**
     * Adds indestructible walls around the map edges.
     */
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

    /**
     * Updates the game state. This is called once per frame.
     * Every dynamic object in the game should update its state here.
     * @param frameTime the time that has passed since the last update
     */
    public void tick(float frameTime) {
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
     * This will update the Box2D world by the given time step.
     * @param frameTime Time since last frame in seconds
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
    public List<Tile> getTiles() {
        return tiles;
    }

    public List<Tile> getTilesByType(int type) {
        return tiles.stream().filter(tile -> tile.getTileType() == type).toList();
    }

    public int getWidth() {
        return tiles.stream().mapToInt(Tile::getX).max().orElse(0) + 1;
    }

    public int getHeight() {
        return tiles.stream().mapToInt(Tile::getY).max().orElse(0) + 1;
    }

}
