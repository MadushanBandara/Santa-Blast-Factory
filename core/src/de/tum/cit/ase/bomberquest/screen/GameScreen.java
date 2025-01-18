package de.tum.cit.ase.bomberquest.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import de.tum.cit.ase.bomberquest.Actors.Player;
import de.tum.cit.ase.bomberquest.BomberQuestGame;
import de.tum.cit.ase.bomberquest.map.*;
import de.tum.cit.ase.bomberquest.texture.Animations;
import de.tum.cit.ase.bomberquest.texture.Drawable;
import de.tum.cit.ase.bomberquest.Actors.Bomb;
import de.tum.cit.ase.bomberquest.Actors.Santa;
import de.tum.cit.ase.bomberquest.map.GameMap;

import static de.tum.cit.ase.bomberquest.Actors.Player.isAlive;

public class GameScreen implements Screen {

    public static final int TILE_SIZE_PX = 16; // Size of a grid cell in pixels
    public static final float SCALE = 0.4f; // Scale of the game

    private final BomberQuestGame game;
    private final SpriteBatch spriteBatch;
    private final GameMap map;
    private final Hud hud;
    private final OrthographicCamera mapCamera;
    private final Viewport viewport;
    private float deltaTime;
    private Player player;

    public GameScreen(BomberQuestGame game) {
        this.game = game;
        this.spriteBatch = game.getSpriteBatch();

        // Initialize game map
        int mapselection = MathUtils.random(1,5);
        this.map = new GameMap(game, "maps/map-"+mapselection+".properties");

        this.player = map.getPlayer();
        this.player.reset(player.getHitbox().getWorld(), player.getX(), player.getY());

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
        map.getSanta().tick(deltaTime);
        // Handle input to go back to the menu
        if (Gdx.input.isKeyJustPressed(Input.Keys.ESCAPE)) {
            game.goToMenu();
        }

        // Cap frame time to prevent the spiral of death
        float frameTime = Math.min(deltaTime, 0.250f);

        // Update game state
        map.tick(frameTime, map);
        hud.update(frameTime);
        updateCamera(this.map.getPlayer());

        // Render the game map
        spriteBatch.setProjectionMatrix(mapCamera.combined);
        spriteBatch.begin();
        renderMap();
        spriteBatch.end();

        // Render the HUD
        hud.render(); // HUD manages its own SpriteBatch

        if (GameStatus.GameOver() && player.getDeathAnimationTime() <= 0) {
            game.setScreen(new GameOverScreen(game)); // Show Game Over Screen
            dispose();
        }
        if(GameStatus.GameWon()){
            game.setScreen(new VictoryScreen(game)); // Show Victory Screen
            dispose();
        }

    }

    private void updateCamera(Player player) {
        // Get the player's position in pixels
        float playerX = player.getX() * TILE_SIZE_PX * SCALE;
        float playerY = player.getY() * TILE_SIZE_PX * SCALE;

        // Set the camera's position to center on the player
        mapCamera.position.set(playerX, playerY, 0);

        // Update the camera to apply the changes
        mapCamera.update();
    }



    private void renderBombs() {

        for (Bomb bomb : map.getPlayer().getBombs()) {
            if (!Bomb.isExploded()){
                float x = bomb.getX() * TILE_SIZE_PX * SCALE;
                float y = bomb.getY() * TILE_SIZE_PX * SCALE;
                float width = bomb.getCurrentAppearance().getRegionWidth() * SCALE;
                float height = bomb.getCurrentAppearance().getRegionHeight() * SCALE;
                game.getSpriteBatch().draw(bomb.getCurrentAppearance(), x, y, width, height);
        }
            else{
                float explosionWidth = (TILE_SIZE_PX * SCALE)*4;
                float explosionHeight = (TILE_SIZE_PX * SCALE)*4;
                //explosion is offset so that appears where the bomb was exploded because the bomb explosion animation is bigger than the bomb
                float offsetX = (explosionWidth - TILE_SIZE_PX * SCALE) / 2f;
                float offsetY = (explosionHeight - TILE_SIZE_PX * SCALE) / 2f;
                float x = (bomb.getX() * TILE_SIZE_PX * SCALE)-offsetX;
                float y = (bomb.getY() * TILE_SIZE_PX * SCALE)-offsetY;
                game.getSpriteBatch().draw(bomb.getCurrentAppearance(), x, y, explosionWidth, explosionHeight);

            }
    }

    }

    private void renderSanta(SpriteBatch spritebatch){
        Santa santa = map.getSanta(); // Get Santa instance

        // Draw Santa
        draw(spriteBatch, santa);

        // If saved, draw the animation near Santa
        if (Santa.isSaved()) {
            TextureRegion currentFrame = Animations.SANTAMESSAGE.getKeyFrame(santa.getElapsedTime(), false);
//Chatgpt help to make image smaller
            // Scale factor for the message animation
            float scale = 0.15f;

            // Calculate scaled dimensions
            float messageWidth = currentFrame.getRegionWidth() * scale;
            float messageHeight = currentFrame.getRegionHeight() * scale;

            // Position the animation above Santa
            float messageX = santa.getX() * TILE_SIZE_PX * SCALE+3; // Align horizontally with Santa
            float messageY = santa.getY() * TILE_SIZE_PX * SCALE + 6; // Position above Santa

            // Draw the animation with the scaled size
            spriteBatch.draw(currentFrame, messageX, messageY, messageWidth, messageHeight);
        }

    }


    private void renderMap() {

        for (Flowers flowers : map.getFlowers()) {
            draw(spriteBatch, flowers);
        }

        renderTiles(spriteBatch);
        //draw(spriteBatch, map.getExit());//render exit after tiles


       /*for (IndestructibleWalls wall : map.getIndestructibleWalls()) {
            draw(spriteBatch, wall);
        }*/

        for (Enemy enemy : map.getEnemies()) {
            draw(spriteBatch, enemy);
        }

        draw(spriteBatch, map.getPlayer());
       // draw(spriteBatch, map.getChest());
        renderSanta(spriteBatch);

        draw(spriteBatch, map.getLife());
        for (Bomb bomb : map.getPlayer().getBombs()) {
           renderBombs();
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

