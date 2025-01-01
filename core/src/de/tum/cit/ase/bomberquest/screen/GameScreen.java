package de.tum.cit.ase.bomberquest.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import de.tum.cit.ase.bomberquest.Actors.Bomb;
import de.tum.cit.ase.bomberquest.BomberQuestGame;
import de.tum.cit.ase.bomberquest.Handlers.KeyHandler;
import de.tum.cit.ase.bomberquest.map.Flowers;
import de.tum.cit.ase.bomberquest.Actors.Player;
import de.tum.cit.ase.bomberquest.map.Tile;
import de.tum.cit.ase.bomberquest.texture.Drawable;
import de.tum.cit.ase.bomberquest.map.GameMap;
import de.tum.cit.ase.bomberquest.texture.Textures;

import java.io.FileReader;
import java.io.IOException;
import java.util.Properties;

import static javax.management.Query.or;

/**
 * The GameScreen class is responsible for rendering the gameplay screen.
 * It handles the game logic and rendering of the game elements.
 */
public class GameScreen implements Screen {

    // Screen dimensions
    int screenWidth = Gdx.graphics.getWidth();
    int screenHeight = Gdx.graphics.getHeight();

    // Dynamic scale factor
    float newScaleX = (float) screenWidth / (21 * TILE_SIZE_PX);
    float newScaleY = (float) screenHeight / (21 * TILE_SIZE_PX);
    float newScale = Math.min(newScaleX, newScaleY);  // Maintain aspect ratio


    /**
     * The size of a grid cell in pixels.
     * This allows us to think of coordinates in terms of square grid tiles
     * (e.g. x=1, y=1 is the bottom left corner of the map)
     * rather than absolute pixel coordinates.
     */
    public static final int TILE_SIZE_PX = 16;


    /**
     * The scale of the game.
     * This is used to make everything in the game look bigger or smaller.
     */
    public static final float SCALE = 2f;

    private final BomberQuestGame game;
    //private final SpriteBatch spriteBatch;
    private final GameMap map;
    private final Hud hud;
    private final OrthographicCamera mapCamera;
    private Viewport viewport;
    private Stage stage;
    private World world;
    private KeyHandler keyHandler;



    /**
     * Constructor for GameScreen. Sets up the camera and font.
     *
     * @param game The main game class, used to access global resources and methods.
     */
    public GameScreen(BomberQuestGame game ) {
        this.game = game;
        this.map = game.getMap();
        this.hud = new Hud(game.spriteBatch);

        this.keyHandler = new KeyHandler(map);

        // Get map dimensions and calculate the world size
        float worldWidth = map.getWidth() * TILE_SIZE_PX * SCALE;
        float worldHeight = map.getHeight() * TILE_SIZE_PX * SCALE;


        // Create and configure the camera for the game view
        this.mapCamera = new OrthographicCamera();
        // Center the camera on the map
        mapCamera.position.set(worldWidth / SCALE, worldHeight / SCALE, 0);



        // Initialize FitViewport with the dynamic map size
        viewport = new FitViewport(worldWidth, worldHeight, mapCamera);
    }

    /**
     * The render method is called every frame to render the game.
     * @param deltaTime The time in seconds since the last render.
     */
    @Override
    public void render(float deltaTime) {

        // Adapt the screen based on a device
        game.spriteBatch.setProjectionMatrix(mapCamera.combined);
        // Check for escape key press to go back to the menu
        if (Gdx.input.isKeyJustPressed(Input.Keys.ESCAPE)) {
            game.goToMenu();
        }

        // Clear the previous frame from the screen, or else the picture smears
        ScreenUtils.clear(Color.BLACK);

        // Cap frame time to 250ms to prevent spiral of death
        float frameTime = Math.min(deltaTime, 0.250f);


        // Render the player movements
        keyHandler.keyHandler();


        // Update the map state
        map.tick(frameTime);

        // Update the camera
        updateCamera();


        // Render the map on the screen
        renderMap();


        // Render the HUD on the screen
        hud.update(deltaTime);
        hud.render();

    }


