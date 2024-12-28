package de.tum.cit.ase.bomberquest.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import de.tum.cit.ase.bomberquest.BomberQuestGame;
import de.tum.cit.ase.bomberquest.map.*;
import de.tum.cit.ase.bomberquest.texture.Drawable;

/**
 * The GameScreen class is responsible for rendering the gameplay screen.
 * It handles the game logic and rendering of the game elements.
 */
public class GameScreen implements Screen {

    public static final int TILE_SIZE_PX = 16; // Size of a grid cell in pixels
    public static final float SCALE = 2f; // Scale of the game

    private final BomberQuestGame game;
    private final SpriteBatch spriteBatch;
    private final GameMap map;
    private final Hud hud;
    private final OrthographicCamera mapCamera;
    private final Viewport viewport;

    /**
     * Constructor for GameScreen. Sets up the camera and HUD.
     *
     * @param game The main game class, used to access global resources and methods.
     */
    public GameScreen(BomberQuestGame game) {
        this.game = game;
        this.spriteBatch = game.getSpriteBatch();
        this.map = game.getMap();

        // Initialize HUD
        this.hud = new Hud(spriteBatch, game.getSkin().getFont("font"));

        // Initialize camera and viewport
        this.mapCamera = new OrthographicCamera();
        this.mapCamera.setToOrtho(false, BomberQuestGame.V_WIDTH / SCALE, BomberQuestGame.V_HEIGHT / SCALE);
        this.viewport = new FitViewport(BomberQuestGame.V_WIDTH / SCALE, BomberQuestGame.V_HEIGHT / SCALE, mapCamera);
    }

    /**
     * Renders the gameplay screen every frame.
     *
     * @param deltaTime The time in seconds since the last render.
     */
    @Override
    public void render(float deltaTime) {
        // Check for escape key to go back to the menu
        if (Gdx.input.isKeyJustPressed(Input.Keys.ESCAPE)) {
            game.goToMenu();
        }

        // Clear the screen
        ScreenUtils.clear(Color.BLACK);

        // Cap frame time to prevent spirals of death
        float frameTime = Math.min(deltaTime, 0.250f);

        // Update the map state and camera
        map.tick(frameTime);
        updateCamera();

        // Render the map and HUD
        renderMap();
        hud.render();
    }

    /**
     * Updates the camera to center on the map or adjust to the player's position.
     */
    private void updateCamera() {
        mapCamera.position.set(mapCamera.viewportWidth / 2, mapCamera.viewportHeight / 2, 0);
        mapCamera.update();
    }

    /**
     * Renders the game map, including all entities.
     */
    private void renderMap() {
        spriteBatch.setProjectionMatrix(mapCamera.combined);
        spriteBatch.begin();

        // Render all map elements

        for (Flowers flowers : map.getFlowers()) {
            draw(spriteBatch, flowers);
        }

        for (IndestructibleWalls wall : map.getIndestructibleWalls()) {
            draw(spriteBatch, wall);
        }

        for (Enemy enemy : map.getEnemies()) {
            draw(spriteBatch, enemy);
        }

        draw(spriteBatch, map.getChest());
        draw(spriteBatch, map.getPlayer());

        spriteBatch.end();
    }

    /**
     * Draws a drawable object on the screen.
     *
     * @param spriteBatch The SpriteBatch to draw with.
     * @param drawable    The object to be drawn.
     */
    private static void draw(SpriteBatch spriteBatch, Drawable drawable) {
        TextureRegion texture = drawable.getCurrentAppearance();

        // Scale coordinates and texture size
        float x = drawable.getX() * TILE_SIZE_PX * SCALE;
        float y = drawable.getY() * TILE_SIZE_PX * SCALE;
        float width = texture.getRegionWidth() * SCALE;
        float height = texture.getRegionHeight() * SCALE;

        spriteBatch.draw(texture, x, y, width, height);
    }

    /**
     * Called when the window is resized.
     *
     * @param width  The new window width.
     * @param height The new window height.
     */
    @Override
    public void resize(int width, int height) {
        viewport.update(width, height, true);
        hud.resize(width, height);
    }

    // Unused methods from the Screen interface
    @Override public void pause() {}
    @Override public void resume() {}
    @Override public void show() {}
    @Override public void hide() {}
    @Override public void dispose() {}
}
