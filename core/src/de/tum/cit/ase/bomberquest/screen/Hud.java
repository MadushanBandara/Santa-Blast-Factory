package de.tum.cit.ase.bomberquest.screen;

import com.badlogic.gdx.Gdx;
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

    // HUD components
    private final OrthographicCamera camera;
    private Viewport viewport;
    public Stage stage;

    // Game state variables
    private Integer worldTimer;
    private float timeCount;
    private static Integer bomber;
    private static Integer enemy;
    private boolean timeUp;

    // UI Labels
    private Label christmasStartLabel;
    private Label countDownLabel;
    private Label bomberLabel;
    private Label amountOfBomberLabel;
    private Label enamyLabel;
    private Label numberOfEnamyLabel;

    public Hud(SpriteBatch spriteBatch) {
        this.camera = new OrthographicCamera();
        this.viewport = new FitViewport(
                BomberQuestGame.V_WIDTH / BomberQuestGame.SCALE,
                BomberQuestGame.V_HEIGHT / BomberQuestGame.SCALE,
                new OrthographicCamera()
        );
        this.stage = new Stage(viewport, spriteBatch);

        // Initialize state variables
        worldTimer = 300;
        timeCount = 0;
        bomber = 10;
        enemy = 0;

        // Set up UI Table
        Table table = new Table();
        table.top();
        table.setFillParent(true);

        countDownLabel = new Label(String.format("%3d", worldTimer), new Label.LabelStyle(new BitmapFont(), Color.WHITE));
        christmasStartLabel = new Label("Christmas Start: ", new Label.LabelStyle(new BitmapFont(), Color.WHITE));
        bomberLabel = new Label(String.format("%2d", bomber), new Label.LabelStyle(new BitmapFont(), Color.WHITE));
        amountOfBomberLabel = new Label("Bombs: ", new Label.LabelStyle(new BitmapFont(), Color.WHITE));
        enamyLabel = new Label(String.format("%2d", enemy), new Label.LabelStyle(new BitmapFont(), Color.WHITE));
        numberOfEnamyLabel = new Label("Enemies: ", new Label.LabelStyle(new BitmapFont(), Color.WHITE));

        table.add(christmasStartLabel).expandX().padBottom(0);
        table.add(countDownLabel).expandX();
        table.add(amountOfBomberLabel).expandX().padBottom(0);
        table.add(bomberLabel).expandX();
        table.add(numberOfEnamyLabel).expandX().padBottom(0);
        table.add(enamyLabel).expandX();




        stage.addActor(table);
    }

    public void render() {
        stage.getViewport().apply();
        stage.act(Gdx.graphics.getDeltaTime());
        stage.draw();
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

    public void resize(int width, int height) {
        viewport.update(width, height);
    }

    public boolean isTimeUp() {
        return timeUp;
    }
}
