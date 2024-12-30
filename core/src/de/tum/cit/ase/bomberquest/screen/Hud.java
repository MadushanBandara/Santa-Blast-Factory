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
 * A Heads-Up Display (HUD) that displays information on the screen.
 * It uses a separate camera so that it is always fixed on the screen.
 */
public class Hud {
    
    /** The SpriteBatch used to draw the HUD. This is the same as the one used in the GameScreen. */
    //private final SpriteBatch spriteBatch;
    /** The font used to draw text on the screen. */
    //private final BitmapFont font;
    /** The camera used to render the HUD. */
    private final OrthographicCamera camera;

    private Viewport viewport;
    public Stage stage;

    private Integer worldTimer;
    private float timeCount;
    private static Integer bomber;
    private static Integer enemy;
    private boolean timeUp;

    private Label christmasStartLabel;
    private Label countDownLabel;

    private Label bomberLabel;
    private Label amountOfBomberLabel;

    private Label enamyLabel;
    private Label numberOfEnamyLabel;
    
    public Hud(SpriteBatch spriteBatch ) {
        //this.spriteBatch = spriteBatch;
        //this.font = font;
        this.camera = new OrthographicCamera();
        this.viewport = new FitViewport(BomberQuestGame.V_WIDTH/BomberQuestGame.SCALE,
                BomberQuestGame.V_HEIGHT/BomberQuestGame.SCALE,new OrthographicCamera());
        this.stage = new Stage(viewport,spriteBatch);


        worldTimer = 300;
        timeCount = 0;
        bomber = 10;
        enemy = 0;

        Table table = new Table();
        table.top();
        table.setFillParent(true);

        countDownLabel = new Label(String.format("%3d", worldTimer), new Label.LabelStyle(new BitmapFont(), Color.WHITE));
        christmasStartLabel = new Label("Christmas Start: " , new Label.LabelStyle(new BitmapFont(),Color.WHITE)) ;

        bomberLabel = new Label(String.format("%2d", bomber), new Label.LabelStyle(new BitmapFont(), Color.WHITE));
        amountOfBomberLabel = new Label("Bombs: " , new Label.LabelStyle(new BitmapFont(),Color.WHITE)) ;

        enamyLabel = new Label(String.format("%2d", enemy), new Label.LabelStyle(new BitmapFont(), Color.WHITE));
        numberOfEnamyLabel = new Label("Enemies: " , new Label.LabelStyle(new BitmapFont(),Color.WHITE)) ;

        table.add(christmasStartLabel).expandX().padTop(10);
        table.add(amountOfBomberLabel).expandX().padTop(10);
        table.add(numberOfEnamyLabel).expandX().padTop(10);
        table.row();
        table.add(countDownLabel).expandX();
        table.add(bomberLabel).expandX();
        table.add(enamyLabel).expandX();

        stage.addActor(table);

    }
    
    /**
     * Renders the HUD on the screen.
     * This uses a different OrthographicCamera so that the HUD is always fixed on the screen.
     */


    public void render() {

        // Render from the camera's perspective
        //spriteBatch.setProjectionMatrix(camera.combined);
        // Start drawing
        //spriteBatch.begin();
        // Draw the HUD elements
        //font.draw(spriteBatch, "Press Esc to Pause!", 10, Gdx.graphics.getHeight() - 10);

        // Finish drawing
        //spriteBatch.end();
    }

    public void update(float dt) {
        timeCount += dt;
        if (timeCount >= 1) {
            if (worldTimer > 0) {
                worldTimer--;
            } else {
                timeUp = true;
            }
            countDownLabel.setText(String.format("%03d", worldTimer));
            timeCount = 0;
        }
    }

    public void setRemainingBombs(int remainingBombs) {
        bomberLabel.setText(String.format("%02d", remainingBombs));
    }
    
    /**
     * Resizes the HUD when the screen size changes.
     * This is called when the window is resized.
     * @param width The new width of the screen.
     * @param height The new height of the screen.
     */
    public void resize(int width, int height) {
        camera.setToOrtho(false, width, height);
    }


    public boolean isTimeUp() {
        return timeUp;
    }
}