    private void renderTiles(SpriteBatch spriteBatch) {

        for (Tile tile : map.getTiles()) {
            // Scale the tile position and size
            float x = tile.getX() * TILE_SIZE_PX * SCALE; // Apply SCALE here
            float y = tile.getY() * TILE_SIZE_PX * SCALE;
            float width = TILE_SIZE_PX * SCALE;  // Scale the tile size
            float height = TILE_SIZE_PX * SCALE;

            //I have added a method to get texture by type in Textures class.
            spriteBatch.draw(Textures.getTextureForTileType(tile.getTileType()), x, y,width , height);  // Draw tile with the new size
        }
    }

    private void renderBombs() {
        game.spriteBatch.begin();
        for (Bomb bomb : map.getPlayer().getBombs()) {
            // Draw each bomb at its position (scaled)
            float x = bomb.getX() * TILE_SIZE_PX * SCALE; // Apply SCALE here
            float y = bomb.getY() * TILE_SIZE_PX * SCALE;
            float width = bomb.getCurrentAppearance().getRegionWidth() * SCALE;
            float height = bomb.getCurrentAppearance().getRegionHeight() * SCALE;

            game.spriteBatch.draw(bomb.getCurrentAppearance(), x, y, width, height);
        }
        game.spriteBatch.end();
    }

    /**
     * Updates the camera to match the current state of the game.
     * Currently, this just centers the camera at the origin.
     */
    private void updateCamera() {
        //mapCamera.setToOrtho(false);
        mapCamera.position.set(mapCamera.viewportWidth / 2, mapCamera.viewportHeight / 2, 0);
        mapCamera.update();
    }


    private void renderMap() {
        // This configures the spriteBatch to use the camera's perspective when rendering
        game.spriteBatch.setProjectionMatrix(mapCamera.combined);

        // Start drawing
        game.spriteBatch.begin();

        renderTiles(game.spriteBatch);

        // Render everything in the map here, in order from lowest to highest (later things appear on top)
        // You may want to add a method to GameMap to return all the drawables in the correct order

        draw(game.spriteBatch, map.getChest());

        Player player = map.getPlayer();
        if (player != null) {
            for (Bomb bomb : map.getPlayer().getBombs()) {
                draw(game.spriteBatch, bomb);
            }
        }
        draw(game.spriteBatch, map.getPlayer());

        // Finish drawing, i.e. send the drawn items to the graphics card
        game.spriteBatch.end();
    }

    /**
     * Draws this object on the screen.
     * The texture will be scaled by the game scale and the tile size.
     * This should only be called between spriteBatch.begin() and spriteBatch.end(), e.g. in the renderMap() method.
     * @param spriteBatch The SpriteBatch to draw with.
     */
    private static void draw(SpriteBatch spriteBatch, Drawable drawable) {
        TextureRegion texture = drawable.getCurrentAppearance();

        // Tile position (scaled by TILE_SIZE_PX and SCALE)
        float x = drawable.getX() * TILE_SIZE_PX * SCALE;
        float y = drawable.getY() * TILE_SIZE_PX * SCALE;

        // Scale the texture size by SCALE factor
        float width = texture.getRegionWidth() * SCALE;
        float height = texture.getRegionHeight() * SCALE;

        // Draw the object (flower, player, chest, etc.)
        spriteBatch.draw(texture, x, y, width, height);


    }

    /**
     * Called when the window is resized.
     * This is where the camera is updated to match the new window size.
     * @param width The new window width.
     * @param height The new window height.
     */
    @Override
    public void resize(int width, int height) {
        viewport.update(width, height, true);
        mapCamera.position.set(viewport.getWorldWidth() / 2, viewport.getWorldHeight() / 2, 0);
        hud.resize(width, height);
    }


    // Unused methods from the Screen interface
    @Override
    public void pause() {
    }

    @Override
    public void resume() {
    }

    @Override
    public void show() {

    }

    @Override
    public void hide() {
    }

    @Override
    public void dispose() {
    }

}
