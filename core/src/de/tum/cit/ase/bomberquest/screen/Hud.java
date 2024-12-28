package de.tum.cit.ase.bomberquest.screen;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import de.tum.cit.ase.bomberquest.BomberQuestGame;

/**
 * A Heads-Up Display (HUD) that displays game-specific information.
 * Includes a timer, bomb count, and enemy count.
 */
public class Hud {

    private final OrthographicCamera camera;
    private final Viewport viewport;
    private final Stage stage;

    private Integer worldTimer; // Game timer
    private float timeCount; // Accumulated time
    private static Integer bombCount; // Number of bombs available
    private static Integer enemyCount; // Number of enemies left
    public static final int TILE_SIZE_PX = 16; // Size of a grid cell in pixels
    public static final float SCALE = 2f; // Scale of the game
    private Label countdownLabel;
    private Label bombCountLabel;
    private Label enemyCountLabel;

    /**
     * Constructor for Hud.
     *
     * @param spriteBatch The SpriteBatch used to render the HUD.
     * @param font        The font to use for HUD elements.
     */
    public Hud(SpriteBatch spriteBatch, BitmapFont font) {
        this.camera = new OrthographicCamera();
        this.viewport = new FitViewport(BomberQuestGame.V_WIDTH / SCALE, BomberQuestGame.V_HEIGHT / SCALE, camera);
        this.stage = new Stage(viewport, spriteBatch);

        this.worldTimer = 300; // Starting time in seconds
        this.timeCount = 0;
        bombCount = 10; // Starting bomb count
        enemyCount = 0; // Starting enemy count

        // Set up the layout using a Table
        Table table = new Table();
        table.top();
        table.setFillParent(true);

        // Labels for game information
        countdownLabel = new Label(String.format("%03d", worldTimer), new Label.LabelStyle(font, Color.WHITE));
        bombCountLabel = new Label(String.format("Bombs: %02d", bombCount), new Label.LabelStyle(font, Color.WHITE));
        enemyCountLabel = new Label(String.format("Enemies: %02d", enemyCount), new Label.LabelStyle(font, Color.WHITE));

        // Adding labels to the table
        table.add(new Label("Time Remaining: ", new Label.LabelStyle(font, Color.WHITE))).expandX().padTop(10);
        table.add(countdownLabel).expandX().padTop(10);
        table.row();
        table.add(new Label("Bombs: ", new Label.LabelStyle(font, Color.WHITE))).expandX().padTop(10);
        table.add(bombCountLabel).expandX().padTop(10);
        table.row();
        table.add(new Label("Enemies: ", new Label.LabelStyle(font, Color.WHITE))).expandX().padTop(10);
        table.add(enemyCountLabel).expandX().padTop(10);

        // Add the table to the stage
        stage.addActor(table);
    }

    /**
     * Updates the HUD, including the timer and game statistics.
     *
     * @param deltaTime Time since the last frame.
     */
    public void update(float deltaTime) {
        timeCount += deltaTime;
        if (timeCount >= 1) {
            if (worldTimer > 0) {
                worldTimer--;
            }
            countdownLabel.setText(String.format("%03d", worldTimer));
            timeCount = 0;
        }
    }

    /**
     * Renders the HUD on the screen.
     */
    public void render() {
        stage.draw();
    }

    /**
     * Sets the bomb count in the HUD.
     *
     * @param remainingBombs Number of remaining bombs.
     */
    public void setBombCount(int remainingBombs) {
        bombCount = remainingBombs;
        bombCountLabel.setText(String.format("Bombs: %02d", bombCount));
    }

    /**
     * Sets the enemy count in the HUD.
     *
     * @param remainingEnemies Number of remaining enemies.
     */
    public void setEnemyCount(int remainingEnemies) {
        enemyCount = remainingEnemies;
        enemyCountLabel.setText(String.format("Enemies: %02d", enemyCount));
    }

    /**
     * Resizes the HUD when the screen size changes.
     *
     * @param width  The new width of the screen.
     * @param height The new height of the screen.
     */
    public void resize(int width, int height) {
        viewport.update(width, height);
    }
}
