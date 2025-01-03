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
import de.tum.cit.ase.bomberquest.Actors.Bomb;
import de.tum.cit.ase.bomberquest.map.GameMap;

public class GameScreen implements Screen {

    public static final int TILE_SIZE_PX = 16; // Size of a grid cell in pixels
    public static final float SCALE = 0.5f; // Scale of the game

    private final BomberQuestGame game;
    private final SpriteBatch spriteBatch;
    private final GameMap map;
    private final Hud hud;
    private final OrthographicCamera mapCamera;
    private final Viewport viewport;

    public GameScreen(BomberQuestGame game) {
        this.game = game;
        this.spriteBatch = game.getSpriteBatch();
        this.map = game.getMap();
        this.hud = new Hud(game.getSpriteBatch());

        // Calculate world size
        float worldWidth = map.getWidth() * TILE_SIZE_PX * SCALE;
        float worldHeight = map.getHeight() * TILE_SIZE_PX * SCALE;

        // Create and configure the camera
        this.mapCamera = new OrthographicCamera();
        viewport = new FitViewport(worldWidth, worldHeight, mapCamera);
        mapCamera.position.set(worldWidth / 2, worldHeight / 2, 0);
        mapCamera.update();
    }

    @Override
    public void render(float deltaTime) {
        // Clear the screen
        ScreenUtils.clear(Color.BLACK);

        // Handle input to go back to the menu
        if (Gdx.input.isKeyJustPressed(Input.Keys.ESCAPE)) {
            game.goToMenu();
        }

        // Cap frame time to prevent the spiral of death
        float frameTime = Math.min(deltaTime, 0.250f);

        // Update game state
        map.tick(frameTime);
        hud.update(frameTime);
        updateCamera();

        // Render the game map
        spriteBatch.setProjectionMatrix(mapCamera.combined);
        spriteBatch.begin();
        renderMap();
        spriteBatch.end();

        // Render the HUD
        hud.render(); // HUD manages its own SpriteBatch
    }

    private void updateCamera() {
        // Update the camera position based on the player (or center the map)
        mapCamera.update();
    }

    private void renderBombs() {
        game.getSpriteBatch().begin();
        for (Bomb bomb : map.getPlayer().getBombs()) {
            float x = bomb.getX() * TILE_SIZE_PX * SCALE;
            float y = bomb.getY() * TILE_SIZE_PX * SCALE;
            float width = bomb.getCurrentAppearance().getRegionWidth() * SCALE;
            float height = bomb.getCurrentAppearance().getRegionHeight() * SCALE;

            game.getSpriteBatch().draw(bomb.getCurrentAppearance(), x, y, width, height);
        }
        game.getSpriteBatch().end();
    }
    private void renderMap() {
        for (Flowers flowers : map.getFlowers()) {
            draw(spriteBatch, flowers);
        }
        renderTiles(spriteBatch);


        for (IndestructibleWalls wall : map.getIndestructibleWalls()) {
            draw(spriteBatch, wall);
        }

        for (Enemy enemy : map.getEnemies()) {
            draw(spriteBatch, enemy);
        }

        draw(spriteBatch, map.getPlayer());
        draw(spriteBatch, map.getChest());
        for (Bomb bomb : map.getPlayer().getBombs()) {
            draw(game.getSpriteBatch(), bomb);
        }

    }

    private static void draw(SpriteBatch spriteBatch, Drawable drawable) {
        TextureRegion texture = drawable.getCurrentAppearance();
        float x = drawable.getX() * TILE_SIZE_PX * SCALE;
        float y = drawable.getY() * TILE_SIZE_PX * SCALE;
        float width = texture.getRegionWidth() * SCALE;
        float height = texture.getRegionHeight() * SCALE;
        spriteBatch.draw(texture, x, y, width, height);
    }

    private void renderTiles(SpriteBatch spriteBatch) {
        for (var tile : map.getTiles()) {
            float x = tile.getX() * TILE_SIZE_PX * SCALE;
            float y = tile.getY() * TILE_SIZE_PX * SCALE;
            float width = TILE_SIZE_PX * SCALE;
            float height = TILE_SIZE_PX * SCALE;
            spriteBatch.draw(tile.getCurrentAppearance(), x, y, width, height);
        }
    }

    @Override
    public void resize(int width, int height) {
        viewport.update(width, height);
        mapCamera.position.set(viewport.getWorldWidth() / 2, viewport.getWorldHeight() / 2, 0);
        hud.resize(width, height);
    }

    @Override
    public void pause() {}

    @Override
    public void resume() {}

    @Override
    public void show() {}

    @Override
    public void hide() {}

    @Override

    public void dispose() {
    }
}

