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
import de.tum.cit.ase.bomberquest.Actors.Bomb;
import de.tum.cit.ase.bomberquest.map.GameMap;
import de.tum.cit.ase.bomberquest.map.Enemy;


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
    private static Integer worldTimer;
    private static float timeCount;
    private static Integer bomber;
    private static Integer enemy;
    private static boolean timeUp;
    private static Integer blastRadius;


    // UI Labels
    private Label christmasStartLabel;
    private Label countDownLabel;
    private Label bomberLabel;
    private Label amountOfBomberLabel;
    private Label enamyLabel;
    private Label numberOfEnamyLabel;
    private Label blastRadiusLabel;
    private Label numBlastRadius;

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
        bomber = Bomb.getMaxBombs();
        enemy =Enemy.countEnemies;
        blastRadius=Bomb.getExplosionRadius();

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
        blastRadiusLabel = new Label("Blast Radius: ", new Label.LabelStyle(new BitmapFont(), Color.WHITE));
        numBlastRadius = new Label(String.format("%2d", blastRadius), new Label.LabelStyle(new BitmapFont(), Color.WHITE));


        table.add(christmasStartLabel).expandX().padBottom(0);
        table.add(countDownLabel).expandX();
        table.add(amountOfBomberLabel).expandX().padBottom(0);
        table.add(bomberLabel).expandX();
        table.add(numberOfEnamyLabel).expandX().padBottom(0);
        table.add(enamyLabel).expandX();
        table.add(blastRadiusLabel).expandX().padBottom(0);
        table.add(numBlastRadius).expandX();




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
        setRemainingBombs(Bomb.getMaxBombs());
        setEnemyCount(GameMap.getEnemiesGenerated());
        setBlastRadius(Bomb.getExplosionRadius());

    }

    public void setRemainingBombs(int remainingBombs) {
        bomberLabel.setText(String.format("%02d", remainingBombs));
    }
    public void setEnemyCount(int enemyCount) {
        this.enemy = enemyCount; // Update the HUD's enemy count variable
        enamyLabel.setText(String.format("%02d", enemyCount)); // Update the label text
    }
    public void setBlastRadius(int blastRadius) {
        numBlastRadius.setText(String.format("%02d", blastRadius));
    }


    public void resize(int width, int height) {
        viewport.update(width, height);
    }

    public static boolean isTimeUp() {
        return timeUp;
    }

    public static Integer getWorldTimer() {
        return worldTimer;
    }

    public static void setWorldTimer(Integer worldTimer) {
        Hud.worldTimer = worldTimer;
    }
}
